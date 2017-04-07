package com.vuclip.abtesthttp.dao.impl;

import com.vuclip.abtesthttp.bean.ConditionBean;
import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.dao.TargetConDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TargetConDaoImpl implements TargetConDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public void addTargetCon(TargetConBean targetConBean) throws Exception {
        String sql = "insert into track_target_condition(target_id,condition_id,createtime,updatetime) values(?,?,?,?)";
        jdbcTemplate.update(sql,targetConBean.getTarget_id(),targetConBean.getCondition_id(),targetConBean.getCreatetime(),targetConBean.getUpdatetime());
    }

    @Override
    public List<TargetConBean> getConByTargetId(int targetId) throws Exception {
        String sql = "select c.*,d.name pname,d.type ptype from (select a.target_id,a.condition_id,b.* from track_target_condition a ,track_condition b where a.condition_id =b.id and a.target_id = "+targetId+") c left join track_condition d on c.pid = d.id";
        List<TargetConBean> conList = jdbcTemplate.query(sql,
                new RowMapper<TargetConBean>() {
                    @Override
                    public TargetConBean mapRow(ResultSet rs, int rowNum) throws SQLException {
                        TargetConBean t = new TargetConBean();
                        t.setId(rs.getInt("id"));
                        t.setName(rs.getString("name"));
                        t.setPname(rs.getString("pname"));
                        t.setPtype(rs.getString("ptype"));
                        t.setTarget_id(Long.parseLong(String.valueOf(rs.getInt("target_id"))));
                        t.setCondition_id(rs.getInt("condition_id"));
                        t.setType(rs.getString("type"));
                        t.setPid(rs.getInt("pid"));
                        return t;
                    }
                });
        return conList;
    }

    @Override
    public int deleteCon(int targetid) throws Exception {
        String sql = "delete from track_target_condition where target_id=?";
        return jdbcTemplate.update(sql,targetid);
    }
}
