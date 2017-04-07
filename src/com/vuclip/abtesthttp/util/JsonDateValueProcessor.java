package com.vuclip.abtesthttp.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class JsonDateValueProcessor implements JsonValueProcessor {

  private String format ="yyyy-MM-dd HH:mm:ss";
     
  public Object processArrayValue(Object value, JsonConfig config) {
    return process(value);
  }
  
  public Object processObjectValue(String key, Object value, JsonConfig config) {
    return process(value);
  }
     
  private Object process(Object value){
       
    if(value instanceof Date){
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(value);
    }
    return value == null ? "" : value.toString();
  }
}