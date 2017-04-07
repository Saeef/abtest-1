package com.vuclip.abtesthttp.service;

import com.vuclip.abtesthttp.bean.ConditionBean;
import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.dao.TargetConDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TargetConService {
    private Logger logger = Logger.getLogger(TargetConService.class);
    @Resource
    private TargetConDao targetConDao;
    public void addTargetCon(TargetConBean targetConBean){
        try {
            targetConDao.addTargetCon(targetConBean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("add target condition faild target_id:"+targetConBean.getTarget_id()+e.getMessage());
        }
    }

    public List<TargetConBean> getConByTargetId(int targetId) {
        List<TargetConBean> conditions = new ArrayList<TargetConBean>();
        try {
            conditions = targetConDao.getConByTargetId(targetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conditions;
    }
}
