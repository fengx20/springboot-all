package com.fengx.bootlog.web;

import com.fengx.bootlog.netty.server.NettyServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Fengx
 * @date: 2021-09-26
 * @description: 控制层
 **/
@RestController
public class NettyController {

    @Resource
    private NettyServer nettyServer;

    @RequestMapping("/localAddress")
    public String localAddress() {
        return "netty服务地址：" + nettyServer.getChannel().localAddress();
    }
}
