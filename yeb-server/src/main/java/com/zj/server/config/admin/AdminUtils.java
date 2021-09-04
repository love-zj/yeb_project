package com.zj.server.config.admin;

import com.zj.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 用户工具类
 *
 * @author zhoujian on 2021/9/2 0002 19:45
 */
public class AdminUtils {

    /**
     * 获取当前登录用户的信息
     * @return
     */
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
