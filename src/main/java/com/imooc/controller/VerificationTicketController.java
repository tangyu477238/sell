package com.imooc.controller;

import com.imooc.service.VerificationTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/verification")
public class VerificationTicketController {


    @Autowired
    private VerificationTicketService verificationTicketService;

    @ResponseBody
    @GetMapping("/cktikcetCzy")
    public String cktikcetCzy(@RequestParam() Map<String,Object> map){




        return "{\"timelist\":[\"19:10\",\"7:10\"],\"plList\":[{\"id\":1,\"fromStation\":\"宏发\",\"toStation\":\"幸福誉\"},{\"id\":2,\"fromStation\":\"幸福誉\",\"toStation\":\"宏发\"}]}";
    }



    @ResponseBody
    @GetMapping("/cktikcetYpjl")
    public Map<String,Object> cktikcetYpjl(@RequestParam() Map<String,Object> map){



        return verificationTicketService.cktikcetYpjl(map);
    }
    //cktikcet  验票系统
    @ResponseBody
    @GetMapping("/ckticket")
    public Map cktikcet(@RequestParam("uid") String uid,
                        Map<String,Object> map){
        log.info("进入cktikcet订单方法.......");
        map = verificationTicketService.cktikcet(uid);

        return map;
    }
}
