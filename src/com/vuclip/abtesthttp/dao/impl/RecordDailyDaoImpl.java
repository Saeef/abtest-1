package com.vuclip.abtesthttp.dao.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.vuclip.abtesthttp.dao.RecordDailyDao;
import com.vuclip.abtesthttp.util.Pagination;

@Component
public class RecordDailyDaoImpl implements RecordDailyDao{
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
/*	@Override
	public int addProject(ProjectBean project) {
		String sql = "INSERT INTO track_project(name,description,active,createtime) values(?,?,?)";
		int insert = jdbcTemplate.update(sql,project.getName(),project.getDescription(),project.getActive(),project,DateUtil.dateFormat(new Date()));
        return insert;
	}*/
	
	@Override
	public Pagination queryRecordDaily(Integer currentPage, Integer numPerPage) {
		String sql = "SELECT * from track_record_daily";
		return new Pagination(sql, currentPage, numPerPage, jdbcTemplate);
	}

}
