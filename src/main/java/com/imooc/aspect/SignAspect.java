package com.imooc.aspect;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证签名切面
 * @author liugh
 * @since on 2018/5/10.
 */
@Slf4j
public class SignAspect extends AspectManager{

    private AspectApi aspectApi;

    public SignAspect(AspectApi aspectApi){
        super();
        this.aspectApi=aspectApi;
    }
    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable{
        aspectApi.doHandlerAspect(pjp,method);
        checkSign(pjp);
        return null;
    }


    private void checkSign(ProceedingJoinPoint pjp) throws Throwable{

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String methodString = request.getMethod();//得到请求URL地址时使用的方法


//        log.info("--------------------开始签名验证-----------------------");
//        if ("GET".equals(methodString)) {
//            Map<String, String[]> parmMap  = request.getParameterMap();//得到请求的URL地址中附带的参数
//            JSONObject requestJson = new JSONObject();
//            for(String key : parmMap.keySet()){
//                requestJson.put(key, parmMap.get(key)[0]);
//            }
//
//            if (!StringUtil.createSignJson(requestJson)) {
//                throw new BusinessException(PublicResultConstant.SIGN_ERROR);
//            }
//        } else if ("POST".equals(methodString)){
//            String classType = pjp.getTarget().getClass().getName();
//            Class<?> clazz = Class.forName(classType);
//            String clazzName = clazz.getName();
//            String methodName = pjp.getSignature().getName(); //获取方法名称
//            Object[] args = pjp.getArgs();//参数
//            Map<String,Object> nameAndArgs = getFieldsName(this.getClass(), clazzName, methodName,args);
//            JSONObject jsonObject = JSONObject.parseObject(nameAndArgs.get("requestJson").toString());
//            if (!StringUtil.createSignJson(jsonObject)) {
//                throw new BusinessException(PublicResultConstant.SIGN_ERROR);
//            }
//        }
//        log.info("--------------------签名验证通过-----------------------");

    }

    private Map<String,Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException {
        Map<String,Object > map = new HashMap<String,Object>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        // String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++){
            map.put( attr.variableName(i + pos),args[i]);//paramNames即参数名
        }
        return map;
    }





    /**
     * 获取方法上的url地址
     * @param method
     * @return
     */
    private String getMethodUrl(Method method,String contextPath) {
        Class<?> declaringClass = method.getDeclaringClass();
        Annotation[] annotations = declaringClass.getAnnotations();
        StringBuilder url = new StringBuilder();
        url.append(contextPath);
        for (Annotation annotation:annotations) {
            if(annotation instanceof RequestMapping){
                String[] value = ((RequestMapping) annotation).value();
                for (String tempUrl:value) {
                    url.append(tempUrl);
                }
            }
        }
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation annotation:declaredAnnotations) {
            String tempAnnotations = annotation.toString();
            if(tempAnnotations.indexOf("Mapping")>0){
                url.append(tempAnnotations.substring(tempAnnotations.indexOf("value=[")+7,tempAnnotations.lastIndexOf("],")));
            }
        }
        return url.toString().replaceAll("/+", "/");
    }


}
