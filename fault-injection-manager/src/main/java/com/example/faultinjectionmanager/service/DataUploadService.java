package com.example.faultinjectionmanager.service;

import com.example.faultinjectionmanager.other.DataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataUploadService {
    //实现将状态和性能数据上报给web管理系统的逻辑
    private final RestTemplate restTemplate;

    //Web管理系统的接收数据端点url；
    private final String webManagementSystemEndpoint="";

    @Autowired
    public DataUploadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void uploadData(DataObject dataObject){
        //将数据上传到web管理系统的代码
        //设置http请求头为json格式
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //创建一个HttpEntity对象，包含请求头和数据对象
        HttpEntity<DataObject> request=new HttpEntity<>(dataObject,headers);

        //发送post请求到web管理系统，并接收响应
        String response=restTemplate.postForObject(webManagementSystemEndpoint,request,String.class);

        System.out.println("Response from web management system :" + response);

    }
}
