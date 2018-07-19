package lava.DataIntoHBase;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.twitter.chill.Base64;


public class SparkDataFromHBase {
	private static final String HBASE_TABLE_NAME="test2";
	private static final String HBASE_COLUMNFAMILY_NAME="v";
	private static final String HBASE_QUALIFIER="dst";
	private static final int HBASE_NUM = 500000;
	@SuppressWarnings("unused")
	public static void main(String[] args)throws Exception{
		System.setProperty("spark.executor.cores", "4");
		System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		SparkConf sConf = new SparkConf()
				.setAppName("SparkDataFormHBase")
				.setMaster("yarn");
		JavaSparkContext sContext = new JavaSparkContext(sConf);
		Configuration configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum","slave1,slave2");
		configuration.set("hbase.zookeeper.property.clientPort","2181");
		configuration.set(TableInputFormat.INPUT_TABLE, HBASE_TABLE_NAME);
		
		Connection connection = ConnectionFactory.createConnection(configuration);
		Admin admin = connection.getAdmin();
		Table table = connection.getTable(TableName.valueOf(HBASE_TABLE_NAME));
		if (!admin.isTableAvailable(TableName.valueOf(HBASE_TABLE_NAME))) {
			System.err.println(HBASE_TABLE_NAME+" is not exist.");
			System.exit(1);
		}
		Scan scan = new Scan();
		scan.setCaching(1000);
		scan.setCacheBlocks(false);
		scan.setTimeRange(Long.parseLong("20180627120000"), Long.valueOf("20180627180000"));
		//scan.addColumn(Bytes.toBytes(HBASE_COLUMNFAMILY_NAME),Bytes.toBytes(HBASE_QUALIFIER));
		//query like: equal to a family:qualifier:value
		//when the query is complicated
		List<String[]> filterValue = new ArrayList<>();
//		String[] value1 = {"1","=","v","proto","[\"HTTP\",\"TCP\"]"};
//		String[] value2 = {"3","=","v","dst","\"中国\""};
		String[] value3 = {"3","!=","v","dst","\"ipv6\":\"::\""};
//		filterValue.add(value1);
//		filterValue.add(value2);
		filterValue.add(value3);
		FilterList filterList = HBaseQuery.filterComponent("MUST_PASS_ALL", filterValue);
		scan.setFilter(filterList);
		
		ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
		String scanToString = Base64.encodeBytes(proto.toByteArray());
		configuration.set(TableInputFormat.SCAN, scanToString);
		
		JavaPairRDD<ImmutableBytesWritable, Result> hbaseRDD = 
				sContext.newAPIHadoopRDD(configuration, 
						TableInputFormat.class,
						ImmutableBytesWritable.class, 
						Result.class);
		JavaRDD<String> resultRDD = hbaseRDD.repartition(8).map(tuple -> {
			Result result = tuple._2();
			byte[] row = result.getRow();
			byte[] value = result.getValue(Bytes.toBytes(HBASE_COLUMNFAMILY_NAME), Bytes.toBytes(HBASE_QUALIFIER));
			//System.out.println(new String(row)+ ":" + new String(value));
			if(row != null && value != null) {
				return new String(row)+ ":" + new String(value);
			}
			return "";
		});
		resultRDD.take(HBASE_NUM).forEach(s -> System.out.println(s));

		sContext.stop();
		sContext.close();
		admin.close();
	}
}
