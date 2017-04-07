package com.vuclip.abtesthttp.ctl;

import java.io.IOException;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.service.ConditionService;
import com.vuclip.abtesthttp.service.TargetConService;
import com.vuclip.abtesthttp.util.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vuclip.abtesthttp.bean.TargetBean;
import com.vuclip.abtesthttp.service.TableSyncToRedis;
import com.vuclip.abtesthttp.service.TargetService;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.JsonUtil;


/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/21
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/target")
public class TargetMangerCtl {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
    private TargetService targetService;

	@Resource
	private TableSyncToRedis tableSyncToRedis;

	@Resource
	private ConditionService conditionService;
	@Resource
	private TargetConService targetConService;

    @RequestMapping("/list")
//  @ResponseBody
  public void getTargetTableData(HttpServletRequest request,HttpServletResponse response) {
      String projectId = request.getParameter("projectId");
	  
      String aoData = request.getParameter("aoData");
      JSONArray jsonarray = JSONArray.fromObject(aoData);
      int start = 0, pageSize = 5,sEcho=1;
      String keyWord="",name="",type="",status="",createTime="",orderByCln="",ascOrdesc ="";
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
            	  type = obj.get("value").toString();
              if (obj.get("name").equals("sSearch_3"))
            	  status = obj.get("value").toString();
              if (obj.get("name").equals("sSearch_4"))
            	  createTime = obj.get("value").toString();
              
              if (obj.get("name").equals("iSortCol_0"))
              	orderByCln = obj.get("value").toString();// orderby column
              
              if (obj.get("name").equals("sSortDir_0"))
              	ascOrdesc = obj.get("value").toString();// asc or desc
          }
          Map<String, Object> params = new HashMap<String, Object>();
          params.put("project_id", projectId);
          params.put("sEcho", sEcho);
          params.put("start", start);
          params.put("pageSize", pageSize);
          params.put("keyWord", keyWord);
          params.put("name", name);
          params.put("type", type);
          params.put("status", status);
          params.put("createTime", createTime);
          params.put("orderByCln", orderByCln);
          params.put("ascOrdesc", ascOrdesc);
          List<TargetBean> list = targetService.targetList(params);
          int count = targetService.targetListCount(params);
          response.getWriter().print(JsonUtil.tojson(list, count, sEcho));
      }catch (Exception e){
          e.printStackTrace();
      }
  }
    
    @ResponseBody
    @RequestMapping("/add")
    public String addTarget(HttpServletRequest request,HttpServletResponse response){
		String msg = "{}";
		List<TargetConBean> list = new ArrayList<TargetConBean>();
		try {
			int projectId = ConvertUtil.converterToInt(request.getParameter("projectid"));
			String name = request.getParameter("name");
			String type = request.getParameter("type");
			int countryControl = ConvertUtil.converterToInt(request.getParameter("country"));
			int pageControl = ConvertUtil.converterToInt(request.getParameter("pageid"));
			String status = request.getParameter("status");
			String description = request.getParameter("desc");

			TargetBean bean = new TargetBean();
			bean.setProjectId(projectId);
			bean.setName(name.toUpperCase());
			bean.setType(type);
			bean.setActive(status);
			bean.setCountryControl(countryControl);
			bean.setPageControl(pageControl);
			bean.setDescription(description);
			if("1".equals(type)){
				List<Map<String,Object>> conditions = conditionService.getConditions();
				for(Map<String,Object> condition:conditions){
					String idValue = request.getParameter(condition.get("name").toString());
					//now means request has this condition,then add condition to track_target_condition table.
					if("checkbox".equals(condition.get("type").toString())){
						if(idValue!=null&&Integer.parseInt(idValue)==Integer.parseInt(condition.get("id").toString())) {
							TargetConBean targetConBean = new TargetConBean();
							targetConBean.setCondition_id(Integer.parseInt(idValue));
							targetConBean.setCreatetime(DateUtil.dateFormat(new Date()));
							targetConBean.setUpdatetime(DateUtil.dateFormat(new Date()));
							list.add(targetConBean);
						}
					}else if("multiselect".equals(condition.get("type").toString())||"select".equals(condition.get("type").toString())){
						if(idValue!=null&&idValue!="") {
							String[] ids = idValue.split(",");
							for (String id : ids) {
								TargetConBean targetConBean = new TargetConBean();
								targetConBean.setCondition_id(Integer.parseInt(id));
								targetConBean.setCreatetime(DateUtil.dateFormat(new Date()));
								targetConBean.setUpdatetime(DateUtil.dateFormat(new Date()));
								list.add(targetConBean);
							}
						}
					}
				}
			}


			int count = 0;
			count = targetService.addTarget(bean,list);
			if(count>0){
				msg = "{\"success\":true,\"msg\":\"add target successfully!\"}";
				tableSyncToRedis.syncTargetTable();
				tableSyncToRedis.syncTargetConditionTable();
				tableSyncToRedis.syncConditionAndValue();
			}else{
				msg = "{\"success\":false,\"msg\":\"add target failed!\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "{\"success\":false,\"msg\":\"add target failed!\"}";
			logger.info("add target failed:" + e.getMessage());
		}
        return msg;
    }
    
	@ResponseBody
    @RequestMapping("/edit")
    public String editTarget(HttpServletRequest request,HttpServletResponse response){
		String msg = "{}";
		List<TargetConBean> list = new ArrayList<TargetConBean>();
		try {
			String method = request.getParameter("method");
			int count = 0;
			String result = "";
			if("edit".equals(method)){
				int id = ConvertUtil.converterToInt(request.getParameter("id"));
				String name = request.getParameter("name");
				String desc = request.getParameter("desc");

				String type = request.getParameter("type");
				String status = request.getParameter("status");
				int country = ConvertUtil.converterToInt(request.getParameter("country"));
				int pageId = ConvertUtil.converterToInt(request.getParameter("pageid"));

				TargetBean bean = new TargetBean();
				bean.setId(id);
				bean.setName(name.toUpperCase());
				bean.setDescription(desc);
				bean.setType(type);
				bean.setActive(status);
				bean.setCountryControl(country);
				bean.setPageControl(pageId);
				if("1".equals(type)){
					List<Map<String,Object>> conditions = conditionService.getConditions();
					for(Map<String,Object> condition:conditions){
						String idValue = request.getParameter(condition.get("name").toString());
						//now means request has this condition,then add condition to track_target_condition table.
						if("checkbox".equals(condition.get("type").toString())){
							if(idValue!=null&&Integer.parseInt(idValue)==Integer.parseInt(condition.get("id").toString())) {
								TargetConBean targetConBean = new TargetConBean();
								targetConBean.setCondition_id(Integer.parseInt(idValue));
								targetConBean.setCreatetime(DateUtil.dateFormat(new Date()));
								targetConBean.setUpdatetime(DateUtil.dateFormat(new Date()));
								list.add(targetConBean);
							}
						}else if("multiselect".equals(condition.get("type").toString())||"select".equals(condition.get("type").toString())){
							if(idValue!=null&&idValue!="") {
								String[] ids = idValue.split(",");
								for (String conId : ids) {
									TargetConBean targetConBean = new TargetConBean();
									targetConBean.setCondition_id(Integer.parseInt(conId));
									targetConBean.setCreatetime(DateUtil.dateFormat(new Date()));
									targetConBean.setUpdatetime(DateUtil.dateFormat(new Date()));
									list.add(targetConBean);
								}
							}
						}
					}
				}

				try {
					count = targetService.updateTarget(bean,list);
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("update target failed:" + e.getMessage());
				}
				result = "edit target successfully!";

			}else if("update".equals(method)){
				int id = ConvertUtil.converterToInt(request.getParameter("id"));
				int status = ConvertUtil.converterToInt(request.getParameter("status"));
				count = targetService.updateTargetStatus(id, status);
				result = "update target successfully!";

			}else if("delete".equals(method)){
				int id = ConvertUtil.converterToInt(request.getParameter("id"));
				count = targetService.delTarget(id);
				result = "delete target successfully!";
			}

			if(count>0){
				msg = "{\"success\":true,\"msg\":\""+result+"\"}";
				tableSyncToRedis.syncTargetAbout();
			}else{
				msg = "{\"success\":false,\"msg\":\"operation failed!\"}";
			}
        } catch (Exception e) {
            e.printStackTrace();
			msg = "{\"success\":false,\"msg\":\"operation failed!\"}";
        }
        return msg;
	}
	@RequestMapping("/getConByTargetId")
	public void getConByTargetId(HttpServletRequest request,HttpServletResponse response) {
		try {
			String targetid = request.getParameter("targetid");
			List<TargetConBean> list =targetConService.getConByTargetId(Integer.parseInt(targetid));
			JSONObject jObject = null;
			JSONArray resultData = new JSONArray();
			if(list!=null&&list.size()>0){
                for (TargetConBean bean:list){
                    jObject = new JSONObject();
                    try{
                        jObject.put("id", bean.getId());
                        jObject.put("name", bean.getName());
                        jObject.put("pname", bean.getPname()==null?"":bean.getPname());
						jObject.put("ptype", bean.getPtype()==null?"":bean.getPtype());
                        jObject.put("condition_id", bean.getCondition_id());
                        jObject.put("target_id", bean.getTarget_id());
                        jObject.put("type", bean.getType());
                        jObject.put("pid", bean.getPid());
                        resultData.add(jObject);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
				response.getWriter().print("{\"conditions\":"+resultData.toString()+"}");
            }else{
                response.getWriter().print("{\"conditions\":[]}");
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@ResponseBody
    @RequestMapping("/getTargetByTypeAndProid")
    public String getTargetByTypeAndProid(HttpServletRequest request,HttpServletResponse response){
		String type = request.getParameter("type");
		String proid = request.getParameter("proid");
		List<Map<String, Object>> list = targetService.getTargetByTypeAndProid(ConvertUtil.converterToInt(type), ConvertUtil.converterToInt(proid));
		JSONObject jObject = null;
		JSONArray resultData = new JSONArray();;
		if(list!=null&&list.size()>0){
			for (Map<String,Object> m:list){
				jObject = new JSONObject();
				try{
					jObject.put("id", m.get("id"));
					jObject.put("name", m.get("name"));
					jObject.put("country", m.get("country"));
					jObject.put("pageId", m.get("pageId"));
					resultData.add(jObject);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return "{\"targetInfo\":"+resultData.toString()+"}";
		}else{
			return "{\"targetInfo\":[]}";
		}
	}
	@ResponseBody
	@RequestMapping("/checkNameIsExist")
	public String checkNameIsExist(HttpServletRequest request,HttpServletResponse response){
		String uname = request.getParameter("name");
		int count = targetService.checkNameIsExist(uname.toUpperCase());
		String isexistStatus = "{}";
		if(count>0){
			isexistStatus="{\"isexist\":true,\"msg\":\"target name is already exist!\"}";
		}else{
			isexistStatus="{\"isexist\":false,\"msg\":\"target name can be used!\"}";
		}
		return isexistStatus;
	}
    
}
