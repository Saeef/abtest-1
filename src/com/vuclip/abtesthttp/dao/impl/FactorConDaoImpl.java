package com.vuclip.abtesthttp.dao.impl;

import com.vuclip.abtesthttp.bean.ConditionValueBean;
import com.vuclip.abtesthttp.bean.FactorConBean;
import com.vuclip.abtesthttp.dao.FactorConDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
@Component
public class FactorConDaoImpl implements FactorConDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public void addFactorCon(FactorConBean factorConBean) throws Exception {
        String sql = "insert into track_factor_condition(factor_id,condition_value_id,createtime,updatetime) values(?,?,?,?)";
        jdbcTemplate.update(sql,factorConBean.getFactor_id(),factorConBean.getCondition_value_id(),factorConBean.getCreatetime(),factorConBean.getUpdatetime());
    }

    @Override
    public List<Map<String ,Object>> getConValueByFactorId(int factorId) throws Exception {
        String sql = "select a.id,c.name,c.type from track_condition_value a,track_factor_condition b,track_condition c where a.id=b.condition_value_id and a.condition_id = c.id and b.factor_id="+factorId;
        List<Map<String ,Object>> conList = jdbcTemplate.queryForList(sql);
        return conList;
    }

    @Override
    public int deleteCon(int factorid) throws Exception {
        String sql = "delete from track_factor_condition where factor_id=?";
        return jdbcTemplate.update(sql,factorid);
    }
}
