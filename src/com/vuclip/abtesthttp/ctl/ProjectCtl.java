package com.vuclip.abtesthttp.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vuclip.abtesthttp.service.ConditionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vuclip.abtesthttp.bean.ProjectBean;
import com.vuclip.abtesthttp.bean.ProjectBean1;
import com.vuclip.abtesthttp.bean.UserBean;
import com.vuclip.abtesthttp.service.ProjectService;
import com.vuclip.abtesthttp.service.TableSyncToRedis;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.JsonUtil;
import com.vuclip.abtesthttp.util.Pagination;
import com.vuclip.abtesthttp.util.ResponseUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/26
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/project")
public class ProjectCtl {
    @Resource
    private ProjectService projectService;
    @Resource
    private ConditionService conditionService;

    @Resource
    private TableSyncToRedis tableSyncToRedis;

    @RequestMapping("/toManager")
    public String toManager(HttpServletRequest request,HttpServletResponse response){
        List<Map<String ,Object>> conditions = conditionService.getConditions();
        List<Map<String ,Object>> commoncons = conditionService.getCommonConditions();
        List<Map<String ,Object>> ratecons = conditionService.getRateConditions();
        List<Map<String ,Object>> ratehidcons = conditionService.getRateHidConditions();
        UserBean user = (UserBean)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/login/index";
        }
        request.getSession().setAttribute("conditions",conditions);
        request.setAttribute("conditions",conditions);
        request.setAttribute("commoncons",commoncons);
        request.setAttribute("ratecons",ratecons);
        request.setAttribute("ratehidcons",ratehidcons);
        return ResponseUtil.TARGETMANAGE;
    }
    @RequestMapping("/getProject")
    @ResponseBody
    public String getProject(HttpServletRequest request,HttpServletResponse response){
        UserBean user = (UserBean)request.getSession().getAttribute("user");
        List<ProjectBean1> allProjects = projectService.queryAllProjects(user.getUserid());
        JSONObject jObject = null;
        JSONArray resultData = new JSONArray();
        if(allProjects!=null&&allProjects.size()>0){
            for (ProjectBean1 pro:allProjects){
                jObject = new JSONObject();
                try{
                    jObject.put("id", pro.getId());
                    jObject.put("name", pro.getName());
                    jObject.put("description", pro.getDescription());
                    jObject.put("createtime", pro.getCreatetime());
                    jObject.put("active", pro.getActive());
                    jObject.put("userid", pro.getUserId());
                    resultData.add(jObject);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            return "{\"projectInfo\":"+resultData.toString()+"}";
        }else{
            return "{\"projectInfo\":[]}";
        }
    }
    @RequestMapping("/list")
//    @ResponseBody
    public void getProjectTableData(HttpServletRequest request,HttpServletResponse response) {
        UserBean user = (UserBean) request.getSession().getAttribute("user");
        String aoData = request.getParameter("aoData");
        JSONArray jsonarray = JSONArray.fromObject(aoData);
        int start = 0, pageSize = 5,sEcho=1;
        String keyWord="",name="",createTime="",status="",orderByCln="",ascOrdesc ="";
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
                if (obj.get("name").equals("sSearch_0"))
                    name = obj.get("value").toString();//column 1
                if (obj.get("name").equals("sSearch_2"))
                    createTime = obj.get("value").toString();// column 3
                if (obj.get("name").equals("sSearch_3"))
                    status = obj.get("value").toString();// column 4

                if (obj.get("name").equals("iSortCol_0"))
                	orderByCln = obj.get("value").toString();// orderby column

                if (obj.get("name").equals("sSortDir_0"))
                	ascOrdesc = obj.get("value").toString();// asc or desc
            }
            List<ProjectBean1> list = projectService.projectList(sEcho,start, pageSize,user.getUserid(),keyWord,name,createTime,status,orderByCln,ascOrdesc);
            int count = projectService.projectListCount(user.getUserid(),keyWord,name,createTime,status);
            response.getWriter().print(JsonUtil.tojson(list, count, sEcho));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping("/add")
    public String savePro(HttpServletRequest request,HttpServletResponse response){
        String proname = request.getParameter("proname");
        String prodesc = request.getParameter("prodesc");
        String status = request.getParameter("status");
        UserBean user = (UserBean)request.getSession().getAttribute("user");

        ProjectBean domain = new ProjectBean();
        domain.setName(proname.toUpperCase());
        domain.setDescription(prodesc);
        domain.setActive(ConvertUtil.converterToInt(status));
        domain.setUserId(user.getUserid());
        int count = projectService.addProject(domain);
        String addStatus = "{}";
        if(count>0){
            addStatus="{\"success\":true,\"msg\":\"project add successed.\"}";
            tableSyncToRedis.syncProjectTable();
        }else{
            addStatus="{\"success\":false,\"msg\":\"project add failed.\"}";
        }

        return addStatus;
    }

    @ResponseBody
    @RequestMapping("/edit")
    public String updateProject(HttpServletRequest request,HttpServletResponse response){

    	String method = request.getParameter("method");
        String addStatus = "{}";
        try {
            int count = 0;
            String msg = "";
            if ("edit".equals(method)) {
                int id = ConvertUtil.converterToInt(request.getParameter("id"));
                String proname = request.getParameter("proname");
                String prodesc = request.getParameter("prodesc");
                String status = request.getParameter("status");
                ProjectBean bean = new ProjectBean();
                bean.setId(id);
                bean.setName(proname.toUpperCase());
                bean.setDescription(prodesc);
                bean.setActive(ConvertUtil.converterToInt(status));
                count = projectService.updateProject(bean);
                msg = "edit project successfully!";
            } else if ("update".equals(method)) {
                int id = ConvertUtil.converterToInt(request.getParameter("id"));
                int status = ConvertUtil.converterToInt(request.getParameter("status"));
                count = projectService.updateProjectStatus(id, status);
                msg = "update project successfully!";
            } else if ("delete".equals(method)) {
                int id = ConvertUtil.converterToInt(request.getParameter("id"));
                count = projectService.delProject(id);
                msg = "delete project successfully!";
            }
            // update db success,then update redis.
            if (count > 0) {
                addStatus = "{\"success\":true,\"msg\":\"" + msg + "\"}";
                if("delete".equals(method)){
                    tableSyncToRedis.syncProjectAbout();
                }else{
                    tableSyncToRedis.syncProjectTable();
                }
            } else {
                addStatus = "{\"success\":true,\"msg\":\"operation failed!\"}";
            }
        }catch (Exception e){
            e.printStackTrace();
            addStatus = "{\"success\":false,\"msg\":\"operation failed!\"}";
        }
    	return addStatus;
    }
    @ResponseBody
    @RequestMapping("/checkNameIsExist")
    public String checkNameIsExist(HttpServletRequest request,HttpServletResponse response){
        String uname = request.getParameter("name");
        int count = projectService.checkNameIsExist(uname.toUpperCase());
        String isexistStatus = "{}";
        if(count>0){
            isexistStatus="{\"isexist\":true,\"msg\":\"project name is already exist!\"}";
        }else{
            isexistStatus="{\"isexist\":false,\"msg\":\"project name can be used!\"}";
        }
        return isexistStatus;
    }

}
