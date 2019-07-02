package com.irs.mapper;

import com.irs.pojo.TbUsers;
import org.mybatis.spring.annotation.MapperScan;
import tk.mybatis.mapper.common.Mapper;

@MapperScan
public interface TbUsersMapper extends Mapper<TbUsers> {
}