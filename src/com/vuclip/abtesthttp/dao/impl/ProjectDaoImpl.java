package com.vuclip.abtesthttp.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.vuclip.abtesthttp.bean.ProjectBean;
import com.vuclip.abtesthttp.bean.ProjectBean1;
import com.vuclip.abtesthttp.dao.ProjectDao;
import com.vuclip.abtesthttp.redis.RedisFactory;
import com.vuclip.abtesthttp.util.DateUtil;
import com.vuclip.abtesthttp.util.Pagination;

@Component
public class ProjectDaoImpl implements ProjectDao{
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int addProject(ProjectBean project) throws Exception{
		String sql = "INSERT INTO track_project(userId,name,description,active,createtime) VALUES('"+project.getUserId()+"','"+project.getName()+"','"+project.getDescription()+"','"+project.getActive()+"','"+DateUtil.dateFormat(new Date())+"')";
        return jdbcTemplate.update(sql);
	}
	
	@Override
	public int updateProject(ProjectBean bean) throws Exception{
		String sql = "UPDATE track_project SET active="+bean.getActive()+",name='"+bean.getName()+"',description='"+bean.getDescription()+"',updatetime='"+DateUtil.dateFormat(new Date())+"'  WHERE id="+bean.getId();
		return jdbcTemplate.update(sql);
	}
	
	@Override
	public int delProject(int id) throws Exception{
		String sql = "UPDATE track_project set del =1 WHERE id="+id;
		return jdbcTemplate.update(sql);
	}
	
	@Override
	public int updateProjectStatus(int id, int status) throws Exception{
		status = status==0?1:0;
		String sql = "UPDATE track_project SET active="+status+" WHERE id="+id;
		return jdbcTemplate.update(sql);
	}
	
	@Override
	public Pagination<ProjectBean> queryProject(Integer currentPage, Integer numPerPage, Map<String, String> params) {
		StringBuffer sql = new StringBuffer("SELECT * from track_project WHERE 1=1 and del=0 and active = 0 ");
		if(params != null && params.size() > 0){
			if(params.get("name") != null){
				sql.append(" AND name LIKE '%"+params.get("name")+"%'");
			}
			if(params.get("userid") != null){
				sql.append(" AND userId ="+params.get("userid"));
			}
		}
		sql.append(" ORDER BY createtime DESC");
		Pagination<ProjectBean> pager = new Pagination<ProjectBean>(sql.toString(), currentPage, numPerPage, jdbcTemplate);
		
		List<ProjectBean>  projectList = jdbcTemplate.query(pager.getMySQLPageSQL(sql.toString(), pager.getStartIndex(), pager.getNumPerPage()),
                new RowMapper<ProjectBean>() {  
                    @Override  
                    public ProjectBean mapRow(ResultSet rs, int rowNum) throws SQLException {  
                    	ProjectBean bean = new ProjectBean();
                    	bean.setId(rs.getInt("id"));
                    	bean.setName(rs.getString("name"));
                    	bean.setDescription(rs.getString("description"));
                    	bean.setActive(rs.getInt("active"));
                    	bean.setCreatetime(rs.getDate("createtime"));
                		return bean;
                    }  
                });
		
		pager.setData(projectList);
		
		return pager;
	}

	@Override
	public int checkNameIsExist(String uname) throws Exception {
		Jedis jedis = RedisFactory.getJedis();
		Boolean isExists = jedis.exists("PROJECT_" + uname);
		int count = 0;
		if(!isExists){
			String sql = "select count(*) from track_project where name = ?";
			count = jdbcTemplate.queryForInt(sql,uname);
		}else{
			count = 1;
		}
		jedis.close();
		return count;
	}

	@Override
	public List<ProjectBean1> projectList(String sql) throws Exception {
		return jdbcTemplate.query(sql,new RowMapper<ProjectBean1>() {
			@Override
			public ProjectBean1 mapRow(ResultSet rs, int rowNum) throws SQLException {
				String createtime = rs.getTimestamp("createtime").toString();
				ProjectBean1 bean = new ProjectBean1();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setDescription(rs.getString("description"));
				bean.setActive(rs.getString("active"));
				bean.setCreatetime(createtime.split("\\.")[0]);
				return bean;
			}
		});
	}

	@Override
	public int projectListCount(String sql) throws Exception {
		return jdbcTemplate.queryForInt(sql);
	}
}
