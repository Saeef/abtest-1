package com.vuclip.abtesthttp.ctl;

import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.service.ConditionService;
import com.vuclip.abtesthttp.service.ReportService;
import com.vuclip.abtesthttp.util.ConvertUtil;

import com.vuclip.abtesthttp.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/28
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/report")
@Controller
public class ReportCtl {
    @Resource
    private ReportService reportService;

    @Resource
    private ConditionService conditionService;

    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping("/abtestReport")
    public String abtestPane(HttpServletRequest request,HttpServletResponse response){
        String dorh = request.getParameter("dorh");
        String startTime = request.getParameter("startTime").replaceAll("-", "");
        String endTime = request.getParameter("endTime").replaceAll("-", "");
        String project = request.getParameter("project");
        String target = request.getParameter("target");
        String fids = request.getParameter("fids");
        String country = request.getParameter("country");
        String pageid = request.getParameter("pageid");
        StringBuffer pieBuffer = new StringBuffer();
        JSONObject dataObj = new JSONObject();
        JSONObject obj = new JSONObject(); 
        List<Map<String, Object>> list = null;
        if(dorh.equals("d")){
            dataObj = reportService.abtestDPane(ConvertUtil.converterToInt(startTime),ConvertUtil.converterToInt(endTime),ConvertUtil.converterToInt(project),ConvertUtil.converterToInt(target),fids,country,pageid);
            List<Map<String,Object>> pieList = (List<Map<String,Object>>)dataObj.get("listPie");
            List<Map<String,Object>> lineList = (List<Map<String,Object>>)dataObj.get("listLine");
            if(pieList!=null&&pieList.size()>0){
                for(Map<String,Object> map:pieList){
                	pieBuffer.append("{region:\""+map.get("name")+"\",val:"+map.get("count")+"},");
                }
                obj.element("regions", "["+pieBuffer.toString().substring(0,pieBuffer.toString().length()-1)+"]");
            }else{
            	obj.element("regions","[]");
            }
            if(lineList!=null&&lineList.size()>0){
            	List<Map<String,Object>> records = (List<Map<String,Object>>)lineList.get(0).get("record");
                List<Map<String,Object>> series = (List<Map<String,Object>>)lineList.get(0).get("series");
                List<Map<String,Object>> perrecords = (List<Map<String,Object>>)lineList.get(0).get("perrecord");
                obj.element("lineData", records.toString());
                obj.element("series", series.toString());
                obj.element("percentData", perrecords.toString());
            }else{
            	obj.element("lineData","[]");
            	obj.element("series","[]");
                obj.element("percentData", "[]");
            }
            return obj.toString();
        }else if (dorh.equals("h")){
        	dataObj = reportService.abtestHPane(ConvertUtil.converterToInt(startTime), ConvertUtil.converterToInt(endTime), ConvertUtil.converterToInt(project), ConvertUtil.converterToInt(target), fids,country,pageid);
        	List<Map<String,Object>> pieList = (List<Map<String,Object>>)dataObj.get("listPie");
            List<Map<String,Object>> lineList = (List<Map<String,Object>>)dataObj.get("listLine");
            if(pieList!=null&&pieList.size()>0){
                for(Map<String,Object> map:pieList){
                	pieBuffer.append("{region:\""+map.get("name")+"\",val:"+map.get("count")+"},");
                }
                obj.element("regions", "["+pieBuffer.toString().substring(0,pieBuffer.toString().length()-1)+"]");
            }else{
            	obj.element("regions","[]");
            }
            if(lineList.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)lineList.get(0).get("record");
                List<Map<String,Object>> series = (List<Map<String,Object>>)lineList.get(0).get("series");
                List<Map<String,Object>> perrecords = (List<Map<String,Object>>)lineList.get(0).get("perrecord");
                obj.element("lineData", records.toString());/*.replaceAll("=",":"))*/
                obj.element("series", series.toString());//.replaceAll("=",":")
                obj.element("percentData", perrecords.toString());
            }else {
            	obj.element("lineData","[]");
            	obj.element("series","[]");
                obj.element("percentData", "[]");
            }
            return obj.toString();
        }/*else if(dorh.equals("allDay")){

            list = reportService.abtestDayToDay(ConvertUtil.converterToInt(startTime),ConvertUtil.converterToInt(endTime),ConvertUtil.converterToInt(project),ConvertUtil.converterToInt(target),fids,country,pageid);
            if(list.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)list.get(0).get("record");
                List<Map<String,Object>> series = (List<Map<String,Object>>)list.get(0).get("series");
                return "{\"lineData\":"+records.toString().replaceAll("=",":")+",\"series\":"+series.toString().replaceAll("=", ":") +"}";
            }else {
                return "{\"lineData\":[],\"series\":[],\"percentData\":[]}";
            }
        }*/
        return "[]";
    }
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/rateReport")
    public String ratePane(HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> params = new HashMap<String,Object>();
        String dorh = request.getParameter("dorh");
        String startTime = request.getParameter("startTime").replaceAll("-", "");
        String endTime = request.getParameter("endTime").replaceAll("-", "");
        String project = request.getParameter("project");
        String target = request.getParameter("target");
        String fids = request.getParameter("fids");
        String country = request.getParameter("country");
        String pageid = request.getParameter("pageid");

        params.put("dorh",dorh);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        params.put("project",project);
        params.put("target",target);
        params.put("fids",fids);
        params.put("country",country);
        params.put("pageid",pageid);

        StringBuffer pieBuffer = new StringBuffer();
        StringBuffer vpPieBuffer = new StringBuffer();
        JSONObject dataObj = new JSONObject();
        JSONObject obj = new JSONObject();

        if(dorh.equals("d")){
            dataObj = reportService.rateDPane(params);
            List<Map<String,Object>> pieList = (List<Map<String,Object>>)dataObj.get("listPie");
            List<Map<String,Object>> lineList = (List<Map<String,Object>>)dataObj.get("listLine");
            if(pieList!=null&&pieList.size()>0){
                for(Map<String,Object> map:pieList){
                    pieBuffer.append("{region:\""+map.get("name")+"\",val:"+map.get("count")+"},");
                    vpPieBuffer.append("{region:\""+map.get("name")+"\",val:"+map.get("vpcount")+"},");
                }
                obj.element("regions", "["+pieBuffer.toString().substring(0,pieBuffer.toString().length()-1)+"]");
                obj.element("vpregions", "["+vpPieBuffer.toString().substring(0,vpPieBuffer.toString().length()-1)+"]");
            }else{
                obj.element("regions","[]");
                obj.element("vpregions","[]");
            }
            if(lineList!=null&&lineList.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)lineList.get(0).get("record");
                List<Map<String,Object>> countmaps = (List<Map<String,Object>>)lineList.get(0).get("countmaps");
                List<Map<String,Object>> vpcountmaps = (List<Map<String,Object>>)lineList.get(0).get("vpcountmaps");
                List<Map<String,Object>> series = (List<Map<String,Object>>)lineList.get(0).get("series");
                obj.element("lineData", records.toString());
                obj.element("countmaps", countmaps.toString());
                obj.element("vpcountmaps", vpcountmaps.toString());
                obj.element("series", series.toString());
            }else{
                obj.element("lineData","[]");
                obj.element("countmaps","[]");
                obj.element("vpcountmaps","[]");
                obj.element("series","[]");
            }
            return obj.toString();
        }else if (dorh.equals("h")){
            dataObj = reportService.rateHPane(params);
            List<Map<String,Object>> pieList = (List<Map<String,Object>>)dataObj.get("listPie");
            List<Map<String,Object>> lineList = (List<Map<String,Object>>)dataObj.get("listLine");
            if(pieList!=null&&pieList.size()>0){
                for(Map<String,Object> map:pieList){
                    pieBuffer.append("{region:\""+map.get("name")+"\",val:"+map.get("count")+"},");
                    vpPieBuffer.append("{region:\""+map.get("name")+"\",val:"+map.get("vpcount")+"},");
                    obj.element("vpregions", "["+vpPieBuffer.toString().substring(0,vpPieBuffer.toString().length()-1)+"]");
                }
                obj.element("regions", "["+pieBuffer.toString().substring(0,pieBuffer.toString().length()-1)+"]");
            }else{
                obj.element("regions","[]");
                obj.element("vpregions","[]");
            }
            if(lineList.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)lineList.get(0).get("record");
                List<Map<String,Object>> countmaps = (List<Map<String,Object>>)lineList.get(0).get("countmaps");
                List<Map<String,Object>> vpcountmaps = (List<Map<String,Object>>)lineList.get(0).get("vpcountmaps");
                List<Map<String,Object>> series = (List<Map<String,Object>>)lineList.get(0).get("series");
                obj.element("lineData", records.toString());/*.replaceAll("=",":"))*/
                obj.element("countmaps", countmaps.toString());
                obj.element("vpcountmaps", vpcountmaps.toString());
                obj.element("series", series.toString());//.replaceAll("=",":")
            }else {
                obj.element("lineData","[]");
                obj.element("countmaps","[]");
                obj.element("vpcountmaps","[]");
                obj.element("series","[]");
            }
            return obj.toString();
        }
        return "[]";
    }
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping("/trackReport")
    public String trackReport(HttpServletRequest request,HttpServletResponse response){
        String dorh = request.getParameter("dorh");
        String project = request.getParameter("project");
        String target = request.getParameter("target");
        String country = request.getParameter("country");
        String pageid = request.getParameter("pageid");
        List<Map<String,Object>> list = null;
        if(dorh.equals("d")){
            String startTime = request.getParameter("startTime").replaceAll("-", "");
            String endTime = request.getParameter("endTime").replaceAll("-", "");
            list = reportService.trackDPane(ConvertUtil.converterToInt(startTime), ConvertUtil.converterToInt(endTime), ConvertUtil.converterToInt(project), ConvertUtil.converterToInt(target),country,pageid);
            if(list!=null&&list.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)list.get(0).get("record");
                List<Map<String,Object>> series = (List<Map<String,Object>>)list.get(0).get("series");
                return "{\"lineData\":"+records.toString().replaceAll("=",":")+",\"series\":"+series.toString().replaceAll("=", ":")+"}";
            }else{
                return "{\"lineData\":[],series:[]}";
            }
        }else if (dorh.equals("h")){
            String startTime = request.getParameter("startTime").replaceAll("-", "");
            String endTime = request.getParameter("endTime").replaceAll("-", "");
            list = reportService.trackHPane(ConvertUtil.converterToInt(startTime), ConvertUtil.converterToInt(endTime), ConvertUtil.converterToInt(project), ConvertUtil.converterToInt(target),country,pageid);
            if(list.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)list.get(0).get("record");
                List<Map<String,Object>> series = (List<Map<String,Object>>)list.get(0).get("series");
                return "{\"lineData\":"+records.toString().replaceAll("=",":")+",\"series\":"+series.toString().replaceAll("=", ":") +"}";
            }else {
                return "{\"lineData\":[],series:[]}";
            }
        }else if(dorh.equals("allDay")){
            String selectDate = request.getParameter("selectDate");
            String tname = request.getParameter("tname");
            list = reportService.trackDayToDay(selectDate,ConvertUtil.converterToInt(project),ConvertUtil.converterToInt(target),tname,country,pageid);
            if(list.size()>0){
                List<Map<String,Object>> records = (List<Map<String,Object>>)list.get(0).get("record");
                List<Map<String,Object>> series = (List<Map<String,Object>>)list.get(0).get("series");
                return "{\"lineData\":"+records.toString().replaceAll("=",":")+",\"series\":"+series.toString().replaceAll("=", ":") +"}";
            }else {
                return "{\"lineData\":[],series:[]}";
            }
        }
        return "[]";
    }
    @ResponseBody
    @RequestMapping("/getTargetCountryOrPageid")
    public String getTargetCountry(HttpServletRequest request,HttpServletResponse response){
    	String targetId = request.getParameter("targetid");
    	String type = request.getParameter("type");
    	String doh = request.getParameter("doh");
    	List<Map<String,Object>> list = reportService.getTargetCountry(targetId,type,doh);
    	JSONArray arr = JSONArray.fromObject(list);
    	JSONObject object = new JSONObject();
    	object.element("data", arr.toString());
    	return object.toString();
    }
}
