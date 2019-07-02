package com.irs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.irs.mapper.TbCarouselMapper;
import com.irs.pojo.TbCarousel;
import com.irs.service.CarouselService;
import com.irs.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
@Service
public class CarouselServiceImpl implements CarouselService {
	@Autowired
	private TbCarouselMapper tbCarouselMapper;

	@Override
	public ResultUtil selCarousels(Integer page, Integer limit) {
		PageHelper.startPage(page, limit);
		Example example=new Example(TbCarousel.class);
		example.setOrderByClause("sorting DESC");
		List<TbCarousel> list = tbCarouselMapper.selectByExample(example);
		PageInfo<TbCarousel> pageInfo = new PageInfo<TbCarousel>(list);
		ResultUtil resultUtil = new ResultUtil();
		resultUtil.setCode(0);
		resultUtil.setCount(pageInfo.getTotal());
		resultUtil.setData(pageInfo.getList());
		return resultUtil;
	}

	@Override
	public void insCarouselService(TbCarousel carousel) {
		Date date=new Date();
		carousel.setCreateTime(date);
		tbCarouselMapper.insertSelective(carousel);
	}

	@Override
	public void updCarouselStatusById(Integer id, Integer status) {
		TbCarousel carousel = tbCarouselMapper.selectByPrimaryKey(id);
		carousel.setStatus(status);
		carousel.setUpdateTime(new Date());
		tbCarouselMapper.updateByPrimaryKey(carousel);
	}

	@Override
	public void delCarouselById(Integer id) {
		tbCarouselMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void delCarouselByIds(String carouselStr) {
		if(!StringUtils.isBlank(carouselStr)){
			String[] ids=carouselStr.split(",");
			for (String id : ids) {
				tbCarouselMapper.deleteByPrimaryKey(Integer.parseInt(id));
			}
		}
	}

	@Override
	public TbCarousel selCarouselById(Integer id) {
		return tbCarouselMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updCarouselService(TbCarousel carousel) {
		TbCarousel c = tbCarouselMapper.selectByPrimaryKey(carousel.getId());
		carousel.setCreateTime(c.getCreateTime());
		carousel.setUpdateTime(new Date());
		tbCarouselMapper.updateByPrimaryKey(carousel);
	}

}
