package com.vuclip.abtesthttp.dao.impl;

import com.vuclip.abtesthttp.dao.ConditionDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ConditionDaoImpl implements ConditionDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Map<String, Object>> getConditions() throws Exception {
        String sql ="select id,name,type,isshow,conditiontype,showcondition from track_condition where pid =0";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getGroupBys(String conName) throws Exception {
        String sql ="select a.id,a.name,a.type,a.isshow,a.conditiontype,a.showcondition,a.pid from track_condition a,(select id from track_condition where name ='"+conName+"') b where a.pid = b.id";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getCommonConditions() throws Exception {
        String sql ="select id,name,type,isshow,conditiontype from track_condition where conditiontype=0 and isshow=0 and pid = 0";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getRateConditions() throws Exception {
        String sql ="select id,name,type,isshow,conditiontype from track_condition where conditiontype=1 and isshow=0 and pid = 0";
        return jdbcTemplate.queryForList(sql);
    }



    @Override
    public List<Map<String, Object>> getRateHidConditions() throws Exception {
        String sql ="select id,name,type,isshow,conditiontype from track_condition where conditiontype=1 and isshow=1 and pid = 0";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getConditionValue(String conditionName) throws Exception {
        String sql ="select b.id id,b.name name from track_condition a,track_condition_value b where a.id = b.condition_id and a.name = ? ";
        return jdbcTemplate.queryForList(sql,conditionName);
    }

    @Override
    public List<Map<String, Object>> getPagePaths(int pid) throws Exception {
        String sql = "select id,name,description,type,isshow,conditiontype,showcondition,pid from track_condition where pid = ? ";
        return jdbcTemplate.queryForList(sql,pid);
    }
}
