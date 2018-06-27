import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import scala.Tuple2;

public class SparkKafkaIntoHBase {
	private static final String HBASE_PKA_NAME="test2";
	private static final String[] KAFKA_TOPIC_NAME= {"test"};
	private static final String HBASE_COLUMNFAMILY_NAME="v";
	private static final long BATCH_DURATION=1000;
	public static void main(String[] args)throws Exception{
		//kafka config
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "192.168.18.143:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "spark");
		kafkaParams.put("auto.offset.reset", "earliest");
		kafkaParams.put("enable.auto.commit", true);
		//spark config
		System.setProperty("spark.executor.cores", "4");
		System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		Collection<String> topics = Arrays.asList(KAFKA_TOPIC_NAME);
		SparkConf sConf = new SparkConf()
				.setAppName("SparkKafkaIntoHBase")
				.setMaster("yarn");
		JavaStreamingContext jssc = new JavaStreamingContext(sConf,Durations.milliseconds(BATCH_DURATION));
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum","slave1,slave2");
		conf.set("hbase.zookeeper.property.clientPort","2181");
		conf.set(TableOutputFormat.OUTPUT_TABLE, HBASE_PKA_NAME);
		
		Job job = Job.getInstance(conf);
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Put.class);
		job.setOutputFormatClass(TableOutputFormat.class);
		//kafka input stream
		JavaInputDStream<ConsumerRecord<String, String>> stream =
				KafkaUtils.createDirectStream(jssc,
						LocationStrategies.PreferConsistent(),
						ConsumerStrategies.<String,String>Subscribe(topics, kafkaParams)
						);
		//for each line
		stream.foreachRDD(rdd ->{
			//map to Map<?,?> format
			JavaRDD<Map<?,?>> out = rdd.flatMap(record -> 
					RecordParser.ptd_parse(record.value()).iterator());
			//map to HBase ouput format
			JavaPairRDD<ImmutableBytesWritable, Put> outData = out
				.repartition(8)	
				.mapToPair(new PairFunction<Map<?,?>, ImmutableBytesWritable, Put>() {
				private static final long serialVersionUID = 1L;
				@Override
				public Tuple2<ImmutableBytesWritable, Put> call (Map<?,?> in) throws IOException{
						ObjectMapper mapper = new ObjectMapper();
						//set rowkey
						byte[] rowKey = RowKeyConverter.PTDRowKey(in.get("id").toString(), mapper.writeValueAsString(in.get("ts")));
						Put put = new Put(rowKey);
						put.setTimestamp(Long.valueOf((new String(rowKey)).substring(1, 15)));
						//set row data
						for(Map.Entry<?, ?> entry : in.entrySet()) {
							if (entry.getValue() != null && entry.getValue().toString() != "[]") {
								byte[] column = Bytes.toBytes(HBASE_COLUMNFAMILY_NAME);
								byte[] qualifier = Bytes.toBytes(entry.getKey().toString());
								byte[] value;
								if(entry.getValue().toString().indexOf("{") == -1 && entry.getValue().toString().indexOf("[") == -1) {
									value = Bytes.toBytes(entry.getValue().toString());
								}else {
									value = Bytes.toBytes(mapper.writeValueAsString(entry.getValue()));
								}
								put.addColumn(column, qualifier, value);
							}
						}
					return new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(), put);
				}
			});
			//save to hdfs
			outData.saveAsNewAPIHadoopDataset(job.getConfiguration());
		});
		jssc.start();
		jssc.awaitTermination();
		jssc.stop();
		jssc.close();
	}
}
