package com.irs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.irs.mapper.TbUsersMapper;
import com.irs.pojo.TbUsers;
import com.irs.pojo.UserSearch;
import com.irs.service.UserService;
import com.irs.util.EmailUtil;
import com.irs.util.GlobalUtil;
import com.irs.util.MyUtil;
import com.irs.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUsersMapper tbUsersMapper;
	
	@Override
	public TbUsers selUserByEmail(String eMail,Long uid) {
		Example example=new Example(TbUsers.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("eMail",eMail);
		if(uid!=null&&!"".equals(uid)){
			criteria.andEqualTo("uid",uid);
		}
		List<TbUsers> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public TbUsers selUserByNickname(String nickname,Long uid) {
		Example example=new Example(TbUsers.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("nickname",nickname);
		if(uid!=null&&!"".equals(uid)){
			criteria.andNotEqualTo("uid",uid);
		}
		List<TbUsers> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public void insUserService(TbUsers user) throws Exception {
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		String code=MyUtil.getStrUUID();
		user.seteCode(code);
		Date date=new Date();
		user.setCreateTime(date);
		if(Boolean.parseBoolean(GlobalUtil.getValue("send.email"))){
			user.setStatus(0+"");//0:未激活，1，正常，2，禁用
			EmailUtil.sendMail(user.geteMail(),code);
		}else{
			user.setStatus(1+"");//0:未激活，1，正常，2，禁用
		}
		tbUsersMapper.insertSelective(user);
	}

	@Override
	public ResultUtil selUsers(Integer page, Integer limit,UserSearch search) {
		PageHelper.startPage(page, limit);
		Example example=new Example(TbUsers.class);
		Example.Criteria criteria = example.createCriteria();
		//设置按创建时间降序排序
		example.setOrderByClause("create_time DESC");
		if(search.getNickname()!=null&&!"".equals(search.getNickname())){
			//注意：模糊查询需要进行拼接”%“  如下，不进行拼接是不能完成查询的哦。
			criteria.andLike("nickname","%"+search.getNickname()+"%");
		}
		if(search.getSex()!=null&&!"-1".equals(search.getSex())){
			criteria.andEqualTo("sex",search.getSex());
		}
		if(search.getStatus()!=null&&!"-1".equals(search.getStatus())){
			criteria.andEqualTo("status",search.getStatus());
		}
		if(search.getCreateTimeStart()!=null&&!"".equals(search.getCreateTimeStart())){
			criteria.andGreaterThanOrEqualTo("createTime",MyUtil.getDateByString(search.getCreateTimeStart()));
		}
		if(search.getCreateTimeEnd()!=null&&!"".equals(search.getCreateTimeEnd())){
			criteria.andLessThanOrEqualTo("createTime",MyUtil.getDateByString(search.getCreateTimeEnd()));
		}
		List<TbUsers> users = tbUsersMapper.selectByExample(example);
		PageInfo<TbUsers> pageInfo = new PageInfo<TbUsers>(users);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public void delUsersService(String userStr) {
		String[] users = userStr.split(",");
		if(users!=null&&users.length>0){
			for (String uid : users) {
				tbUsersMapper.deleteByPrimaryKey(Long.parseLong(uid));
			}
		}
	}

	@Override
	public void delUserByUid(String uid) {
		tbUsersMapper.deleteByPrimaryKey(Long.parseLong(uid));
	}

	@Override
	public TbUsers selUserByUid(Long uid) {
		return tbUsersMapper.selectByPrimaryKey(uid);
	}

	@Override
	public void updUserService(TbUsers user) {
		TbUsers u = tbUsersMapper.selectByPrimaryKey(user.getUid());
		user.setPassword(u.getPassword());
		user.seteCode(u.geteCode());
		user.setCreateTime(u.getCreateTime());
		tbUsersMapper.updateByPrimaryKey(user);
	}

}
