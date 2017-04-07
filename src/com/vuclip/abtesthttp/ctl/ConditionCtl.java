package com.vuclip.abtesthttp.ctl;

import com.vuclip.abtesthttp.service.ConditionService;
import com.vuclip.abtesthttp.util.TargetConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/conditionCtl")
public class ConditionCtl {
    @Resource
    private ConditionService conditionService;
    @RequestMapping("/getConditions")
    public void getConditions(HttpServletRequest request,HttpServletResponse response){
        List<Map<String,Object>> conditions  = conditionService.getConditions();
        JSONArray array = new JSONArray();
        for(Map<String ,Object> condition:conditions){
            //id,name,type,isshow,conditiontype,showcondition
            JSONObject object = new JSONObject();
            object.element("id",condition.get("id"));
            object.element("name",condition.get("name"));
            object.element("type",condition.get("type"));
            object.element("isshow",condition.get("isshow"));
            object.element("conditiontype",condition.get("conditiontype"));
            object.element("showcondition",condition.get("showcondition"));
            array.add(object);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.element("conditions", array.toString());
        try {
            PrintWriter out = response.getWriter();
            out.print(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/getConditionValue")
    public void getConditionValue(HttpServletRequest request,HttpServletResponse response){
        List<String> conNames = TargetConfig.getConNames();
        Map<String,Object> selectConNames = TargetConfig.getSelectConPids();
        Map<String,Object> conPids = TargetConfig.getMultiConPids();
        JSONObject checkboxs = new JSONObject();
        JSONObject multis = new JSONObject();
        JSONObject selects = new JSONObject();
        JSONObject json = new JSONObject();
        for(String conName:conNames){
            JSONArray jsonArr = new JSONArray();
            List<Map<String,Object>> values = conditionService.getConditionValue(conName);
            for(Map<String ,Object> value:values){
                JSONObject object = new JSONObject();
                object.element("id",value.get("id"));
                object.element("name",value.get("name"));
                jsonArr.add(object);
            }
            checkboxs.element(conName,jsonArr.toString());
        }
        for (String key : conPids.keySet()) {
            JSONArray jsonArr = new JSONArray();
            List<Map<String,Object>> values =  conditionService.getPagePaths(Integer.parseInt(conPids.get(key).toString()));//Page path
            for(Map<String ,Object> value:values){
                JSONObject object = new JSONObject();
                object.element("id",value.get("id"));
                object.element("name",value.get("name"));
                object.element("description",value.get("description"));
                object.element("type",value.get("type"));
                object.element("isshow",value.get("isshow"));
                object.element("conditiontype",value.get("conditiontype"));
                object.element("showcondition",value.get("showcondition"));
                object.element("pid", value.get("pid"));
                jsonArr.add(object);
            }
            multis.element(key,jsonArr.toString());
        }
        for (String key : selectConNames.keySet()) {
            JSONArray jsonArr = new JSONArray();
            List<Map<String,Object>> values =  conditionService.getPagePaths(Integer.parseInt(selectConNames.get(key).toString()));//Page path
            for(Map<String ,Object> value:values){
                JSONObject object = new JSONObject();
                object.element("id",value.get("id"));
                object.element("name",value.get("name"));
                object.element("description",value.get("description"));
                object.element("type",value.get("type"));
                object.element("isshow",value.get("isshow"));
                object.element("conditiontype",value.get("conditiontype"));
                object.element("showcondition",value.get("showcondition"));
                object.element("pid", value.get("pid"));
                jsonArr.add(object);
            }
            selects.element(key,jsonArr.toString());
        }
        json.element("checkboxs",checkboxs.toString());
        json.element("multiselects", multis.toString());
        json.element("selects", selects.toString());
        try {
            PrintWriter out = response.getWriter();
            out.print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
