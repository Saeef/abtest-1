package com.vuclip.abtesthttp.service;

import com.vuclip.abtesthttp.bean.UserBean;
import com.vuclip.abtesthttp.dao.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Service
public class UserService {
    @Resource
    private UserDao userDao;
    public int userAdd(UserBean userDomain) throws Exception{
        String sql = "insert into track_user(username,password,insertDate) values(?,?,?)";
        int insert = userDao.userAdd(sql,userDomain);
        return insert;
    }
    public UserBean login(String uname){
        String sql = "select username,password,userid from track_user where username = ?";
        UserBean userDomain;
        try {
            userDomain = userDao.getUserByUname(sql, uname);
        } catch (Exception e) {
            userDomain = null;
        }
        return userDomain;
    }

    public int updateUser(UserBean user) throws Exception{
        String sql = "update track_user set username=?,password=? where userid = ?";
        int update = userDao.updateUser(sql,user);
        return update;
    }
    public UserBean getUserByUserId(int userId){
        String sql = "select username,password,userid from track_user where userid = ?";
        UserBean userDomain;
        try {
            userDomain = userDao.getUserByUserId(sql, userId);
        } catch (Exception e) {
            userDomain = null;
        }
        return userDomain;
    }
}
