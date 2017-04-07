package com.vuclip.abtesthttp.service;

import com.vuclip.abtesthttp.dao.ConditionDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ConditionService {
    @Resource
    private ConditionDao conditionDao;
    public List<Map<String,Object>> getConditions(){
        List<Map<String,Object>> conditions = new ArrayList<Map<String, Object>>();
        try {
            conditions = conditionDao.getConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conditions;
    }

    public List<Map<String,Object>> getRateConditions() {
        List<Map<String,Object>> rateconditions = new ArrayList<Map<String, Object>>();
        try {
            rateconditions = conditionDao.getRateConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rateconditions;
    }

    public List<Map<String,Object>> getCommonConditions() {
        List<Map<String,Object>> commonconditions = new ArrayList<Map<String, Object>>();
        try {
            commonconditions = conditionDao.getCommonConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonconditions;
    }

    public List<Map<String,Object>> getRateHidConditions() {
        List<Map<String,Object>> ratehidconditions = new ArrayList<Map<String, Object>>();
        try {
            ratehidconditions = conditionDao.getRateHidConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratehidconditions;
    }

    public List<Map<String,Object>> getConditionValue(String conditionName) {
        List<Map<String,Object>> conditionValues = new ArrayList<Map<String, Object>>();
        try {
            conditionValues = conditionDao.getConditionValue(conditionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conditionValues;
    }

    public List<Map<String, Object>> getPagePaths(int pid) {
        List<Map<String,Object>> conditionValues = new ArrayList<Map<String, Object>>();
        try {
            conditionValues = conditionDao.getPagePaths(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conditionValues;
    }

    public List<Map<String, Object>> getGroupBys(String groupBy) {
        List<Map<String,Object>> groupBys = new ArrayList<Map<String, Object>>();
        try {
            groupBys = conditionDao.getGroupBys(groupBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupBys;
    }
}
