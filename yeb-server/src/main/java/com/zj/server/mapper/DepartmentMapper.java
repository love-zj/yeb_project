package com.zj.server.mapper;

import com.zj.server.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     * @return
     */
    List<Department> getAllDepartments(@Param("parentId") Integer parentId);

    /**
     * 添加部门
     * @param department
     */
    void addDepartment(Department department);

     /**
     * 删除部门
     * @param department
     */
    void deleteDepartment(Department department);
}
