package com.vuclip.abtesthttp.dao;

import com.vuclip.abtesthttp.bean.UserBean;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public interface UserDao {
    public int userAdd(String sql,UserBean user) throws Exception;

    public UserBean getUserByUname(String sql, String uname) throws Exception;

    public int updateUser(String sql, UserBean user) throws Exception;

    public UserBean getUserByUserId(String sql, int userId) throws Exception;
}
