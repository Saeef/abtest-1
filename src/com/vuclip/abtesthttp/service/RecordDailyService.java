package com.vuclip.abtesthttp.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.abtesthttp.dao.RecordDailyDao;
import com.vuclip.abtesthttp.util.Pagination;

@Transactional
@Service
public class RecordDailyService {
	
	@Resource
    private  RecordDailyDao  recordDailyDao;
	
    
    public Pagination queryRecordDaily(Integer startIndex, Integer pageSize){
    	return recordDailyDao.queryRecordDaily(startIndex, pageSize);
    }
    
   
}
