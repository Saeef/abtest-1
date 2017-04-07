package com.vuclip.abtesthttp.ctl;

import com.vuclip.abtesthttp.bean.*;
import com.vuclip.abtesthttp.service.ConditionService;
import com.vuclip.abtesthttp.service.FactorConService;
import com.vuclip.abtesthttp.service.FactorService;
import com.vuclip.abtesthttp.service.TableSyncToRedis;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.DateUtil;
import com.vuclip.abtesthttp.util.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/29
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/factor")
public class FactorCtl {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private FactorService factorService;

	@Resource
	private TableSyncToRedis tableSyncToRedis;

    @Resource
    private ConditionService conditionService;

    @Resource
    private FactorConService factorConService;

    @ResponseBody
    @RequestMapping("/getFactorByTargetId")
    public String getFactorByTargetId(HttpServletRequest request,HttpServletResponse response){
        String targetid = request.getParameter("targetid");
        List<Map<String,Object>> list = null;
        try {
            list = factorService.getFactorByTargetId(ConvertUtil.converterToInt(targetid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(list!=null&&list.size()>0) {
            JSONObject object = null;
            JSONArray array = new JSONArray();
            for (Map<String, Object> m : list) {
                object = new JSONObject();
                try {
                    object.put("id", m.get("id"));
                    object.put("name", m.get("name"));
                    array.add(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "{\"factorInfo\":" + array.toString() + "}";
        }else{
            return "{\"factorInfo\":[]}";
        }
    }

    @RequestMapping("/list")
//  @ResponseBody
  public void getFactorTableData(HttpServletRequest request,HttpServletResponse response) {
      String targetId = request.getParameter("targetId");
	  
      String aoData = request.getParameter("aoData");
      JSONArray jsonarray = JSONArray.fromObject(aoData);
      int start = 0, pageSize = 5,sEcho=1;
      String keyWord="",createTime="",orderByCln="",ascOrdesc ="";
      try {
          for (int i = 0; i < jsonarray.size(); i++) {
              JSONObject obj = JSONObject.fromObject(jsonarray.get(i));
              if (obj.get("name").equals("sEcho"))
                  sEcho = ConvertUtil.converterToInt(obj.get("value").toString());//start
              if (obj.get("name").equals("iDisplayStart"))
                  start = ConvertUtil.converterToInt(obj.get("value").toString());//start
              if (obj.get("name").equals("iDisplayLength"))
                  pageSize = ConvertUtil.converterToInt(obj.get("value").toString());// pageSize

              if (obj.get("name").equals("sSearch"))
                  keyWord = obj.get("value").toString();//keyword
              if (obj.get("name").equals("sSearch_2"))
            	  createTime = obj.get("value").toString();
              
              if (obj.get("name").equals("iSortCol_0"))
              	orderByCln = obj.get("value").toString();// orderby column
              
              if (obj.get("name").equals("sSortDir_0"))
              	ascOrdesc = obj.get("value").toString();// asc or desc
          }
          Map<String, Object> params = new HashMap<String, Object>();
          params.put("target_id", targetId);
          params.put("sEcho", sEcho);
          params.put("start", start);
          params.put("pageSize", pageSize);
          params.put("keyWord", keyWord);
          params.put("createTime", createTime);
          params.put("orderByCln", orderByCln);
          params.put("ascOrdesc", ascOrdesc);
          List<FactorBean> list = factorService.factorList(params);
          int count = factorService.factorListCount(params);
          response.getWriter().print(JsonUtil.tojson(list, count, sEcho));
      }catch (Exception e){
          e.printStackTrace();
      }
  }
    
    @ResponseBody
    @RequestMapping("/add")
    public String addFactor(HttpServletRequest request,HttpServletResponse response){
    	
    	int targetId = ConvertUtil.converterToInt(request.getParameter("targetid"));
    	String name = request.getParameter("name");
    	String description = request.getParameter("desc");
    	
    	FactorBean bean = new FactorBean();
    	bean.setTargetId(targetId);
    	bean.setName(name.toUpperCase());
    	bean.setDescription(description);

        List<Map<String,Object>> conditions  = conditionService.getConditions();
        List<FactorConBean> list = new ArrayList<FactorConBean>();
        for(Map<String,Object> condition:conditions){
            if("GroupBy".equals(condition.get("name").toString())){
                List<Map<String,Object>> getGroupBys = conditionService.getGroupBys("GroupBy");
                for(Map<String,Object> map:getGroupBys){
                    String idValue = request.getParameter("fac" + map.get("name").toString());
                    //now means request has this condition,then add condition to track_factor_condition table.
                    if(idValue!=null&&idValue!=""){
                        String[] ids = idValue.split(",");
                        for (String valueId : ids) {
                            FactorConBean facConBean = new FactorConBean();
                            facConBean.setCondition_value_id(Integer.parseInt(valueId));
                            facConBean.setCreatetime(DateUtil.dateFormat(new Date()));
                            facConBean.setUpdatetime(DateUtil.dateFormat(new Date()));
                            list.add(facConBean);
                        }
                    }
                }
            }
        }
        int count = 0;
        try {
            count = factorService.addFactor(bean,list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("add factor failed:"+e.getMessage());
        }

        String msg = "{}";
        try {
            if(count>0){
                msg = "{\"success\":true,\"msg\":\"add factor successfully!\"}";
				tableSyncToRedis.syncFactorAbout();
            }else{
                msg = "{\"success\":false,\"msg\":\"add factor failed!\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
    
	@ResponseBody
    @RequestMapping("/edit")
    public String editFactor(HttpServletRequest request,HttpServletResponse response){
        String msg = "{}";
        try {
            String method = request.getParameter("method");
            int count = 0;
            String result = "";
            if("edit".equals(method)){
                int id = NumberUtils.toInt(request.getParameter("id"), 0);
                String name = request.getParameter("name");
                String description = request.getParameter("desc");

                FactorBean bean = new FactorBean();
                bean.setId(id);
                bean.setName(name.toUpperCase());
                bean.setDescription(description);

                List<Map<String,Object>> conditions  = conditionService.getConditions();
                List<FactorConBean> list = new ArrayList<FactorConBean>();
                for(Map<String,Object> condition:conditions){
                    if("GroupBy".equals(condition.get("name").toString())){
                        List<Map<String,Object>> getGroupBys = conditionService.getGroupBys("GroupBy");
                        for(Map<String,Object> map:getGroupBys){
                            String idValue = request.getParameter("fac" + map.get("name").toString());
                            //now means request has this condition,then add condition to track_factor_condition table.
                            if(idValue!=null&&idValue!=""){
                                String[] ids = idValue.split(",");
                                for (String valueId : ids) {
                                    FactorConBean facConBean = new FactorConBean();
                                    facConBean.setCondition_value_id(Integer.parseInt(valueId));
                                    facConBean.setCreatetime(DateUtil.dateFormat(new Date()));
                                    facConBean.setUpdatetime(DateUtil.dateFormat(new Date()));
                                    list.add(facConBean);
                                }
                            }
                        }
                    }
                }
                try {
                    count = factorService.updateFactor(bean,list);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("update factor failed:" + e.getMessage());
                }
                result = "edit factor successfully!";

            }else if("update".equals(method)){
                /*int id = NumberUtils.toInt(request.getParameter("id"), 0);
                int status = NumberUtils.toInt(request.getParameter("status"), 0);
                result = "update factor successfully!";*/

            }else if("delete".equals(method)){
                int id = ConvertUtil.converterToInt(request.getParameter("id"));
                try {
                    count = factorService.delFactor(id);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("delete factor failed:" + e.getMessage());
                }
                result = "delete factor successfully!";
            }
            if(count>0){
                msg = "{\"success\":true,\"msg\":\""+result+"\"}";
                tableSyncToRedis.syncFactorAbout();
            }else{
                msg = "{\"success\":false,\"msg\":\"operation failed!\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "{\"success\":false,\"msg\":\"operation failed!\"}";
        }
        return msg;
	}

	@ResponseBody
	@RequestMapping("/checkNameIsExist")
	public String checkNameIsExist(HttpServletRequest request,HttpServletResponse response){
		String uname = request.getParameter("name");
		int count = factorService.checkNameIsExist(uname.toUpperCase());
		String isexistStatus = "{}";
		if(count>0){
			isexistStatus="{\"isexist\":true,\"msg\":\"factor name is already exist!\"}";
		}else{
			isexistStatus="{\"isexist\":false,\"msg\":\"factor name can be used!\"}";
		}
		return isexistStatus;
	}
    @RequestMapping("/getConValueByFactorId")
    public void getConValueByFactorId(HttpServletRequest request,HttpServletResponse response) {
        try {
            String factorid = request.getParameter("factorid");
            List<Map<String,Object>> list =factorConService.getConValueByFactorId(Integer.parseInt(factorid));
            JSONObject jObject = null;
            JSONArray resultData = new JSONArray();
            if(list!=null&&list.size()>0){
                for (Map<String,Object> map:list){
                    jObject = new JSONObject();
                    try{
                        jObject.put("id", map.get("id"));
                        jObject.put("name", map.get("name"));
                        jObject.put("type", map.get("type"));
                        resultData.add(jObject);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                response.getWriter().print("{\"conditionValues\":"+resultData.toString()+"}");
            }else{
                response.getWriter().print("{\"conditionValues\":[]}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
