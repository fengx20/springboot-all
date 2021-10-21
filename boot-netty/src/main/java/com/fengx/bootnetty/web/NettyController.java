package com.fengx.bootnetty.web;

import com.fengx.bootnetty.server.NettyServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Fengx
 * @date: 2021-09-17
 * @description: api
 **/
@RestController
@RequestMapping(value = "/nettyserver", method = RequestMethod.GET)
public class NettyController {

    @Resource
    private NettyServer nettyServer;

    @RequestMapping("/localAddress")
    public String localAddress() {
        return "netty服务端地址：" + nettyServer.getChannel().localAddress();
    }

    @RequestMapping("/isOpen")
    public String isOpen() {
        return "netty服务是否启动：" + nettyServer.getChannel().isOpen();
    }

}
