package com.vuclip.abtesthttp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vuclip.abtesthttp.bean.FactorConBean;
import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.dao.FactorConDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.abtesthttp.bean.FactorBean;
import com.vuclip.abtesthttp.dao.FactorDao;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.Pagination;

@Transactional
@Service
public class FactorService {
	private Logger logger = Logger.getLogger(this.getClass());
	private static Map<Integer,String> index2Name = new HashMap<Integer,String>();
	
	static{
		index2Name.put(0, "name");
		index2Name.put(1, "description");
		index2Name.put(2, "createtime");
	}
	
	@Resource
    private FactorDao factorDao;

	@Resource
	private FactorConDao factorConDao;
	
    public int addFactor(FactorBean bean,List<FactorConBean> list) throws Exception{
		int count = 0;
		Long newTargetId = factorDao.addFactor(bean);
		if(newTargetId>0&&list.size()>0){
			for(FactorConBean factorConBean:list){
				factorConBean.setFactor_id(newTargetId);
				factorConDao.addFactorCon(factorConBean);
			}
		}
		count =1;
		return count;
	}
    
    public int updateFactor(FactorBean bean,List<FactorConBean> list) throws Exception{
		int success = 0;
		factorConDao.deleteCon(bean.getId());
		//update target.
		factorDao.updateFactor(bean);
		//if hava condition then add factor condition.
		if(list.size()>0){
			for(FactorConBean factorConBean:list){
				factorConBean.setFactor_id(Long.parseLong(String.valueOf(bean.getId())));
				factorConDao.addFactorCon(factorConBean);
			}
		}
		success=1;
		return success;
    }
    
    public int delFactor(int factorId) throws Exception{
    	return factorDao.delFactor(factorId);
    }
    
    public Pagination<FactorBean> queryFactor(Integer currentPage, Integer pageSize, Map<String, String> params) throws Exception{
    	return factorDao.queryFactor(currentPage, pageSize, params);
    }
    public List<Map<String, Object>> getFactorByTargetId(int targetid) throws Exception{
        return factorDao.getFactorByTargetId(targetid);
    }
    public int checkNameIsExist(String uname) {
        int count = 0;
        try {
            count = factorDao.checkNameIsExist(uname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

	public List<FactorBean> factorList(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer("SELECT  id,target_id,name,description,createtime,updatetime FROM track_factor where 1=1 and del=0 ");
        if(params != null && params.size() > 0){
	        if(params.get("target_id")!=null&&!"".equals(params.get("target_id"))){
	        	sql.append("and target_id="+params.get("target_id"));
	        }
	        if(params.get("keyWord")!=null &&!"".equals(params.get("keyWord"))){
	        	String keyWord = params.get("keyWord").toString();
	        	sql.append(" and (name like '%"+keyWord+"%' or description like '%"+keyWord+"%' or createtime like '%"+keyWord+"%')");
	        }
	        if(params.get("createTime")!=null &&!"".equals(params.get("createTime"))){
	            sql.append(" and createtime like '%"+params.get("createTime")+"%'");
	        }
	        if(params.get("orderByCln")!=null&&!"".equals(params.get("orderByCln"))&&params.get("ascOrdesc")!=null&&!"".equals(params.get("ascOrdesc"))){
	        	if(ConvertUtil.converterToInt(params.get("sEcho")==null?"":params.get("sEcho").toString())==1){
	        		sql.append(" order by createtime desc");
	        	}else{
	        		sql.append(" order by " +index2Name.get(ConvertUtil.converterToInt(params.get("orderByCln").toString()))+" "+params.get("ascOrdesc"));
	        	}
	        }
	        Object pageSize = params.get("pageSize");
	        if(ConvertUtil.converterToInt(pageSize.toString())!=-1){
	        	sql.append(" limit "+params.get("start")+","+(Integer.parseInt(params.get("start").toString())+Integer.parseInt(pageSize.toString())));
	        }
        }
        List<FactorBean> list = new ArrayList<FactorBean>();
        try {
            list = factorDao.factorList(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
	}

	public int factorListCount(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer("SELECT  count(*) FROM track_factor where 1=1 and del=0 ");
		if(params != null && params.size() > 0){
	        if(params.get("target_id")!=null&&!"".equals(params.get("target_id"))){
	        	sql.append("and target_id="+params.get("target_id"));
	        }
	        if(params.get("keyWord")!=null &&!"".equals(params.get("keyWord"))){
	        	String keyWord = params.get("keyWord").toString();
	        	sql.append(" and (name like '%"+keyWord+"%' or description like '%"+keyWord+"%' or createtime like '%"+keyWord+"%')");
	        }
	        if(params.get("createTime")!=null &&!"".equals(params.get("createTime"))){
	            sql.append(" and createtime like '%"+params.get("createTime")+"%'");
	        }
        }
		int count = 0;
		try {
			count = factorDao.factorListCount(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
   
}
