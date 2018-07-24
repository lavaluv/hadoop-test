package com.ctrip.ops.sysdev.outputs;

import com.ctrip.ops.sysdev.baseplugin.BaseOutput;
import com.ctrip.ops.sysdev.render.DateFormatter;
import com.ctrip.ops.sysdev.render.TemplateRender;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.sniff.Sniffer;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@Log4j2
public class ElasticsearchHTTP extends BaseOutput {
    private final static int BULKACTION = 20000;

    private TemplateRender indexRender;
    private String indexTimezone;
    private RestClient restClient;
    private TemplateRender indexTypeRender;
    private TemplateRender idRender;
    private int bulkActions;
    private List<String> hosts;
    private List<Map> actionList = new ArrayList<>();
    private static final String BULKPATH = "/_bulk";
    private Boolean isSniff;
    private Sniffer sniffer;

    public ElasticsearchHTTP(Map config) {
        super(config);
    }

    protected void prepare() {
        try {
            if (config.containsKey("timezone")) {
                this.indexTimezone = (String) config.get("timezone");
            } else {
                this.indexTimezone = "UTC";
            }
            String index = (String) config.get("index");
            this.indexRender = TemplateRender.getRender(index, this.indexTimezone);


            this.bulkActions = getConfig(config, "bulk_actions", BULKACTION, false);
            this.indexTimezone = getConfig(config, "timezone", "UTC", false);
            this.hosts = (ArrayList<String>) getConfig(config, "hosts", null, true);
            this.isSniff = getConfig(config, "sniff", true, false);
        } catch (Exception e) {
            log.fatal("could not get correct config in ElasticsearchHTTP output:" + e);
            System.exit(1);
        }

        if (config.containsKey("document_id")) {
            String document_id = config.get("document_id").toString();
            try {
                this.idRender = TemplateRender.getRender(document_id);
            } catch (IOException e) {
                log.fatal("could not build tempalte from " + document_id);
                System.exit(1);
            }
        } else {
            this.idRender = null;
        }

        String index_type = "logs";
        if (config.containsKey("index_type")) {
            index_type = config.get("index_type").toString();
        }
        try {
            this.indexTypeRender = TemplateRender.getRender(index_type);
        } catch (IOException e) {
            log.fatal("could not build tempalte from " + index_type);
            System.exit(1);
        }

        try {
            this.initESClient();
        } catch (UnknownHostException e) {
            log.fatal("could not init  es client");
            System.exit(1);
        }
    }

    private void initESClient() throws NumberFormatException,
            UnknownHostException {

        List<HttpHost> httpHostList = hosts.stream().map(hostString -> {
            return HttpHost.create(hostString);
        }).collect(toList());
        List<HttpHost> clusterHosts = unmodifiableList(httpHostList);

        if (config.containsKey("user") && config.containsKey("password")) {
            String user = config.get("user").toString();
            String password = config.get("password").toString();
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(user, password));

            restClient = RestClient.builder(clusterHosts.toArray(new HttpHost[clusterHosts.size()]))
                    .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                        @Override
                        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        }
                    }).build();
        } else {
            restClient = RestClient.builder(clusterHosts.toArray(new HttpHost[clusterHosts.size()]))
                    .build();
        }
        if (this.isSniff) {
            sniffer = Sniffer.builder(restClient).build();
        }

    }

    protected void emit(final Map event) {
        String _index = (String) this.indexRender.render(event);
        String _indexType = (String) indexTypeRender.render(event);
        String requestBody;
        Response response = null;

        addActionList(event, _index, _indexType);
        if (this.actionList.size() / 2 >= this.bulkActions) {
            try {

                requestBody = actionList.stream().map(JSONValue::toJSONString).collect(Collectors.joining("\n")) + "\n";
                log.info(requestBody);
                response = restClient.performRequest(
                        "POST",
                        BULKPATH,
                        Collections.<String, String>emptyMap(),
                        new NStringEntity(
                                requestBody,
                                ContentType.APPLICATION_JSON
                        )
                );
                log.info(response.toString());
            } catch (IOException e) {
                log.error("Bulk index es Error:", e);
                if (response != null)
                    log.error("Response Code is " + response.getStatusLine().toString());
            } finally {
                actionList.clear();
            }
        }
    }

    private void addActionList(Map event, String _index, String _indexType) {
        Map indexAction = new HashMap() {
        };
        Map indexMap = new HashMap() {
            {
                put("_index", _index);
                put("_type", _indexType);
            }
        };
        indexAction.put("index", indexMap);
        actionList.add(indexAction);
        actionList.add(event);
    }

    public void shutdown() {
        log.info("close restClient");
        try {
            sniffer.close();
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

