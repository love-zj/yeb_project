package com.zj.server.mapper;

import com.zj.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zj.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 获取所有操作员
     * @param id
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(Integer id, String keywords);

}
