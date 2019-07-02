package com.irs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.irs.mapper.TbLogMapper;
import com.irs.pojo.TbLog;
import com.irs.pojo.UserSearch;
import com.irs.service.LogService;
import com.irs.util.MyUtil;
import com.irs.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
	
	@Autowired
	private TbLogMapper tbLogMapper;

	@Override
	public void insLog(TbLog log) {
		tbLogMapper.insertSelective(log);
	}

	@Override
	public ResultUtil selLogList(Integer page, Integer limit,UserSearch search) {
		PageHelper.startPage(page, limit);
		Example example=new Example(TbLog.class);
		//设置按创建时间降序排序
		example.setOrderByClause("id DESC");
		Example.Criteria criteria = example.createCriteria();
		if(search.getOperation()!=null&&!"".equals(search.getOperation())){
			criteria.andLike("operation","%"+search.getOperation()+"%");
		}
		if(search.getCreateTimeStart()!=null&&!"".equals(search.getCreateTimeStart())){
			criteria.andGreaterThanOrEqualTo("createTime",MyUtil.getDateByString(search.getCreateTimeStart()));
		}
		if(search.getCreateTimeEnd()!=null&&!"".equals(search.getCreateTimeEnd())){
			criteria.andLessThanOrEqualTo("createTime",MyUtil.getDateByString(search.getCreateTimeEnd()));
		}
		
		List<TbLog> logs = tbLogMapper.selectByExample(example);
		PageInfo<TbLog> pageInfo = new PageInfo<TbLog>(logs);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public int delLogsByDate(Date date) {
		Example example=new Example(TbLog.class);
		Example.Criteria criteria =example.createCriteria();
		criteria.andLessThanOrEqualTo("createTime",date);
		int count = tbLogMapper.deleteByExample(example);
		return count;
	}

}
