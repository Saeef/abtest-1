package com.vuclip.abtesthttp.dao.impl;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vuclip.abtesthttp.bean.FactorConBean;
import com.vuclip.abtesthttp.redis.RedisFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.vuclip.abtesthttp.bean.FactorBean;
import com.vuclip.abtesthttp.dao.FactorDao;
import com.vuclip.abtesthttp.util.DateUtil;
import com.vuclip.abtesthttp.util.Pagination;

import redis.clients.jedis.Jedis;

@Component
public class FactorDaoImpl implements FactorDao{
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Long addFactor(final FactorBean bean) {
		final String sql = "INSERT INTO track_factor(target_id,name,description,createtime) VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, bean.getTargetId());
				ps.setString(2, bean.getName());
				ps.setString(3, bean.getDescription());
				ps.setString(4,DateUtil.dateFormat(new Date()));
				return ps;
			}
		},keyHolder);
		Long generatedId = keyHolder.getKey().longValue();
		return generatedId;
	}

	@Override
	public int updateFactor(FactorBean bean) {
		String sql = "UPDATE track_factor SET name='"+bean.getName()+"',description='"+bean.getDescription()+"',updatetime='"+DateUtil.dateFormat(new Date())+"'  WHERE id="+bean.getId();
		return jdbcTemplate.update(sql);
	}
	
	@Override
	public int delFactor(int id) {
		String sql = "UPDATE  track_factor set del =1 WHERE id="+id;
		return jdbcTemplate.update(sql);
	}
	
	@Override
	public Pagination<FactorBean> queryFactor(Integer currentPage, Integer numPerPage, Map<String, String> params) {
		StringBuffer sql = new StringBuffer("SELECT * FROM track_factor WHERE 1=1 and del=0 ");
		if(params != null && params.size() > 0){
			if(params.get("target_id") != null){
				sql.append(" AND target_id="+params.get("target_id"));
			}
			if(params.get("name") != null){
				sql.append(" AND name LIKE '%"+params.get("name")+"%'");
			}
		}
		Pagination<FactorBean> pager = new Pagination<FactorBean>(sql.toString(), currentPage, numPerPage, jdbcTemplate);
		
		List<FactorBean> targetList = jdbcTemplate.query(pager.getMySQLPageSQL(sql.toString(), pager.getStartIndex(), pager.getNumPerPage()),
                new RowMapper<FactorBean>() {  
                    @Override  
                    public FactorBean mapRow(ResultSet rs, int rowNum) throws SQLException {  
                    	String createtime = rs.getTimestamp("createtime").toString();
                    	String updatetime = rs.getTimestamp("updatetime")==null?"":rs.getTimestamp("updatetime").toString();
                    	FactorBean t = new FactorBean();
                		t.setId(rs.getInt("id"));
                		t.setTargetId(rs.getInt("target_id"));
                		t.setName(rs.getString("name"));
                		t.setDescription(rs.getString("description"));
                		t.setCreatetime(createtime.split("\\.")[0]);
                		t.setUpdatetime(updatetime==""?updatetime:updatetime.split("\\.")[0]);
                		return t;
                    }  
                }); 
		
		pager.setData(targetList);
		return pager;
	}

	@Override
	public List<Map<String, Object>> getFactorByTargetId(int targetid) {
		String sql = "select * from track_factor where  del=0 and target_id = ?";

		return jdbcTemplate.queryForList(sql,targetid);
	}

	@Override
	public int checkNameIsExist(String uname) throws Exception {
		Jedis jedis = RedisFactory.getJedis();
		Boolean isExists = jedis.exists("FACTOR_" + uname);
		int count = 0;
		if(!isExists){
			String sql = "select count(*) from track_factor where name = ?";
			count = jdbcTemplate.queryForInt(sql,uname);
		}else{
			count = 1;
		}
		jedis.close();
		return count;
	}

	@Override
	public List<FactorBean> factorList(StringBuffer sql) throws Exception {
		List<FactorBean> targetList = jdbcTemplate.query(sql.toString(),
                new RowMapper<FactorBean>() {  
                    @Override  
                    public FactorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                    	String createtime = rs.getTimestamp("createtime").toString();
                    	String updatetime = rs.getTimestamp("updatetime")==null?"":rs.getTimestamp("updatetime").toString();
                    	FactorBean t = new FactorBean();
                		t.setId(rs.getInt("id"));
                		t.setTargetId(rs.getInt("target_id"));
                		t.setName(rs.getString("name"));
                		t.setDescription(rs.getString("description"));
                		t.setCreatetime(createtime.split("\\.")[0]);
                		t.setUpdatetime(updatetime==""?updatetime:updatetime.split("\\.")[0]);
                		return t;
                    }  
                }); 
		return targetList;
	}

	@Override
	public int factorListCount(StringBuffer sql) throws Exception {
		return jdbcTemplate.queryForInt(sql.toString());
	}

	@Override
	public int delFactorByTargetId(int targetId) throws Exception {
		String sql = "UPDATE  track_factor set del =1 WHERE target_id="+targetId;
		return jdbcTemplate.update(sql);
	}

	@Override
	public int delFactorByProId(int proId) throws Exception {
		String sql = "UPDATE  track_factor set del =1 WHERE target_id in (select id from track_target where project_id="+proId+")";
		return jdbcTemplate.update(sql);
	}
}
