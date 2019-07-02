package com.irs.service.impl;

import com.irs.cache.BaseCacheService;
import com.irs.cache.CacheConstants;
import com.irs.mapper.TbRolesMapper;
import com.irs.pojo.TbRoles;
import com.irs.util.GlobalMessageUtil;
import com.irs.util.RRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.*;

/**
 * basecache基类
 */
@Service
public class BaseCacheServiceImpl implements BaseCacheService {

    private final ConcurrentMap<String, Future<TbRoles>> BaseCacheMap_TbRole_By_UserId = new ConcurrentHashMap<String, Future<TbRoles>>();


    @Autowired
    private TbRolesMapper tbRolesMapper;
    @Override
    public TbRoles getRoleByUserId(String userId) {
        return getBaseCache(BaseCacheMap_TbRole_By_UserId, userId,"getRoleByUserId");
    }
    //利用future实现的缓存
    private <T> T getBaseCache(ConcurrentMap<String, Future<T>> cacheMap, String userId,String type) {
        while (true) {
            Future<T> f = cacheMap.get(userId);
            if (f == null) {
                Callable<T> eval = new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        // TODO 类型转换不够优雅，后续去优化
                        if (type.equals(CacheConstants.GET_ROLE_BY_USERID)) {
                            Example example = new Example(TbRoles.class);
                            List<TbRoles> list = tbRolesMapper.selectByExample(example);
                            return (T) list;
                        } else {
                            return null;
                        }
                    }
                };
                FutureTask<T> ft = new FutureTask<T>(eval);
                f = cacheMap.putIfAbsent(userId, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (ExecutionException e) {
                //todo 后续可更改为格式化数据，将参数加上
                GlobalMessageUtil.getString(this.getClass().getName(),"缓存版块->","加载缓存失败异常");
                throw new RRException("读取用户信息异常");
            } catch (Exception e) {throw new RRException("读取用户信息异常");
            }
        }
    }
}
