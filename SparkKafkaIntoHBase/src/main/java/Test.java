import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	public static void main(String args[])throws Exception{
		//String s = "{\"a\":1,\"data\":[{\"a\":{\"vector\":null},\"ack\":[2807909648,2807910184,2807910721,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"alert\":true,\"bytes\":786,\"c2s\":{\"bytes\":786,\"pkts\":9},\"cp\":[{\"ip\":\"223.104.189.225\",\"ipv6\":\"::\",\"port\":58067},{\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"port\":80}],\"d\":{\"domain\":[{\"author\":\"C2\",\"domain\":\"kks.me\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}],\"file\":null,\"ip\":null,\"sengine\":null,\"url\":[{\"author\":\"C2\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"url\":\"http://kks.me/a6Vgv\",\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}]},\"dev\":\"2NHF5K2\",\"domain\":[\"kks.me\"],\"dst\":{\"city\":\"大连\",\"country\":\"中国\",\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"isp\":\"联通\",\"location\":{\"lat\":\"38.91459\",\"lon\":\"121.618622\"},\"mac\":\"224f00000001\",\"port\":80,\"region\":\"辽宁\",\"unit\":\"\"},\"file\":[],\"flow_id\":1225887219056640,\"flow_pkg_id\":126184491234,\"has_pta\":false,\"http\":{\"i\":0,\"request\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"host\":\" kks.me\",\"other\":[{\"key\":\"Host\",\"value\":\" kks.me\"},{\"key\":\"Connection\",\"value\":\" Keep-Alive\\r\\n\"},{\"key\":\"User-Agent\",\"value\":\" Apache-HttpClient/UNAVAILABLE (java 1.4)\\r\\n\"}],\"uri\":\"/a6Vgv\"},\"method\":\"GET\"},\"response\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"other\":null},\"status\":0}},\"id\":563076137912546,\"ip\":[\"223.104.189.225\",\"218.60.119.108\"],\"is_malicious\":true,\"label\":[\"木马程序\"],\"malname\":[\"Trojan/Win32.Farfli\",\"Trojan/Win32.Farfli\"],\"pid\":\"PTD\",\"pkts\":9,\"proto\":[\"HTTP\",\"TCP\"],\"protoid\":[1002,4],\"pta_analysis\":[],\"pver\":\"PTD-20g-0.1\",\"r\":{\"ip\":null},\"risk_level\":3,\"s2c\":{\"bytes\":0,\"pkts\":0},\"seq\":[169418156,169418271,169418386,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"src\":{\"city\":\"\",\"country\":\"中国\",\"ip\":\"223.104.189.225\",\"ipv6\":\"::\",\"isp\":\"移动\",\"location\":{\"lat\":\"36.675807\",\"lon\":\"117.000923\"},\"mac\":\"02062697ac58\",\"port\":58067,\"region\":\"山东\",\"unit\":\"\"},\"threat_level\":3,\"ts\":{\"end\":\"2018-05-07 23:54:13\",\"start\":\"2018-05-07 23:53:35\"},\"type\":\"cap\",\"url\":[\"http://kks.me/a6Vgv\"],\"ver\":\"0.2\"},{\"a\":{\"vector\":null},\"ack\":[1050398675,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"alert\":true,\"bytes\":377,\"c2s\":{\"bytes\":377,\"pkts\":4},\"cp\":[{\"ip\":\"112.45.233.51\",\"ipv6\":\"::\",\"port\":27718},{\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"port\":80}],\"d\":{\"domain\":[{\"author\":\"C2\",\"domain\":\"kks.me\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}],\"file\":null,\"ip\":null,\"sengine\":null,\"url\":[{\"author\":\"C2\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"url\":\"http://kks.me/a7taw\",\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}]},\"dev\":\"2NHF5K2\",\"domain\":[\"kks.me\"],\"dst\":{\"city\":\"大连\",\"country\":\"中国\",\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"isp\":\"联通\",\"location\":{\"lat\":\"38.91459\",\"lon\":\"121.618622\"},\"mac\":\"224f00000001\",\"port\":80,\"region\":\"辽宁\",\"unit\":\"\"},\"file\":[],\"flow_id\":1788837172477952,\"flow_pkg_id\":126525603616,\"has_pta\":false,\"http\":{\"i\":0,\"request\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"host\":\" kks.me\",\"other\":[{\"key\":\"Host\",\"value\":\" kks.me\"},{\"key\":\"Connection\",\"value\":\" Keep-Alive\\r\\n\"},{\"key\":\"User-Agent\",\"value\":\" Apache-HttpClient/UNAVAILABLE (java 1.4)\\r\\n\"}],\"uri\":\"/a7taw\"},\"method\":\"GET\"},\"response\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"other\":null},\"status\":0}},\"id\":281601502314272,\"ip\":[\"112.45.233.51\",\"218.60.119.108\"],\"is_malicious\":true,\"label\":[\"木马程序\"],\"malname\":[\"Trojan/Win32.Farfli\",\"Trojan/Win32.Farfli\"],\"pid\":\"PTD\",\"pkts\":4,\"proto\":[\"HTTP\",\"TCP\"],\"protoid\":[1002,4],\"pta_analysis\":[],\"pver\":\"PTD-20g-0.1\",\"r\":{\"ip\":null},\"risk_level\":3,\"s2c\":{\"bytes\":0,\"pkts\":0},\"seq\":[1071286245,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"src\":{\"city\":\"遂宁\",\"country\":\"中国\",\"ip\":\"112.45.233.51\",\"ipv6\":\"::\",\"isp\":\"移动\",\"location\":{\"lat\":\"30.513311\",\"lon\":\"105.571331\"},\"mac\":\"0206058759b1\",\"port\":27718,\"region\":\"四川\",\"unit\":\"\"},\"threat_level\":3,\"ts\":{\"end\":\"2018-05-07 23:55:14\",\"start\":\"2018-05-07 23:55:14\"},\"type\":\"cap\",\"url\":[\"http://kks.me/a7taw\"],\"ver\":\"0.2\"}]}";
		//String s = "{\"cp\":[{\"ip\":\"112.45.233.51\",\"ipv6\":\"::\",\"port\":27718},{\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"port\":80}],\"ack\":[12345,1050399212,1050399748,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}";
		//String s = "{\"d\":{\"domain\":[{\"author\":\"C2\",\"domain\":\"kks.me\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}],\"file\":null,\"ip\":null,\"sengine\":null,\"url\":[{\"author\":\"C2\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"url\":\"http://kks.me/a7fBq\",\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}]}}";
		//String s = "{\"dst\":{\"city\":\"大连\",\"country\":\"中国\",\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"isp\":\"联通\",\"location\":{\"lat\":\"38.91459\",\"lon\":\"121.618622\"},\"mac\":\"224f00000001\",\"port\":80,\"region\":\"辽宁\",\"unit\":\"\"}}";
		//String s = "{\"a\":{\"vector\":null},\"ack\":[1050398675,1050399212,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"alert\":true,\"bytes\":606,\"c2s\":{\"bytes\":606,\"pkts\":6},\"cp\":[{\"ip\":\"112.45.233.51\",\"ipv6\":\"::\",\"port\":27718},{\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"port\":80}],\"d\":{\"domain\":[{\"author\":\"C2\",\"domain\":\"kks.me\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}],\"file\":null,\"ip\":null,\"sengine\":null,\"url\":[{\"author\":\"C2\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"url\":\"http://kks.me/a7fBq\",\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}]},\"dev\":\"2NHF5K2\",\"domain\":[\"kks.me\"],\"dst\":{\"city\":\"大连\",\"country\":\"中国\",\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"isp\":\"联通\",\"location\":{\"lat\":\"38.91459\",\"lon\":\"121.618622\"},\"mac\":\"224f00000001\",\"port\":80,\"region\":\"辽宁\",\"unit\":\"\"},\"file\":[],\"flow_id\":1788837172477952,\"flow_pkg_id\":126525603616,\"has_pta\":false,\"http\":{\"i\":0,\"request\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"host\":\" kks.me\",\"other\":[{\"key\":\"Host\",\"value\":\" kks.me\"},{\"key\":\"Connection\",\"value\":\" Keep-Alive\\r\\n\"},{\"key\":\"User-Agent\",\"value\":\" Apache-HttpClient/UNAVAILABLE (java 1.4)\\r\\n\"}],\"uri\":\"/a7fBq\"},\"method\":\"GET\"},\"response\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"other\":null},\"status\":0}},\"id\":563076479024928,\"ip\":[\"112.45.233.51\",\"218.60.119.108\"],\"is_malicious\":true,\"label\":[\"木马程序\"],\"malname\":[\"Trojan/Win32.Farfli\",\"Trojan/Win32.Farfli\"],\"pid\":\"PTD\",\"pkts\":6,\"proto\":[\"HTTP\",\"TCP\"],\"protoid\":[1002,4],\"pta_analysis\":[],\"pver\":\"PTD-20g-0.1\",\"r\":{\"ip\":null},\"risk_level\":3,\"s2c\":{\"bytes\":0,\"pkts\":0},\"seq\":[1071286245,1071286360,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"src\":{\"city\":\"遂宁\",\"country\":\"中国\",\"ip\":\"112.45.233.51\",\"ipv6\":\"::\",\"isp\":\"移动\",\"location\":{\"lat\":\"30.513311\",\"lon\":\"105.571331\"},\"mac\":\"0206058759b1\",\"port\":27718,\"region\":\"四川\",\"unit\":\"\"},\"threat_level\":3,\"ts\":{\"end\":\"2018-05-07 23:55:15\",\"start\":\"2018-05-07 23:55:14\"},\"type\":\"cap\",\"url\":[\"http://kks.me/a7fBq\"],\"ver\":\"0.2\"}";
		//String s = "{\"a\":{\"vector\":null},\"ack\":[1050398675,1050399212,1050399748,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}";
		//String s = "{\"a\":1,\"data\":[{\"a\":{\"vector\":null},\"ack\":[2807909648,2807910184,2807910721,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"alert\":true,\"bytes\":786,\"c2s\":{\"bytes\":786,\"pkts\":9},\"cp\":[{\"ip\":\"223.104.189.225\",\"ipv6\":\"::\",\"port\":58067},{\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"port\":80}],\"d\":{\"domain\":[{\"author\":\"C2\",\"domain\":\"kks.me\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}],\"file\":null,\"ip\":null,\"sengine\":null,\"url\":[{\"author\":\"C2\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"url\":\"http://kks.me/a6Vgv\",\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}]},\"dev\":\"2NHF5K2\",\"domain\":[\"kks.me\"],\"dst\":{\"city\":\"大连\",\"country\":\"中国\",\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"isp\":\"联通\",\"location\":{\"lat\":\"38.91459\",\"lon\":\"121.618622\"},\"mac\":\"224f00000001\",\"port\":80,\"region\":\"辽宁\",\"unit\":\"\"},\"file\":[],\"flow_id\":1225887219056640,\"flow_pkg_id\":126184491234,\"has_pta\":false,\"http\":{\"i\":0,\"request\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"host\":\" kks.me\",\"other\":[{\"key\":\"Host\",\"value\":\" kks.me\"},{\"key\":\"Connection\",\"value\":\" Keep-Alive\\r\\n\"},{\"key\":\"User-Agent\",\"value\":\" Apache-HttpClient/UNAVAILABLE (java 1.4)\\r\\n\"}],\"uri\":\"/a6Vgv\"},\"method\":\"GET\"},\"response\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"other\":null},\"status\":0}},\"id\":563076137912546,\"ip\":[\"223.104.189.225\",\"218.60.119.108\"],\"is_malicious\":true,\"label\":[\"木马程序\"],\"malname\":[\"Trojan/Win32.Farfli\",\"Trojan/Win32.Farfli\"],\"pid\":\"PTD\",\"pkts\":9,\"proto\":[\"HTTP\",\"TCP\"],\"protoid\":[1002,4],\"pta_analysis\":[],\"pver\":\"PTD-20g-0.1\",\"r\":{\"ip\":null},\"risk_level\":3,\"s2c\":{\"bytes\":0,\"pkts\":0},\"seq\":[169418156,169418271,169418386,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"src\":{\"city\":\"\",\"country\":\"中国\",\"ip\":\"223.104.189.225\",\"ipv6\":\"::\",\"isp\":\"移动\",\"location\":{\"lat\":\"36.675807\",\"lon\":\"117.000923\"},\"mac\":\"02062697ac58\",\"port\":58067,\"region\":\"山东\",\"unit\":\"\"},\"threat_level\":3,\"ts\":{\"end\":\"2018-05-07 23:54:13\",\"start\":\"2018-05-07 23:53:35\"},\"type\":\"cap\",\"url\":[\"http://kks.me/a6Vgv\"],\"ver\":\"0.2\"},{\"a\":{\"vector\":null},\"ack\":[1050398675,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"alert\":true,\"bytes\":377,\"c2s\":{\"bytes\":377,\"pkts\":4},\"cp\":[{\"ip\":\"112.45.233.51\",\"ipv6\":\"::\",\"port\":27718},{\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"port\":80}],\"d\":{\"domain\":[{\"author\":\"C2\",\"domain\":\"kks.me\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}],\"file\":null,\"ip\":null,\"sengine\":null,\"url\":[{\"author\":\"C2\",\"malname\":\"Trojan/Win32.Farfli\",\"rid\":0,\"url\":\"http://kks.me/a7taw\",\"virus_behaviors\":[],\"virus_family\":\"Trojan/Win32.Farfli\",\"virus_platform\":\"Win32\",\"virus_type\":\"Trojan\"}]},\"dev\":\"2NHF5K2\",\"domain\":[\"kks.me\"],\"dst\":{\"city\":\"大连\",\"country\":\"中国\",\"ip\":\"218.60.119.108\",\"ipv6\":\"::\",\"isp\":\"联通\",\"location\":{\"lat\":\"38.91459\",\"lon\":\"121.618622\"},\"mac\":\"224f00000001\",\"port\":80,\"region\":\"辽宁\",\"unit\":\"\"},\"file\":[],\"flow_id\":1788837172477952,\"flow_pkg_id\":126525603616,\"has_pta\":false,\"http\":{\"i\":0,\"request\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"host\":\" kks.me\",\"other\":[],\"uri\":\"/a7taw\"},\"method\":\"GET\"},\"response\":{\"body\":{\"bytes\":0,\"direction\":\"\",\"fn\":\"\",\"ft\":\"\",\"md5\":\"\"},\"header\":{\"other\":null},\"status\":0}},\"id\":281601502314272,\"ip\":[\"112.45.233.51\",\"218.60.119.108\"],\"is_malicious\":true,\"label\":[\"木马程序\"],\"malname\":[\"Trojan/Win32.Farfli\",\"Trojan/Win32.Farfli\"],\"pid\":\"PTD\",\"pkts\":4,\"proto\":[\"HTTP\",\"TCP\"],\"protoid\":[1002,4],\"pta_analysis\":[],\"pver\":\"PTD-20g-0.1\",\"r\":{\"ip\":null},\"risk_level\":3,\"s2c\":{\"bytes\":0,\"pkts\":0},\"seq\":[1071286245,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"ts\":{\"end\":\"2018-05-07 23:55:14\",\"start\":\"2018-05-07 23:55:14\"}}]}";
		String s = "{\"data\":[{\"ts_end\":\"2015-08-19 03:45:13\"}]}";
//;		Iterator<String> iterator = ptd_parse(s).iterator();
//		while (iterator.hasNext()) {
//			String string = (String) iterator.next();
//			System.out.println(string);
//		}
		System.out.print(ptd_parse(s));
	}
	public static String ptd_parse(String in) throws IOException {
		ArrayList<String> result = new ArrayList<>();
		ArrayList<String> sList = new ArrayList<>();
		ArrayList<String> key = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(in);
		Iterator<Entry<String, JsonNode>> it = node.fields();  
        while (it.hasNext())  
        {  
            Entry<String, JsonNode> entry = it.next(); 
            if (entry.getKey().equals("data")) {
            	Iterator<JsonNode> array = entry.getValue().iterator(); 
            	while(array.hasNext()) {
            		JsonToSqlString(sList, array.next(),key, mapper);
            		result.add("{" + sList.toString().substring(1, sList.toString().length() - 1) + "}");
            		key.clear();
            		sList.clear();
            	}
            	break;
			}
        } 
		return result.toString().substring(1, result.toString().length() - 1);
	}
	public static void JsonToSqlString(ArrayList<String> out,JsonNode node,ArrayList<String> preKey,ObjectMapper mapper) {
		Iterator<Entry<String, JsonNode>> it = node.fields(); 
        while (it.hasNext())  
        {  
            Entry<String, JsonNode> entry = it.next(); 
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            preKey.add(key);
            System.out.println("input:"+key+":"+value);
            if(value.isObject()) {
            	System.out.println("objec");
            	JsonToSqlString(out,value, preKey, mapper);
            	preKey.remove(key);
//            }else if (value.isArray() && value.has(0) && (value.get(0).isArray() || value.get(0).isObject())) {
//                System.out.println("array"+ value.toString());
//                Iterator<JsonNode> iterator = value.iterator();
//                while (iterator.hasNext()) {
//					JsonNode jsonNode = (JsonNode) iterator.next();
//					JsonToSqlString(out,jsonNode, preKey, mapper);
//				}
//                preKey.remove(key);
            }else {
            	String v = value.toString();
            	String k = preKey.toString().replaceAll("\\[*\\]* *", "").replace(",", "_");
            	if(k.equals("ts")) {
					k = "ts_end";
				}
            	if (value.isArray() && value.has(0) &&  value.get(0).isObject()) {
            		v = formatJsonArray(value, mapper);
				}else if(k.matches("^(d_|r_|http_|label|dns_|users_|email_)(\\S)*")&& v =="null") {
					v = "[]";
				}else if(k.matches("^(ts_)(\\S)*")) {
					if (v.length() <12) {
						String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(Integer.parseInt(v) * 1000L)).replace("/", "-");
						if (k.equals("ts_end")) {
							out.add("\"time\":\"" + date.substring(0, 10) + "\"");
						}
						v = "\"" + date + "\"";
					}
					else if(k.equals("ts_end")){
						out.add("\"time\":" + v.substring(0, 11) + "\"");
					}
				}
				else if (k.matches("^(dst_ip|src_ip)(\\S)*") && !v.contains("\"")) {
					v = "\"" + v + "\"";
				}
            	out.add("\"" + k + "\"" + ":" + v);
            	preKey.remove(key);
            }
        } 
	}
	public static String formatJsonArray(JsonNode node,ObjectMapper mapper) {
		String out = "";
		Iterator<JsonNode> in = node.iterator(); 
        while (in.hasNext()) {
            JsonNode array = in.next(); 
            Iterator<Entry<String, JsonNode>> it = array.fields(); 
            String temp = "";
            while (it.hasNext())  
            {  
                Entry<String, JsonNode> entry = it.next(); 
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                temp += ",\"" + key + "\"" + ":" + value.toString().replace("\\\"", "\"");
            }
            temp = "{" + temp.substring(1) + "}";
            out += ",\"" + temp.replace("\"", "\\\"") + "\"";
        }
        return "[" + out.substring(1) + "]";
	}
}
