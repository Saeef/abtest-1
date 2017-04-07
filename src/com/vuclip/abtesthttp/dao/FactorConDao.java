package com.vuclip.abtesthttp.dao;

import com.vuclip.abtesthttp.bean.FactorConBean;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/7/22
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public interface FactorConDao {
    public void addFactorCon(FactorConBean factorConBean) throws Exception;

    public  int deleteCon(int factorId) throws Exception;

    public List<Map<String ,Object>> getConValueByFactorId(int factorId) throws Exception;
}
