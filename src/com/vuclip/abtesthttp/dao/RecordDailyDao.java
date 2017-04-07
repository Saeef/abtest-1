package com.vuclip.abtesthttp.dao;

import com.vuclip.abtesthttp.util.Pagination;

public interface RecordDailyDao {
	
	public Pagination queryRecordDaily(Integer currentPage, Integer numPerPage);

}