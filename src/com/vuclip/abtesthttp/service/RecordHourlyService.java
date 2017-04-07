package com.vuclip.abtesthttp.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.abtesthttp.dao.RecordHourlyDao;
import com.vuclip.abtesthttp.util.Pagination;


@Transactional
@Service
public class RecordHourlyService {
	
	@Resource
    private RecordHourlyDao recordHourlyDao;
    
    public Pagination queryProject(Integer startIndex, Integer pageSize){
    	return recordHourlyDao.queryRecordHourly(startIndex, pageSize);
    }
    
   
}
