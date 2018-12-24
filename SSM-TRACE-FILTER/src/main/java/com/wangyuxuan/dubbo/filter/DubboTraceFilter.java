package com.wangyuxuan.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: wangyuxuan
 * @Date: 2018/12/24 10:29
 * @Description: dubbo日志拦截插件，方便排查定位异常。
 */

@Slf4j
@Activate(group = Constants.PROVIDER, order = -999)
public class DubboTraceFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            FilterDesc filterReq = new FilterDesc();
            filterReq.setInterfaceName(invocation.getInvoker().getInterface().getName());
            filterReq.setMethodName(invocation.getMethodName());
            filterReq.setArgs(invocation.getArguments());
            log.debug("dubbo请求数据:{}", JSON.toJSONString(filterReq));

            Result result = invoker.invoke(invocation);
            if (result.hasException() && invoker.getInterface() != GenericService.class) {
                log.error("dubbo执行异常", result.getException());
            } else {
                log.info("dubbo执行成功");
                FilterDesc filterRsp = new FilterDesc();
                filterRsp.setInterfaceName(invocation.getInvoker().getInterface().getName());
                filterRsp.setMethodName(invocation.getMethodName());
                filterRsp.setArgs(new Object[]{result.getValue()});
                log.debug("dubbo返回数据:{}", JSON.toJSONString(filterRsp));
            }
            return result;
        } catch (RuntimeException e) {
            log.error("dubbo未知异常" + RpcContext.getContext().getRemoteHost()
                    + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                    + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
            throw e;
        }
    }
}
