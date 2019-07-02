package com.irs.mapper;

import com.irs.pojo.TbUsers;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface MainMapper extends Mapper<TbUsers>{
	@Select("select * from tb_users where to_days(create_time) = to_days(now());")
	List<TbUsers> selUsersToday();
	
	@Select("SELECT * FROM tb_users WHERE TO_DAYS( NOW( ) ) - TO_DAYS( create_time)= 1  ")
	List<TbUsers> selUsersYesterday();
	
	@Select("SELECT * FROM  tb_users WHERE YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now());")
	List<TbUsers> selUsersYearWeek();
	
	@Select("SELECT * FROM tb_users WHERE DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )")
	List<TbUsers> selUsersMonth();
}
