package com.vuclip.abtesthttp.dao;

import com.vuclip.abtesthttp.bean.ConditionBean;
import com.vuclip.abtesthttp.bean.TargetConBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public interface TargetConDao {
    public void addTargetCon(TargetConBean targetConBean) throws Exception;

    public List<TargetConBean> getConByTargetId(int targetId) throws Exception;

    public  int deleteCon(int targetid) throws Exception;
}
