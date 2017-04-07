package com.vuclip.abtesthttp.dao.impl;

import com.vuclip.abtesthttp.dao.ReportDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/28
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReportDaoImpl implements ReportDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> abtestHPane(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> abtestDPane(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> abtestDayToDay(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> trackHPane(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> trackDPane(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> trackDayToDay(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

	@Override
	public List<Map<String, Object>> getTargetCountry(String sql) throws Exception {
		return jdbcTemplate.queryForList(sql);
	}
    
}
