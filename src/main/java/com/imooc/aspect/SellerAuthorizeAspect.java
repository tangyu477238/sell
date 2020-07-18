package com.imooc.aspect;

import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisConstant;
import com.imooc.exception.BusinessException;
import com.imooc.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by SqMax on 2018/4/2.
 */
@Aspect
@Slf4j
@Configuration
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("(execution(public * com.imooc.controller.Seller*.*(..))"
            +"|| execution(public * com.imooc.controller.Buy*.*(..)))"
            +"&& !execution(public * com.imooc.controller.Buy*.notify*(..))"
            +"&& !execution(public * com.imooc.controller.VerificationTicket*.*(..))" //验票
            +"&& !execution(public * com.imooc.controller.Buy*.queryOrder*(..))"
            +"&& !execution(public * com.imooc.controller.SellerUserController.*(..))"

            )

    public void verify(){}

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();

        //查询cookie
        Cookie cookie= CookieUtil.get(request,CookieConstant.TOKEN);
        if (cookie==null){
            log.warn("【登录校验】Cookie中查不到token");
            throw new BusinessException("500","系统错误");
        }
        //去redis查询
        String tokenValue=redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if (StringUtils.isEmpty(tokenValue)){
            log.warn("【登录校验】 Redis中查不到token");
            throw new BusinessException("500","系统错误");
        }
    }

}
