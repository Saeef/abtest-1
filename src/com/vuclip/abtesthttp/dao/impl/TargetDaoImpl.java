package com.vuclip.abtesthttp.dao.impl;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vuclip.abtesthttp.redis.RedisFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.vuclip.abtesthttp.bean.TargetBean;
import com.vuclip.abtesthttp.dao.TargetDao;
import com.vuclip.abtesthttp.util.DateUtil;
import com.vuclip.abtesthttp.util.Pagination;

import redis.clients.jedis.Jedis;

@Component
public class TargetDaoImpl implements TargetDao{

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Long addTarget(final TargetBean bean) throws Exception{
        final String sql = "INSERT INTO track_target(project_id ,name,description,type,active,country,pageId,createtime) VALUES(?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, bean.getProjectId());
				ps.setString(2, bean.getName());
				ps.setString(3, bean.getDescription());
				ps.setString(4, bean.getType());
				ps.setString(5, bean.getActive());
				ps.setInt(6, bean.getCountryControl());
				ps.setInt(7, bean.getPageControl());
				ps.setString(8,DateUtil.dateFormat(new Date()));
				return ps;
			}
		},keyHolder);
		Long generatedId = keyHolder.getKey().longValue();
		return generatedId;
        
	}
	
	public int updateTarget(TargetBean bean) throws Exception{
		String sql = "UPDATE track_target SET type="+bean.getType()+",active="+bean.getActive()+",country="+bean.getCountryControl()+",pageId="+bean.getPageControl()+",name='"+bean.getName()+"',description='"+bean.getDescription()+"',updatetime='"+DateUtil.dateFormat(new Date())+"'  WHERE id="+bean.getId();
		return jdbcTemplate.update(sql);
    }
    
    public int delTarget(int id){
    	String sql = "update track_target set del = 1 WHERE id="+id;
		return jdbcTemplate.update(sql);
    }
    
    public int updateTargetStatus(int id, int status){
    	status = status==0?1:0;
		String sql = "UPDATE track_target SET active="+status+" WHERE id="+id;
		return jdbcTemplate.update(sql);
    }
	
	@Override
	public Pagination<TargetBean> queryTarget(Integer currentPage, Integer numPerPage, Map<String,String> params) {
		StringBuffer sql = new StringBuffer("SELECT * FROM track_target WHERE 1=1 and del=0 ");
		if(params != null && params.size() > 0){
			
			if(params.get("project_id") != null){
				sql.append(" AND project_id="+params.get("project_id"));
			}
			if(params.get("name") != null){
				sql.append(" AND name LIKE '%"+params.get("name")+"%'");
			}
		}
		Pagination<TargetBean> pager = new Pagination<TargetBean>(sql.toString(), currentPage, numPerPage, jdbcTemplate);
		
		List<TargetBean> targetList = jdbcTemplate.query(pager.getMySQLPageSQL(sql.toString(), pager.getStartIndex(), pager.getNumPerPage()),
                new RowMapper<TargetBean>() {  
                    @Override  
                    public TargetBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                    	String createtime = rs.getTimestamp("createtime").toString();
                    	String updatetime = rs.getTimestamp("updatetime")==null?"":rs.getTimestamp("updatetime").toString();
                    	TargetBean t = new TargetBean();
                		t.setId(rs.getInt("id"));
                		t.setProjectId(rs.getInt("project_id"));
                		t.setName(rs.getString("name"));
                		t.setDescription(rs.getString("description"));
                		t.setType(rs.getInt("type")==0?"tracking":"AB-Test");
                		t.setActive(rs.getInt("active")==0?"active":"inactive");
                		t.setCreatetime(createtime.split("\\.")[0]);
                		t.setUpdatetime(updatetime.split("\\.")[0]);
                		
                		t.setCountryControl(rs.getInt("country"));
                		t.setPageControl(rs.getInt("pageId"));
                		
                		
                		return t;
                    }  
                }); 
		
		pager.setData(targetList);
		return pager;
	}
	
	@Override
	public List<TargetBean> targetList(StringBuffer sql) throws Exception {
		List<TargetBean> targetList = jdbcTemplate.query(sql.toString(),
                new RowMapper<TargetBean>() {  
                    @Override  
                    public TargetBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                    	String createtime = rs.getTimestamp("createtime").toString();
                    	String updatetime = rs.getTimestamp("updatetime")==null?"":rs.getTimestamp("updatetime").toString();
                    	TargetBean t = new TargetBean();
                		t.setId(rs.getInt("id"));
                		t.setProjectId(rs.getInt("project_id"));
                		t.setName(rs.getString("name"));
                		t.setDescription(rs.getString("description"));
                		t.setType(rs.getString("type"));
                		t.setActive(rs.getString("active"));
                		t.setCreatetime(createtime.split("\\.")[0]);
                		t.setUpdatetime(updatetime==""?updatetime:updatetime.split("\\.")[0]);
                		t.setCountryControl(rs.getInt("country"));
                		t.setPageControl(rs.getInt("pageId"));
                		return t;
                    }  
                }); 
		return targetList;
	}

	@Override
	public List<Map<String, Object>> getTargetByTypeAndProid(int type, int proid) throws Exception {
		String sql = "select id,name,country,pageId from track_target where del =0 and type = ? and project_id = ?";
		return jdbcTemplate.queryForList(sql,new Object[]{type,proid});
	}
	@Override
	public int checkNameIsExist(String uname) throws Exception {
		Jedis jedis = RedisFactory.getJedis();
		Boolean isExists = jedis.exists("TARGET_" + uname);
		int count = 0;
		if(!isExists){
			String sql = "select count(*) from track_target where name = ?";
			count = jdbcTemplate.queryForInt(sql,uname);
		}else{
			count = 1;
		}
		jedis.close();
		return count;
	}

	@Override
	public int targetListCount(StringBuffer sql) throws Exception {
		return jdbcTemplate.queryForInt(sql.toString());
	}

	@Override
	public int delTargetByProId(int proId) throws Exception {
		String sql = "update track_target set del = 1 WHERE project_id="+proId;
		return jdbcTemplate.update(sql);
	}
}
