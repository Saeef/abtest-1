package com.vuclip.abtesthttp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/8/7
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
public class TargetConfig {
    private static List<String> conditionNames = new ArrayList<String>();
    static Map<String,Object> multiSelectconPids = new HashMap<String, Object>();
    static Map<String,Object> selectConPids = new HashMap<String, Object>();
    static{
        conditionNames.add("User");
        multiSelectconPids.put("PagePath", 6);
        multiSelectconPids.put("Device",2);
        multiSelectconPids.put("ABTestCountry",4);
        selectConPids.put("GroupBy",1);
    }
    public static List<String> getConNames(){
        return conditionNames;
    }
    public static Map<String,Object> getMultiConPids(){
        return multiSelectconPids;
    }
    public static Map<String,Object> getSelectConPids(){
        return selectConPids;
    }
}
