package com.vuclip.abtesthttp.bean;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/24
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class ConditionValueBean {
    private int id;
    private int condition_id;
    private String name;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCondition_id() {
        return condition_id;
    }

    public void setCondition_id(int condition_id) {
        this.condition_id = condition_id;
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
}
