package com.irs.controller;

import com.irs.pojo.UserSearch;
import com.irs.service.LogService;
import com.irs.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("log/")
public class LogController {
	
	@Autowired
	private LogService logServiceImpl;
	
	@RequestMapping("logList")
	public String logList(){
		return "page/log/logList";
	}
	
	@RequestMapping("getLogList")
	@ResponseBody
	public ResultUtil getLogList(Integer page, Integer limit,UserSearch search){
		return logServiceImpl.selLogList(page,limit,search);
	}
}
