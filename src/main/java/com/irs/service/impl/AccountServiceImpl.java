package com.irs.service.impl;

import com.irs.mapper.TbUsersMapper;
import com.irs.pojo.TbUsers;
import com.irs.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private TbUsersMapper tbUsersMapper;

	@Override
	public TbUsers selUserByCodeAndStatus(String eCode,String status) {
		Example example=new Example(TbUsers.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("eCode",eCode);
		criteria.andEqualTo("status",status);
		List<TbUsers> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public void updUserStatus(TbUsers user) {
		tbUsersMapper.updateByPrimaryKey(user);
	}

}
