package com.wh.zjj.provider.aop;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.wh.zjj.api.response.RestException;
import com.wh.zjj.api.response.RestExceptionEnum;
import com.wh.zjj.api.response.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import java.lang.reflect.Method;

/**
 * ClassName :  <br>
 * Data : 2019/3/19 8:45 <br>
 * AOP监控切面处理类
 *
 * @author : 李刚 <br>
 * @version :  <br>
 */
@Slf4j
@Component
@Aspect
public class MethodMonitorAspect {

    /**
     * dubbo接口方法监控
     *
     * @param joinPoint
     * @return
     */
    @Around("execution(* com.wh.zjj.api.service..*.*(..))")
    public Object dubboMethodMonitor(ProceedingJoinPoint joinPoint) {
        return executeMonitor(joinPoint);
    }

    /**
     * 监控逻辑
     *
     * @param joinPoint
     * @return
     */
    private Object executeMonitor(ProceedingJoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 目标方法名
        Method targetMethod = methodSignature.getMethod();
        // 监控key
        String monitorKey = targetMethod.getDeclaringClass().getName() + "." + targetMethod.getName();

        log.info("executeMonitor,start,monitorKey:{}", monitorKey);

        StopWatch watch = new StopWatch("executeMonitor:" + monitorKey);
        watch.start("monitorKey");

        RpcContext context = RpcContext.getContext();
        try {
            if (context != null) {
                String clientIp = context.getRemoteHost();
                String name = context.getRemoteHostName();
                log.info("executeMonitor,ip:{},name:{},调用,attach:{}", clientIp, name, context.getAttachments());
            }
            log.info("executeMonitor,monitorKey:{},args:{}", monitorKey, JSONObject.toJSON(joinPoint.getArgs()));

            Object result = joinPoint.proceed();

            watch.stop();
            long cost = watch.getTotalTimeMillis();

            log.info("executeMonitor,monitorKey:{},watch:{}", monitorKey, watch.prettyPrint());
            if(cost > 50) {
                log.warn("耗时过长: {}", cost);
            }
            log.info("executeMonitor,end,monitorKey:{},cost:{},return:{}", monitorKey, cost, JSONObject.toJSON(result));
            return result;
        } catch (RestException e) {
            log.error("方法:{},业务异常:{},msg:{}", monitorKey, e.getCode(), e.getMessage());
            return ResultEntity.fail(e);
        } catch (Throwable e) {
            log.error("方法:{},未知错误", monitorKey, e);
            return ResultEntity.fail(new RestException(RestExceptionEnum.SERVER_ERROR));
        }
    }

}
