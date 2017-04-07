package com.vuclip.abtesthttp.dao.impl;

import com.vuclip.abtesthttp.bean.UserBean;
import com.vuclip.abtesthttp.dao.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserDaoImpl implements UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Override
    public int userAdd(String sql, UserBean user) throws Exception {
        int insert = jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getInsertDate());
        return insert;
    }

    @Override
    public UserBean getUserByUname(String sql, String uname) throws Exception{
        return jdbcTemplate.queryForObject(sql, new Object[]{uname}, new RowMapper<UserBean>() {
            @Override
            public UserBean mapRow(ResultSet resultSet, int i) throws SQLException {
                UserBean user = null;
                if(resultSet.getMetaData().getColumnCount()>0){
                    user = new UserBean();
                    user.setUserid(resultSet.getInt("userid"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                }
                return user;
            }
        });
    }

    @Override
    public int updateUser(String sql, UserBean user) throws Exception {
        int update = jdbcTemplate.update(sql,user.getUsername(), user.getPassword(), user.getUserid());
        return update;
    }

    @Override
    public UserBean getUserByUserId(String sql, int userId) throws Exception {
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new RowMapper<UserBean>() {
            @Override
            public UserBean mapRow(ResultSet resultSet, int i) throws SQLException {
                UserBean user = null;
                if(resultSet.getMetaData().getColumnCount()>0){
                    user = new UserBean();
                    user.setUserid(resultSet.getInt("userid"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                }
                return user;
            }
        });
    }
}
