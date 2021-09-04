package com.zj.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.zj.server.pojo.*;
import com.zj.server.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoujian
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/system/basic/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "查询所有员工(分页)")
    @GetMapping("/")
    public RespPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    Employee employee,
                                    LocalDate[] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage,size,employee,beginDateScope);
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsStatus")
    private List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }
    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevel")
    public List<Joblevel> getAllJobLevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nation")
    public List<Nation> getAllNation(){
        return nationService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/position")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartments();
    }

    /**
     * 添加员工的工号就用当前最大的工号+1的值
     * @return
     */
    @ApiOperation(value = "获取工号")
    @GetMapping("/maxWorkID")
    public RespBean maxWorkID(){
        return employeeService.maxWorkID();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee){
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee){
        if (employeeService.updateById(employee)){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id){
        if (employeeService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    /*
    * 没有传id的话就是查询所有，传id的话就可以查询指定员工
    * sql中通过if标签来实现，id为空则不加上 and e.id=#{id}
    * */
    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> list = employeeService.getEmployee(null);
       /* ExportParams(导出之后的文件名,表格名, 导出Excel的类型，HSSF是03版的，XSSF是07版的);
          03版的导出速度更快，兼容性更好*/
        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, list);
        ServletOutputStream outputStream = null;
        try {
            //流形式传输
            response.setHeader("content-type","application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
             outputStream = response.getOutputStream();
             workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导入员工数据")
    @ApiImplicitParams({@ApiImplicitParam(name="file",value = "上传文件",dataType = "MultipartFile")})
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        //去掉标题行
        params.setTitleRows(1);
        //mybatisPlus查询数据库中全部的nation
        List<Nation> nations = nationService.list();
        List<PoliticsStatus> politicsStatuses = politicsStatusService.list();
        List<Position> positions = positionService.list();
        List<Department> departments = departmentService.list();
        List<Joblevel> joblevels = joblevelService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee ->{

                /*
                * 先employee.getNation().getName()获取name，在通过有参构造获得nation对象 只有name ,
                * 在通过indexOf 返回集合中第一次出现该对象的索引，在通过nations.get() 获取到完整nation对象
                * (数据库中查询的全部nation对象集合nations)，getId获得对应的Id,也就是Employee中的nationId
                * 建立在Nation中重写了name的hashcode and equals方法的基础上，且name不为空。indexOf方法时要使用。
                *  */
                //民族id
                employee.setNationId(nations.get(nations.indexOf(new Nation(employee.getNation().getName()))).getId());
                //政治面貌id
                employee.setPoliticId(politicsStatuses.get(politicsStatuses.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                //部门id
                employee.setDepartmentId(departments.get(departments.indexOf(new Department(employee.getDepartment().getName()))).getId());
                //职称id
                employee.setJobLevelId(joblevels.get(joblevels.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                //职位id
                employee.setPosId(positions.get(positions.indexOf(new Position(employee.getPosition().getName()))).getId());
            });
            if (employeeService.saveBatch(list)){
                return RespBean.success("导入成功");
            }
            return RespBean.error("导入失败");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }

}
