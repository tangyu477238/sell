package com.imooc.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019\1\6 0006 13:32
 * @Description:
 */
@ControllerAdvice
public class AllControllerAdvice {



    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     */
    @ModelAttribute
    public void addAttributes(Model model) {
    }



    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
//    @ResponseBody
//    @ExceptionHandler(value = MyException.class)
//    public Map myErrorHandler(MyException ex) {
//        Map map = new HashMap();
//        map.put("code", ex.getCode());
//        map.put("msg", ex.getMsg());
//        return map;
//    }


    @ExceptionHandler(value = BusinessException.class)
    public ModelAndView myErrorHandler(BusinessException ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/fail");
        modelAndView.addObject("code", ex.getCode());
        modelAndView.addObject("msg", ex.getMsg());
        return modelAndView;
    }
}
