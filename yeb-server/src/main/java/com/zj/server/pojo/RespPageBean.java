package com.zj.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页公共返回对象
 *
 * @author zhoujian on 2021/9/3 0003 11:25
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {

    private Long total; //总条数
    private List<?> data; //数据

}
