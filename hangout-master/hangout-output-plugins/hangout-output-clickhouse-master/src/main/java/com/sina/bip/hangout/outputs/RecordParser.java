package com.sina.bip.hangout.outputs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/*
 * parse one row from ptd to sql String
 * format input:
 * 
 */
public class RecordParser {
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
            //get the "data" in ptd json string
            if (entry.getKey().equals("data")) {
            	Iterator<JsonNode> array = entry.getValue().iterator(); 
            	while(array.hasNext()) {
            		//format string
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
            //for json like "a":{"b":value},parse to "a_b":value
            if(value.isObject()) {
            	JsonToSqlString(out,value, preKey, mapper);
            	preKey.remove(key);
            }else {
            	String v = value.toString();
            	String k = preKey.toString().replaceAll("\\[*\\]* *", "").replace(",", "_");
            	//for "ts":1234567890 with unixtimestemp
            	if(k.equals("ts")) {
					k = "ts_end";
				}
            	//for json like "a":[{"b":value}],parse to "a":["{\"b\":value}"],all cahr " replace to \"
            	if (value.isArray() && value.has(0) &&  value.get(0).isObject()) {
            		v = formatJsonArray(value, mapper);
            	//for json like d_ | r_ | http_,replace null value to [] 
				}else if(k.matches("^(d_|r_|http_|label|dns_|users_|email_)(\\S)*") && v =="null") {
					v = "[]";
				//add "time":yyyy/MM/dd
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
            	//for dst_ip|src_ip:123456789
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
                temp += ",\"" + key + "\"" + ":" + value.toString().replaceAll("\\\\\"|\"\\\\", "\"").replaceAll("\\\\", "\\\\\\\\");
            }
            temp = "{" + temp.substring(1) + "}";
            out += ",\"" + temp.replace("\"", "\\\"") + "\"";
        }
        return "[" + out.substring(1) + "]";
	}
}
