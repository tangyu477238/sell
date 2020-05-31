package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.dataobject.JianyiDO;
import com.imooc.repository.JianyiRepository;
import com.imooc.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/jianyi")
public class JianyiController {


    @Autowired
    JianyiRepository jianyiRepository;

    //
    @GetMapping("/index")
    public ModelAndView jianyi(@RequestParam("uid") String uid,
                               Map<String,Object> map){
        log.info("进入jianyi方法......." );
        map.put("uid", uid);
        return new ModelAndView("mobile/jianyi", map);
    }


    @ResponseBody
    @GetMapping("/add")
    public ResultVO add(@RequestParam("uid") String uid,
                        @RequestParam("stype") String stype,
                        @RequestParam("fstation") String fstation,
                        @RequestParam("tstation") String tstation,
                        @RequestParam("banci") String banci,
                        @RequestParam("remark") String remark){
        log.info("进入add方法.......");

        JianyiDO jianyiDO = new JianyiDO();
        jianyiDO.setStype(stype);
        jianyiDO.setFstation(fstation);
        jianyiDO.setTstation(tstation);
        jianyiDO.setBanci(banci);
        jianyiDO.setRemark(remark);
        jianyiDO.setCreateTime(new Date());
        jianyiDO.setCreateUser(uid);
        jianyiRepository.save(jianyiDO);

        return ResultVOUtil.success();

    }
}
