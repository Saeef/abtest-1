package com.vuclip.abtesthttp.service;

import com.vuclip.abtesthttp.bean.ConditionBean;
import com.vuclip.abtesthttp.bean.ConditionValueBean;
import com.vuclip.abtesthttp.bean.FactorConBean;
import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.dao.FactorConDao;
import com.vuclip.abtesthttp.dao.TargetConDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FactorConService {
    private Logger logger = Logger.getLogger(FactorConService.class);
    @Resource
    private FactorConDao factorConDao;

    public List<Map<String,Object>> getConValueByFactorId(int factorId) {
        List<Map<String,Object>> conditionValues = new ArrayList<Map<String,Object>>();
        try {
            conditionValues = factorConDao.getConValueByFactorId(factorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conditionValues;
    }
}
