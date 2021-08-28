package com.zj.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.server.pojo.Admin;
import com.zj.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登陆之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, HttpServletRequest request);

}
