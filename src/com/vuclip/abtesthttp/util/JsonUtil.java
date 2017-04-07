package com.vuclip.abtesthttp.util;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;


public class JsonUtil {
	public static String tojson(List list,int count,int sEcho) throws ParseException {
        String json = null; // return json data
        String aaData=JSONArray.fromObject(list,getJsonConfig()).toString();
        json = "{\"sEcho\":"+sEcho+",\"iTotalRecords\":"+count+",\"iTotalDisplayRecords\":"+count+",\"aaData\":"+aaData+"}";
        return json;
    }
	public static JsonConfig getJsonConfig(){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());
        return jsonConfig;
    }
}
