package com.mq.mq_demo.boot.conroller;

import com.mq.mq_demo.boot.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestRabbitController {
    @Autowired
    private MsgProducer msgProducer;
    @RequestMapping("/send")
    public String send(){
        for (int i = 0;i<10;i++){

            msgProducer.sendMsg("myContent_"+i);
        }
        return "ok";
    }
}
