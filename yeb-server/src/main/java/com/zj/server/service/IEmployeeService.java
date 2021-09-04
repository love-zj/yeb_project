package com.zj.server.service;

import com.zj.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.server.pojo.RespBean;
import com.zj.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 查询所有员工(分页)
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取工号
     * @return
     */
    RespBean maxWorkID();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean addEmp(Employee employee);

    /**
     * 查询员工
     * @param id
     */
    List<Employee> getEmployee(Integer id);
}
