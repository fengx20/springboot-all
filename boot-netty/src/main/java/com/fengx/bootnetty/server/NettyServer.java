package com.fengx.bootnetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author: Fengx
 * @date: 2021-09-17
 * @description:
 **/
@Component("nettyServer")
public class NettyServer {

    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    /**
     * EventLoopGroup 包含一组 EventLoop，Channel 通过注册到 EventLoop 中执行操作
     */
    // 配置服务端NIO线程组
    // parentGroup 用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到childGroup的Selector中
    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    // childGroup 用于处理每一个连接发生的读写事件
    private final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel channel;

    // InetSocketAddress：此类实现 IP 套接字地址（IP 地址 + 端口号）,不依赖任何协议。
    public ChannelFuture bing(InetSocketAddress address) {
        /**
         * Netty中的所有IO操作都是异步的。这意味着任何IO调用都将立即返回，
         * 而不能保证所请求的IO操作在调用结束时完成。相反，将返回一个带有ChannelFuture的实例，该实例将提供有关IO操作的结果或状态的信息。
         * ChannelFuture要么是未完成状态，要么是已完成状态
         */
        ChannelFuture channelFuture = null;
        try {
            /**
             * 创建服务端的启动对象，设置参数
             * Bootstrap和ServerBootStrap是Netty提供的一个创建客户端和服务端启动器的工厂类，使用这个工厂类非常便利地创建启动类
             * 都是继承于AbstractBootStrap抽象类，所以大致上的配置方法都相同
             */
            ServerBootstrap b = new ServerBootstrap();
            // 设置两个线程组parentGroup和childGroup
            b.group(parentGroup, childGroup)
                    /**
                     * 设置服务端通道实现类型，非阻塞模式
                     * 这个方法用于设置通道类型，当建立连接后，会根据这个设置创建对应的Channel实例
                     * NioSocketChannel：异步非阻塞的客户端 TCP Socket 连接。
                     * NioServerSocketChannel：异步非阻塞的服务器端 TCP Socket 连接。
                     *
                     * 常用的就是这两个通道类型，因为是异步非阻塞的。所以是首选。
                     * OioSocketChannel：同步阻塞的客户端 TCP Socket 连接。
                     * OioServerSocketChannel：同步阻塞的服务器端 TCP Socket 连接。
                     * NioSctpChannel：异步的客户端 Sctp（Stream Control Transmission Protocol，流控制传输协议）连接。
                     * NioSctpServerChannel：异步的 Sctp 服务器端连接。
                     */
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 设置线程队列得到连接个数
                     * option()设置的是服务端用于接收进来的连接，也就是parentGroup线程
                     * SO_BACKLOG 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    /**
                     * 设置保持活动连接状态
                     * childOption()是提供给父管道接收到的连接，也就是childGroup线程
                     * SO_KEEPALIVE 连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    /**
                     * 使用匿名内部类的形式初始化通道对象
                     * ChannelPipeline是Netty处理请求的责任链，ChannelHandler则是具体处理请求的处理器。实际上每一个channel都有一个处理器的流水线。
                     * childHandler()方法需要初始化通道，实例化一个ChannelInitializer，
                     * 这时候需要重写initChannel()初始化通道的方法，装配流水线就是在这个地方进行
                     */
                    .childHandler(new MyChannelInitializer());
            /**
             * Netty中的所有IO操作都是异步的。这意味着任何IO调用都将立即返回，
             * 而不能保证所请求的IO操作在调用结束时完成。相反，将返回一个带有ChannelFuture的实例，该实例将提供有关IO操作的结果或状态的信息。
             * ChannelFuture要么是未完成状态，要么是已完成状态
             *
             * 绑定端口号，启动服务端
             * bind() 提供用于服务端或者客户端绑定服务器地址和端口号，默认是异步启动。如果加上sync()方法则是同步。
             */
            channelFuture = b.bind(address).syncUninterruptibly();
            channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                logger.info("netty服务端已启动");
            } else {
                logger.error("netty服务端启动错误");
            }
        }
        return channelFuture;
    }

    public void destroy() {
        if (null == channel) return;
        channel.close();
        // 使线程池退出
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }

    public Channel getChannel() {
        return channel;
    }

}
