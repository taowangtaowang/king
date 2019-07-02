package com.irs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.irs.pojo.TbUsers;
import com.irs.service.MainService;

@Controller
@RequestMapping("main/")
public class MainController {
	
	@Autowired
	private MainService mainServiceImpl;
	
	@RequestMapping("getUserTotal")
	@ResponseBody
	public List<TbUsers> getUserTotal(){
		return mainServiceImpl.selUserList();
	}
	
	
	@RequestMapping("getUsersToday")
	@ResponseBody
	public List<TbUsers> getUsersToday(){
		return mainServiceImpl.selUsersToday();
	}
	@RequestMapping("getUsersYestoday")
	@ResponseBody
	public List<TbUsers> getUsersYestoday(){
		return mainServiceImpl.selUsersYestoday();
	}
	@RequestMapping("getUsersYearWeek")
	@ResponseBody
	public List<TbUsers> getUsersYearWeek(){
		return mainServiceImpl.selUsersYearWeek();
	}
	
	@RequestMapping("getUsersMonth")
	@ResponseBody
	public List<TbUsers> getUsersMonth(){
		return mainServiceImpl.selUsersMonth();
	}
	
	@RequestMapping("/dataAccessGender")
	@ResponseBody
	public Map<String, Object> dataAccessGender() {
	    Map<String, Object> j=new HashMap<>();
	    String[] categories = {"男", "女", "保密"};
	    j.put("categories", categories);
	    Map<String, Object> json=null;
	    List<Map<String, Object>> list=new ArrayList<>();
	    for(int i=0;i<categories.length;i++){
	    	json = new HashMap<String, Object>();
		    json.put("value", mainServiceImpl.seUserCountBygender(i));
		    json.put("name",categories[i]);
		    list.add(json);
	    }
	    j.put("values", list);
	    return j;
	}
}
