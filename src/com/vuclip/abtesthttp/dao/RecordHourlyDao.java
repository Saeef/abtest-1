package com.vuclip.abtesthttp.dao;

import com.vuclip.abtesthttp.util.Pagination;


public interface RecordHourlyDao {
	
	public Pagination queryRecordHourly(Integer currentPage, Integer numPerPage);

}