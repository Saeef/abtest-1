package com.vuclip.abtesthttp.bean;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/24
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
public class FactorConBean {
    private int id;
    private Long factor_id;
    private int condition_value_id;
    private String createtime;
    private String updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getFactor_id() {
        return factor_id;
    }

    public void setFactor_id(Long factor_id) {
        this.factor_id = factor_id;
    }

    public int getCondition_value_id() {
        return condition_value_id;
    }

    public void setCondition_value_id(int condition_value_id) {
        this.condition_value_id = condition_value_id;
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
