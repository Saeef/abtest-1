package com.vuclip.abtesthttp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vuclip.abtesthttp.bean.ConditionBean;
import com.vuclip.abtesthttp.bean.TargetConBean;
import com.vuclip.abtesthttp.dao.FactorDao;
import com.vuclip.abtesthttp.dao.TargetConDao;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.abtesthttp.bean.TargetBean;
import com.vuclip.abtesthttp.dao.TargetDao;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.Pagination;


@Transactional
@Service
public class TargetService {
	private Logger logger = Logger.getLogger(this.getClass());
	private static Map<Integer,String> index2Name = new HashMap<Integer,String>();
	@Resource
	private TargetConDao targetConDao;
	@Resource
	private FactorDao factorDao;
	
	static{
		index2Name.put(0, "name");
		index2Name.put(1, "description");
		index2Name.put(2, "type");
		index2Name.put(3, "active");
		index2Name.put(4, "createtime");
	}
	
	@Resource
    private TargetDao targetDao;
	
    public int addTarget(TargetBean bean,List<TargetConBean> targetConList) throws Exception{
		int count = 0;
		Long newTargetId = targetDao.addTarget(bean);
		if(newTargetId>0&&targetConList.size()>0){
			for(TargetConBean targetConBean:targetConList){
				targetConBean.setTarget_id(newTargetId);
				targetConDao.addTargetCon(targetConBean);
			}
		}
		count =1;
    	return count;
	}
    
    public int updateTarget(TargetBean bean,List<TargetConBean> list) throws Exception{
		int success = 0;
		//delete condition of this target before update target.
		targetConDao.deleteCon(bean.getId());
		//update target.
		targetDao.updateTarget(bean);
		//if hava condition then add target condition.
		if(list.size()>0){
			for(TargetConBean targetConBean:list){
				targetConBean.setTarget_id(Long.parseLong(String.valueOf(bean.getId())));
				targetConDao.addTargetCon(targetConBean);
			}
		}
		success=1;
		return success;
    }
    
    public int delTarget(int id) throws Exception{
		int success = 0;
		targetDao.delTarget(id);
		factorDao.delFactorByTargetId(id);
		success =1;
    	return success;
    }
    
    public int updateTargetStatus(int id, int status){
    	return targetDao.updateTargetStatus(id, status);
    }
    
    public Pagination<TargetBean> queryTarget(Integer currentPage, Integer pageSize, Map<String,String> params){
    	return targetDao.queryTarget(currentPage, pageSize, params);
    }

    public List<Map<String, Object>> getTargetByTypeAndProid(int type,int proid) {
        List<Map<String, Object>> list = null;
        try {
            list = targetDao.getTargetByTypeAndProid(type, proid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int checkNameIsExist(String uname) {
        int count = 0;
        try {
            count = targetDao.checkNameIsExist(uname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

	public List<TargetBean> targetList(Map<String,Object> params) {
		StringBuffer sql = new StringBuffer("SELECT  id,project_id,name,description,case active WHEN 1 THEN 'inactive' WHEN 0 THEN 'active' end  as active,createtime,updatetime,case type WHEN 0 THEN 'Tracking' WHEN 1 THEN 'AB-Test' end  as type,country,pageId FROM track_target where 1=1 and del =0 ");
        String active = "";
        if(params != null && params.size() > 0){
	        if(params.get("project_id")!=null&&!"".equals(params.get("project_id"))){
	        	sql.append("and project_id="+params.get("project_id"));
	        }
	        if(params.get("keyWord")!=null &&!"".equals(params.get("keyWord"))){
	        	String keyWord = params.get("keyWord").toString();
	        	if("active".equals(keyWord)){
	        		active = "0";
	        	}else if("inactive".equals(keyWord)){
	        		active = "1";
	        	}
	        	sql.append(" and (name like '%"+keyWord+"%' or description like '%"+keyWord+"%' or active like '%"+(active==""?keyWord:active)+"%' or createtime like '%"+keyWord+"%')");
	        }
	        if(params.get("createTime")!=null &&!"".equals(params.get("createTime"))){
	            sql.append(" and createtime like '%"+params.get("createTime")+"%'");
	        }
	        Object type = params.get("type");
	        if(type!=null &&!"".equals(type)){
	        	if("active".equals(params.get("status"))){
	        		type="0";
	        	}else{
	        		type="1";
	        	}
	        	sql.append(" and type = "+ ConvertUtil.converterToInt(type.toString()));
	        }
	        Object status = params.get("status");
	        if(status!=null &&!"".equals(status)){
	        	if("active".equals(params.get("status"))){
	        		status="0";
	        	}else{
	        		status="1";
	        	}
	        	sql.append(" and active = "+ ConvertUtil.converterToInt(status.toString()));
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
        List<TargetBean> list = new ArrayList<TargetBean>();
        try {
            list = targetDao.targetList(sql);
			for(TargetBean targetBean:list){
				List<TargetConBean> conditionBeans = targetConDao.getConByTargetId(targetBean.getId());
				targetBean.setTargetConBeanList(conditionBeans);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
	}

	public int targetListCount(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer("SELECT  count(*) FROM track_target where 1=1 and del=0 ");
        String active = "";
        if(params != null && params.size() > 0){
	        if(params.get("project_id")!=null&&!"".equals(params.get("project_id"))){
	        	sql.append(" and project_id="+params.get("project_id"));
	        }
	        if(params.get("keyWord")!=null &&!"".equals(params.get("keyWord"))){
	        	String keyWord = params.get("keyWord").toString();
	        	if("active".equals(keyWord)){
	        		active = "0";
	        	}else if("inactive".equals(keyWord)){
	        		active = "1";
	        	}
	        	sql.append(" and (name like '%"+keyWord+"%' or description like '%"+keyWord+"%' or active like '%"+(active==""?keyWord:active)+"%' or createtime like '%"+keyWord+"%')");
	        }
	        if(params.get("createTime")!=null &&!"".equals(params.get("createTime"))){
	            sql.append(" and createtime like '%"+params.get("createTime")+"%'");
	        }
	        Object type = params.get("type");
	        if(type!=null &&!"".equals(type)){
	        	if("active".equals(params.get("status"))){
	        		type="0";
	        	}else{
	        		type="1";
	        	}
	        	sql.append(" and type = "+ ConvertUtil.converterToInt(type.toString()));
	        }
	        Object status = params.get("status");
	        if(status!=null &&!"".equals(status)){
	        	if("active".equals(params.get("status"))){
	        		status="0";
	        	}else{
	        		status="1";
	        	}
	        	sql.append(" and active = "+ ConvertUtil.converterToInt(status.toString()));
	        }
        }
        int count = 0;
        try {
        	count = targetDao.targetListCount(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
	}
   
}
