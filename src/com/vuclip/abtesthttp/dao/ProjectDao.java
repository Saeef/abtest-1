package com.vuclip.abtesthttp.dao;

import java.util.List;
import java.util.Map;

import com.vuclip.abtesthttp.bean.ProjectBean;
import com.vuclip.abtesthttp.bean.ProjectBean1;
import com.vuclip.abtesthttp.util.Pagination;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectDao {
	
    public int addProject(ProjectBean project) throws Exception;
    
    public int updateProject(ProjectBean bean) throws Exception;
    
    public int delProject(int id) throws Exception;
    
    public int updateProjectStatus(int id, int status) throws Exception;
	
	public Pagination<ProjectBean> queryProject(Integer currentPage, Integer numPerPage, Map<String, String> params);

    public int checkNameIsExist(String uname) throws Exception;

    public List<ProjectBean1> projectList(String sql) throws Exception;

    public int projectListCount(String sql) throws Exception;
}
