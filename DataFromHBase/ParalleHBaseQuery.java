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
	private static final int BUCHET_SIZE=4;
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
	private static Runnable myRunnable(final int in,String tableName,String columnFamilyName,String qualifier,String startRow,String endRow,int num) {
		Runnable r = () -> {
			try {
				//when the query is complicated
				List<String[]> filterValue = new ArrayList<>();
				String[] value1 = {"1","=","v","proto","[\"HTTP\",\"TCP\"]"};
				String[] value2 = {"3","=","v","dst","\"北京\""};
				filterValue.add(value1);
				filterValue.add(value2);
				FilterList filterList = filterComponent("MUST_PASS_ALL", filterValue);
				
				//query like: equal to a family:qualifier:value
//				String[] simpleValue = {"2","=","\"中国\""};
//				ValueFilter valueFilter= simpleFilterComponent(simpleValue);
				
				NavigableMap<String, String> resultMap = new TreeMap<String,String>();
				Configuration conf = new Configuration();
				conf.set("hbase.zookeeper.quorum","slave2");
				conf.set("hbase.zookeeper.property.clientPort","2181");
				Connection connection = ConnectionFactory.createConnection(HBaseConfiguration.create(conf));
				Table table = connection.getTable(TableName.valueOf(tableName));
				byte[] cfnByte = columnFamilyName.getBytes();
				byte[] qByte = qualifier.getBytes();
				Scan scan = new Scan();
				scan.setCaching(1000);
				scan.setCacheBlocks(false);
				scan.withStartRow(Bytes.toBytes(in+startRow));
				scan.withStopRow(Bytes.toBytes(in+endRow));
				//scan.addColumn(cfnByte, qByte);
//				if(valueFilter != null) {
//					scan.setFilter(valueFilter);
//				}
				scan.setFilter(filterList);
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
					showQueryResult(resultMap,qualifier);
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
		//HBase info
		String tableName = "test2";
		String columnFamilyName = "v";
		String qualifierName = "dst";
		String startRow = "20180625120000";
		String endRow = "20180625130000";
		int num = Integer.MAX_VALUE;
		for(int i = 0; i < BUCHET_SIZE;i++) {
			Thread t = new Thread(myRunnable(i,tableName,columnFamilyName,qualifierName,startRow,endRow,num));
			t.start();
		}
	}
}
