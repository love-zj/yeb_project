package com.zj.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zj.server.pojo.MenuRole;
import com.zj.server.mapper.MenuRoleMapper;
import com.zj.server.pojo.RespBean;
import com.zj.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Resource
    private MenuRoleMapper menuRoleMapper;


    /**
     * 更新角色菜单
     *
     * 可能是删除一些菜单，可能是添加一些菜单，可能又增又删。
     *
     * 采用的方法是先把rid对应的角色的菜单全部清空，再把传入的菜单id与角色id关联起来，给对应的角色id添加上对应的菜单id
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional  //因为涉及到删除和插入，所以加上事务
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //删除掉全部菜单时
        if (null==mids||0==mids.length){
            return RespBean.success("更新成功!");
        }
        Integer row = menuRoleMapper.insertRecord(rid, mids);
        if (row==mids.length){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }
}
