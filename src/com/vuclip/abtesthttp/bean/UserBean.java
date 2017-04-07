package com.vuclip.abtesthttp.bean;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class UserBean {
    private int userid;
    private String username;
    private String password;
    private String insertDate;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public String toString() {
        return "UserDomain:[userid:"+this.getUserid()+",username:"+this.getUsername()+",password:"+this.getPassword()+",insertDate:"+this.getInsertDate()+"]";
    }
}
