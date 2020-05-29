package ge.spring.controller;

import ge.spring.netty.tcp.GroupChatBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dengzhipeng
 * @version 1.0
 * @date 2020/5/29 0029
 */
@RestController
public class TestController {

    @Resource
    GroupChatBean groupChatBean;


    @GetMapping("/test")
    public Object test(){
//        Map<String, Channel> channels = GroupChatServerHandelThread.channels;
        groupChatBean.channels.forEach((k, v) -> {
            v.writeAndFlush("[服务器广播]" + "6666");
        });
        return "ok";
    }
}
