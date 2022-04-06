package com.fzh.springcloud.controller;

import com.fzh.springcloud.entities.CommonResult;
import com.fzh.springcloud.entities.Payment;
import com.fzh.springcloud.myrule.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping(value = "consumer")
@Slf4j
public class OrderController {

    //public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    public static final String serviceName = "CLOUD-PAYMENT-SERVICE";
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private LoadBalancer loadBalancer;

    @GetMapping("payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);  //写操作
    }

    @GetMapping("payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        log.info("==========================获取: "+id);
        CommonResult s = restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
        return s;
    }

    @GetMapping("payment/getByMyBL/{id}")
    public CommonResult<Payment> getPaymentByMyBL(@PathVariable("id") Long id){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances ==null || instances.size()<=0){
            return new CommonResult<Payment>(444,"操作失败！");
        }
        ServiceInstance serviceInstance = loadBalancer.instance(instances);
        URI url = serviceInstance.getUri();
        CommonResult s = restTemplate.getForObject(url+"/payment/get/"+id,CommonResult.class);
        return s;
    }
}
