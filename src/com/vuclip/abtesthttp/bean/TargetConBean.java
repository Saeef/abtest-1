package com.vuclip.abtesthttp.bean;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class TargetConBean {
    private int id;
    private String name;
    private Long target_id;
    private int condition_id;
    private String createtime;
    private String updatetime;
    private String type;
    private int pid;
    private String pname;
    private String ptype;

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getTarget_id() {
        return target_id;
    }

    public void setTarget_id(Long target_id) {
        this.target_id = target_id;
    }

    public int getCondition_id() {
        return condition_id;
    }

    public void setCondition_id(int condition_id) {
        this.condition_id = condition_id;
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

    @Override
    public String toString() {
        return "TargetConBean{" +
                "id=" + id +
                ", target_id=" + target_id +
                ", condition_id=" + condition_id +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
