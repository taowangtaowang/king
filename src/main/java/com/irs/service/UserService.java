package com.irs.service;

import com.irs.pojo.TbUsers;
import com.irs.pojo.UserSearch;
import com.irs.util.ResultUtil;

public interface UserService {
	//用户邮箱唯一性检验
	public TbUsers selUserByEmail(String eMail, Long uid);

	//用户昵称唯一性检验
	public TbUsers selUserByNickname(String nickname, Long uid);

	//增加用户
	public void insUserService(TbUsers user) throws Exception;

	//得到用户信息
	public ResultUtil selUsers(Integer page, Integer limit, UserSearch search);

	//批量删除用户
	public void delUsersService(String userStr);

	//删除指定用户
	public void delUserByUid(String uid);

	//查询用户
	public TbUsers selUserByUid(Long uid);

	//更新用户信息
	public void updUserService(TbUsers user);
}
