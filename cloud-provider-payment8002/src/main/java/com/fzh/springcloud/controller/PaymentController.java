package com.fzh.springcloud.controller;

import com.fzh.springcloud.entities.CommonResult;
import com.fzh.springcloud.entities.Payment;
import com.fzh.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "payment")
@SuppressWarnings("unchecked")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private String  serverPort;

    @PostMapping(value = "create")
    public CommonResult create(Payment payment){
        int result = paymentService.create(payment);
        log.info("插入结果: "+result);
        if (result>0)
            return  new CommonResult(200,"插入成功！",result);
        else
            return new CommonResult(444,"插入失败！",null);
    }

    @GetMapping(value = "get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("==========================获取: "+payment);
        log.info(10/2+"");
        if (payment!=null)
            return  new CommonResult(200,"查询成功,服务端口号为："+serverPort,payment);
        else
            return new CommonResult(444,"查询Id:"+id+"失败！没有对应记录！",null);
    }
}
