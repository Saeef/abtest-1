package com.vuclip.abtesthttp.bean;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/26
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class ProjectDomain {
    private int id;
    private String name;
    private String description;
    private int active;
    private String createtime;
    private String updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
}
