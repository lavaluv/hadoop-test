import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.util.Bytes;

public class ParalleHBaseQuery {
	//buchet_size equal to thread count,it's determing by table,don't change
	private static final int BUCHET_SIZE=10;
	public static FilterList filterComponent (String operator,List<String[]> filters) {
		FilterList.Operator o = null;
		switch (operator) {
		case "MUST_PASS_ONE":
			o = Operator.MUST_PASS_ONE;
			break;
		case "MUST_PASS_ALL":
			o = Operator.MUST_PASS_ALL;
			break;
		default:
			System.err.println("Err input in filterComponent.");
			return null;
		}
		FilterList filterList = new FilterList(o);
		Iterator<String[]> iterator = filters.iterator();
		while(iterator.hasNext()) {
			String[] string = iterator.next();
			CompareOperator cOperator = null;
			switch (string[1]) {
			case "=":
				cOperator = CompareOperator.EQUAL;
				break;
			case "!=":
				cOperator = CompareOperator.NOT_EQUAL;
				break;
			case ">":
				cOperator = CompareOperator.GREATER;
				break;
			case "<":
				cOperator = CompareOperator.LESS;
				break;
			case ">=":
				cOperator = CompareOperator.GREATER_OR_EQUAL;
				break;
			case "<=":
				cOperator = CompareOperator.LESS_OR_EQUAL;
				break;
			case "no":
				cOperator = CompareOperator.NO_OP;
				break;
			default:
				System.err.println("Err input in filterComponent.");
				return null;
			}
			if (string[0] == "1") {
				//will return a row that matched
				SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
						Bytes.toBytes(string[2]), 
						Bytes.toBytes(string[3]), 
						cOperator, 
						Bytes.toBytes(string[4]));
				filterList.addFilter(singleColumnValueFilter);
			}else if(string[0] == "2") {
				//only return a cell that matched
				ColumnValueFilter columnValueFilter = new ColumnValueFilter(
						Bytes.toBytes(string[2]), 
						Bytes.toBytes(string[3]), 
						cOperator, 
						Bytes.toBytes(string[4]));
				filterList.addFilter(columnValueFilter);
			}else if(string[0] == "3") {
				//return a row that family:qualifier:value match your regex
				RegexStringComparator regexFilter = new RegexStringComparator(string[4]);
				SingleColumnValueFilter complexRegexFilter = new SingleColumnValueFilter(
						Bytes.toBytes(string[2]), 
						Bytes.toBytes(string[3]), 
						cOperator,
						regexFilter);
				filterList.addFilter(complexRegexFilter);
			}else {
				System.err.println("Err input in filterComponent.");
			}
		}
		return filterList;
	}
	public static ValueFilter simpleFilterComponent (String[] filter) {
		CompareOperator cOperator = null;
		switch (filter[1]) {
		case "=":
			cOperator = CompareOperator.EQUAL;
			break;
		case "!=":
			cOperator = CompareOperator.NOT_EQUAL;
			break;
		case ">":
			cOperator = CompareOperator.GREATER;
			break;
		case "<":
			cOperator = CompareOperator.LESS;
			break;
		case ">=":
			cOperator = CompareOperator.GREATER_OR_EQUAL;
			break;
		case "<=":
			cOperator = CompareOperator.LESS_OR_EQUAL;
			break;
		case "no":
			cOperator = CompareOperator.NO_OP;
			break;
		default:
			System.err.println("Err input in simpleFilterComponent.");
			return null;
		}
		ValueFilter valueFilter = null;
		if (filter[0] == "1") {
			valueFilter = new ValueFilter(
					cOperator,new BinaryComparator(Bytes.toBytes(filter[2])));
		}else if(filter[0] =="2") {
			valueFilter = new ValueFilter(
					cOperator, new RegexStringComparator(filter[2]));
		}else {
			System.err.println("Err input in simpleFilterComponent.");
		}
		return valueFilter;
	}
	public static void showQueryResult(Map<String, String> re,String qualifier) {
		for(Map.Entry<String, String> entry:re.entrySet()) {
			System.out.println(entry.getKey()+":"+qualifier+":"+entry.getValue());
		}
	}
	private static Runnable Query(final int in,String tableName,String columnFamilyName,String qualifier,String startRow,String endRow,int num,ArrayList<String[]> query,boolean isComplexQuery) {
		Runnable r = () -> {
			try {
				//store result in map
				NavigableMap<String, String> resultMap = new TreeMap<String,String>();
				//hbase conf
				Configuration conf = new Configuration();
				conf.set("hbase.zookeeper.quorum","slave1,slave2");
				conf.set("hbase.zookeeper.property.clientPort","2181");
				Connection connection = ConnectionFactory.createConnection(HBaseConfiguration.create(conf));
				Table table = connection.getTable(TableName.valueOf(tableName));
				byte[] cfnByte = columnFamilyName.getBytes();
				byte[] qByte = qualifier.getBytes();
				//scan conf
				Scan scan = new Scan();
				scan.setCaching(1000);
				scan.setCacheBlocks(false);
				scan.withStartRow(Bytes.toBytes(in+startRow));
				scan.withStopRow(Bytes.toBytes(in+endRow));
				
				if (isComplexQuery) {
					FilterList filterList = filterComponent("MUST_PASS_ALL", query);
					scan.setFilter(filterList);
				}
				else {
					ValueFilter valueFilter = simpleFilterComponent(query.get(0));
					scan.setFilter(valueFilter);
				}
				//get query result
				ResultScanner scanner = table.getScanner(scan);
				try {
					Result result;
					int count = 0;
					while((result = scanner.next()) != null && count++ < num) {
						byte[] row = result.getRow();
						byte[] value = result.getValue(cfnByte, qByte);
						//System.out.println(new String(row)+ ":" + new String(value));
						resultMap.put((new String(row)).replace("x", "").substring(1), new String(value));
					}
					//showQueryResult(resultMap,qualifier);
					System.out.println("Return result count is:" + resultMap.size());
				}finally {
					scanner.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		};
		return r;
	}
	public static void main(String[] args)throws Exception{
		//HBase query info
		String tableName = "test2";
		String columnFamilyName = "v";
		String qualifierName = "dst";
		String startRow = "20180627120000";
		String endRow = "20180627123000";
		int num = Integer.MAX_VALUE;
		boolean isComplexQuery = true;
		ArrayList<String[]> Query = new ArrayList<>();
		if (isComplexQuery) {
			//when the query is complicated
			//add your value like:filtertype|operator|columnFamilyName|qulifier|value
			String[] value1 = {"1","=","v","proto","[\"HTTP\",\"TCP\"]"};
			String[] value2 = {"3","=","v","dst","\"北京\""};
			Query.add(value1);
			Query.add(value2);
		}
		else {
			//simple oen query:filtertype|operater|value 
			String[] query = {"2","=","\"中国\""};
			Query.add(query);
		}
		for(int i = 0; i < BUCHET_SIZE;i++) {
			Thread t = new Thread(Query(i,tableName,columnFamilyName,qualifierName,startRow,endRow,num,Query,isComplexQuery));
			t.start();
		}
	}
}
