package com.irs.mapper;

import com.irs.pojo.TbMenus;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdminMenusMapper extends Mapper<TbMenus> {
	@Select("SELECT m.menuId as menuId,m.title,m.icon,m.href,m.spread,m.parentId as parentId,m.perms FROM tb_roles_menus r LEFT JOIN tb_menus m ON r.menuId = m.menuId WHERE r.roleId = #{0} order by sorting desc")
	List<TbMenus> getMenus(Long roleId);
}
