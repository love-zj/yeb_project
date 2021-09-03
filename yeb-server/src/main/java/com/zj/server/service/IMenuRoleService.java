package com.zj.server.service;

import com.zj.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.server.pojo.RespBean;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(@Param("rid") Integer rid, @Param("mids") Integer[] mids);

}
