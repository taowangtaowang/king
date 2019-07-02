package com.irs.service.impl;

import com.irs.mapper.MainMapper;
import com.irs.mapper.TbUsersMapper;
import com.irs.pojo.TbUsers;
import com.irs.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MainServiceImpl implements MainService {
	
	@Autowired
	private TbUsersMapper tbUsersMapper;
	
	@Autowired
	private MainMapper mainMapper;

	@Override
	public List<TbUsers> selUserList() {
		Example example=new Example(TbUsers.class);
		Example.Criteria criteria = example.createCriteria();
		return tbUsersMapper.selectByExample(example);
	}
	
	@Override
	public List<TbUsers> selUsersToday() {
		return mainMapper.selUsersToday();
	}

	@Override
	public List<TbUsers> selUsersYestoday() {
		return mainMapper.selUsersYesterday();
	}


	@Override
	public List<TbUsers> selUsersYearWeek() {
		// TODO Auto-generated method stub
		return mainMapper.selUsersYearWeek();
	}
	
	@Override
	public List<TbUsers> selUsersMonth() {
		return mainMapper.selUsersMonth();
	}

	@Override
	public int seUserCountBygender(int i) {
		Example example=new Example(TbUsers.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("sex",i+"");
		List<TbUsers> list = tbUsersMapper.selectByExample(example);
		return list.size();
	}

}
