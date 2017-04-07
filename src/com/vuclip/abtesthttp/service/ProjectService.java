package com.vuclip.abtesthttp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vuclip.abtesthttp.dao.FactorDao;
import com.vuclip.abtesthttp.dao.TargetDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.abtesthttp.bean.ProjectBean;
import com.vuclip.abtesthttp.bean.ProjectBean1;
import com.vuclip.abtesthttp.dao.ProjectDao;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.Pagination;

@Transactional
@Service
public class ProjectService {
	
	private static Map<Integer,String> index2Name = new HashMap<Integer,String>();
	
	static{
		index2Name.put(0, "name");
		index2Name.put(1, "description");
		index2Name.put(2, "createtime");
		index2Name.put(3, "active");
	}
	@Resource
    private ProjectDao projectDao;
    @Resource
    private TargetDao targetDao;
    @Resource
    private FactorDao factorDao;
	
    public int addProject(ProjectBean bean){
        int count =0;
        try {
            count = projectDao.addProject(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
	}
    
    public int updateProject(ProjectBean bean){
        int count =0;
        try {
            count = projectDao.updateProject(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public int delProject(int projectId) throws Exception{
        int count = 0;
        projectDao.delProject(projectId);
        targetDao.delTargetByProId(projectId);
        factorDao.delFactorByProId(projectId);
        count=1;
        return count;
    }
    
    public int updateProjectStatus(int id, int status){
        int count =0;
        try {
           count = projectDao.updateProjectStatus(id, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public Pagination<ProjectBean> queryProject(Integer currentPage, Integer pageSize, Map<String, String> params){
    	return projectDao.queryProject(currentPage, pageSize, params);
    }

    public int checkNameIsExist(String uname) {
        int count = 0;
        try {
            count = projectDao.checkNameIsExist(uname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<ProjectBean1> projectList(int sEcho,int start, int pageSize, int userid, String keyWord, String name, String createTime, String status,String orderByCln,String ascOrdesc) {
        String sql = "SELECT  id,name,description,case active WHEN 1 THEN 'inactive' WHEN 0 THEN 'active' end  as active,createtime,updatetime,userId FROM track_project where 1=1 and del=0 and userid = "+userid;
        String active = "";
        if(keyWord!=null &&!"".equals(keyWord)){
        	if("active".equals(keyWord)){
        		active = "0";
        	}else if("inactive".equals(keyWord)){
        		active = "1";
        	}
            sql+=" and (name like '%"+keyWord+"%' or description like '%"+keyWord+"%' or active like '%"+(active==""?keyWord:active)+"%' or createtime like '%"+keyWord+"%')";
        }
        if(name!=null &&!"".equals(name)){
            sql+=" and name like '%"+name+"%'";
        }
        if(createTime!=null &&!"".equals(createTime)){
            sql+=" and createtime like '%"+createTime+"%'";
        }
        if(status!=null &&!"".equals(status)){
        	if("active".equals(status)){
        		status="0";
        	}else{
        		status="1";
        	}
            sql+=" and active = "+ ConvertUtil.converterToInt(status);
        }
        if(orderByCln!=null&&!"".equals(orderByCln)&&ascOrdesc!=null&&!"".equals(ascOrdesc)){
        	if(sEcho==1){
        		sql+= " order by createtime desc";
        	}else{
        		sql+= " order by " +index2Name.get(ConvertUtil.converterToInt(orderByCln))+" "+ascOrdesc;
        	}
        }
        if(pageSize!=-1){
        	sql+=" limit "+start+","+(start+pageSize);
        }
        List<ProjectBean1> list = new ArrayList<ProjectBean1>();
        try {
            list = projectDao.projectList(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int projectListCount(int userid, String keyWord, String name, String createTime, String status) {
        String sql = "select count(*) from track_project where 1=1 and del=0 and userid = "+userid;
        String active = "";
        if(keyWord!=null &&!"".equals(keyWord)){
        	if("active".equals(keyWord)){
        		active = "0";
        	}else if("inactive".equals(keyWord)){
        		active = "1";
        	}
            sql+=" and (name like '%"+keyWord+"%' or description like '%"+keyWord+"%' or active like '%"+(active==""?keyWord:active)+"%' or createtime like '%"+keyWord+"%')";
        }
        if(name!=null &&!"".equals(name)){
            sql+=" and name like '%"+name+"%'";
        }
        if(createTime!=null &&!"".equals(createTime)){
            sql+=" and createtime like '%"+createTime+"%'";
        }
        if(status!=null &&!"".equals(status)){
        	if("active".equals(status)){
        		status="0";
        	}else{
        		status="1";
        	}
            sql+=" and active = "+ ConvertUtil.converterToInt(status);
        }
        int count = 0;
        try {
            count = projectDao.projectListCount(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<ProjectBean1> queryAllProjects(int userid) {
        List<ProjectBean1> allProjects = new ArrayList<ProjectBean1>();
        String sql = "select id, name, description, active, createtime, updatetime, userId, del from track_project where userid="+userid+" and del = 0 and active = 0";
        try {
            allProjects = projectDao.projectList(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allProjects;
    }
}
