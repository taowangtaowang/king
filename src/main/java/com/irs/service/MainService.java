package com.irs.service;

import java.util.List;

import com.irs.pojo.TbUsers;

public interface MainService {

	public List<TbUsers> selUserList();

	public List<TbUsers> selUsersToday();

	public List<TbUsers> selUsersYestoday();
	
	public List<TbUsers> selUsersYearWeek();
	
	public List<TbUsers> selUsersMonth();

	public int seUserCountBygender(int i);
}
