package com.huawei.charging.adapter;

import com.huawei.charging.application.ChargeServiceI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;

@Controller
public class ChargeController {

    @Resource
    private ChargeServiceI chargeService;

    @PostMapping("begin")
    public void begin(){
    }

    @PostMapping("charge")
    public void charge(){
    }

    @PostMapping("end")
    public void end(){
    }
}
