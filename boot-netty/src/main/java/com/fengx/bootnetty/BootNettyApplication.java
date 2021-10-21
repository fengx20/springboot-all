package com.fengx.bootnetty;

import com.fengx.bootnetty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class BootNettyApplication implements CommandLineRunner {

    @Value("${netty.host}")
    private String host;
    @Value("${netty.port}")
    private int port;
    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(BootNettyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 此类实现 IP 套接字地址（IP 地址 + 端口号）,不依赖任何协议。
        InetSocketAddress address = new InetSocketAddress(host, port);
        // 绑定端口号，启动服务端，开启监听
        ChannelFuture channelFuture = nettyServer.bing(address);
        // 在JVM销毁前执行的一个线程，当然这个线程依然要自己写，这里执行netty服务端关闭的线程
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
        // Netty server启动：绑定端口，开启监听是通过异步开启一个子线程执行的，当前线程不会同步等待；
        // closeFuture().syncUninterruptibly()就是让当前线程(即主线程)同步等待Netty server的close事件，
        // Netty server的channel close后，主线程才会继续往下执行。closeFuture()在channel close的时候会通知当前线程
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }

}
