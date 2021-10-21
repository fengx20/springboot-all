package com.fengx.bootnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Fengx
 * @date: 2021-09-17
 * @description: NettyClient客户端，用于测试
 **/
public class ApiTest {

    public static void main(String[] args) {
        // 创建线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建bootstrap对象，配置参数
            // Bootstrap和ServerBootStrap是Netty提供的一个创建客户端和服务端启动器的工厂类，使用这个工厂类非常便利地创建启动类
            // 都是继承于AbstractBootStrap抽象类，所以大致上的配置方法都相同
            Bootstrap b = new Bootstrap();
            // 设置线程组
            b.group(workerGroup);
            // 设置客户端的通道实现类型
            b.channel(NioSocketChannel.class);
            // option()设置的是服务端用于接收进来的连接，也就是parentGroup线程
            b.option(ChannelOption.AUTO_READ, true);
            // 使用匿名内部类初始化通道
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    // 基于换行符号
                    channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    // 解码转String，注意调整自己的编码格式GBK、UTF-8
                    channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
                    // 解码转String，注意调整自己的编码格式GBK、UTF-8
                    channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                    // 在管道中添加我们自己的接收数据实现方法
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 客户端接收服务端的消息
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            // 接收msg消息
                            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 客户端接收到服务端消息：" + msg);
                        }
                    });
                }
            });
            ChannelFuture f = b.connect("127.0.0.1", 7397).sync();
            System.out.println("netty客户端已启动");

            //向服务端发送信息
            f.channel().writeAndFlush("你好，SpringBoot启动的netty服务端，我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            f.channel().writeAndFlush("你好，SpringBoot启动的netty服务端，我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            f.channel().writeAndFlush("你好，SpringBoot启动的netty服务端，我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            f.channel().writeAndFlush("你好，SpringBoot启动的netty服务端，我的结尾是一个换行符，用于传输半包粘包处理”\r\n");
            f.channel().writeAndFlush("你好，SpringBoot启动的netty服务端，我的结尾是一个换行符，用于传输半包粘包处理”\r\n");

            // closeFuture().syncUninterruptibly()就是让当前线程(即主线程)同步等待Netty server的close事件，
            // Netty server的channel close后，主线程才会继续往下执行。closeFuture()在channel close的时候会通知当前线程
            f.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出机制，使线程池退出，Netty处理的是线程池，线程池的关闭要求其中的每一个线程关闭
            workerGroup.shutdownGracefully();
        }
    }

}
