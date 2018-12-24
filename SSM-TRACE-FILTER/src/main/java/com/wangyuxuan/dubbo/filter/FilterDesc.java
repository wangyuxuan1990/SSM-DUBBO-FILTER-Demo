package com.wangyuxuan.dubbo.filter;

import lombok.Data;

/**
 * @Auther: wangyuxuan
 * @Date: 2018/12/24 10:26
 * @Description: 拦截对象
 */

@Data
public class FilterDesc {

    private String interfaceName ;//接口名
    private String methodName ;//方法名
    private Object[] args ;//参数
}
