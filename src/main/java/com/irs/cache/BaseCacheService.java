package com.irs.cache;

import com.irs.pojo.TbRoles;

public interface BaseCacheService {
    /**
     * 返回所有的用户角色列表或者单个用户的角色信息
     * @param userId
     * @return
     */
    TbRoles getRoleByUserId(String userId);
}
