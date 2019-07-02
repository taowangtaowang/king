package com.irs.service;
import java.util.Date;

import com.irs.pojo.TbLog;
import com.irs.pojo.UserSearch;
import com.irs.util.ResultUtil;

public interface LogService {
	//添加日志
	public void insLog(TbLog log);
	
	//获取日志列表
	ResultUtil selLogList(Integer page, Integer limit, UserSearch search);

	//删除指定日期以前的日志
	public int delLogsByDate(Date date);
}
