package com.fengx.bootlog;

import com.fengx.bootlog.codec.ObjDecoder;
import com.fengx.bootlog.codec.ObjEncoder;
import com.fengx.bootlog.entity.TransportProtocol;
import com.fengx.bootlog.entity.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.UUID;

/**
 * @author: Fengx
 * @date: 2021-09-27
 * @description: 测试类
 **/
public class ApiTest {

    public static void main(String[] args) {
        System.out.println("netty客户端测试开始");
        // 线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 客户端启动器
            Bootstrap b = new Bootstrap();
            // 设置线程组
            b.group(workerGroup);
            // 客户端异步非阻塞模式
            b.channel(NioSocketChannel.class);
            // 设置自动读取模式
            b.option(ChannelOption.AUTO_READ, true);
            // 初始化通道对象
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    // 对象传输处理
                    channel.pipeline().addLast(new ObjDecoder(TransportProtocol.class));
                    channel.pipeline().addLast(new ObjEncoder(TransportProtocol.class));
                    // 在管道中添加我们自己的接收数据实现方法
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                        }
                    });
                }
            });
            // 连接服务端
            ChannelFuture f = b.connect("127.0.0.1", 7397).sync();
            System.out.println("客户端已启动");

            TransportProtocol tp1 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "李小明", 1, "T0-1", new Date(), "13566668888", "184172133@qq.com", "北京"));
            TransportProtocol tp2 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "张大明", 2, "T0-2", new Date(), "13566660001", "huahua@qq.com", "南京"));
            TransportProtocol tp3 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "李书鹏", 2, "T1-1", new Date(), "13566660002", "xiaobai@qq.com", "榆树"));
            TransportProtocol tp4 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "韩小雪", 2, "T2-1", new Date(), "13566660002", "xiaobai@qq.com", "榆树"));
            TransportProtocol tp5 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "董叔飞", 2, "T4-1", new Date(), "13566660002", "xiaobai@qq.com", "河北"));
            TransportProtocol tp6 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "候明相", 2, "T5-1", new Date(), "13566660002", "xiaobai@qq.com", "下花园"));
            TransportProtocol tp7 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "田明明", 2, "T3-1", new Date(), "13566660002", "xiaobai@qq.com", "东平"));
            TransportProtocol tp8 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "王大伟", 2, "T4-1", new Date(), "13566660002", "xiaobai@qq.com", "西湖"));
            TransportProtocol tp9 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "李雪明", 2, "T1-1", new Date(), "13566660002", "xiaobai@qq.com", "南昌"));
            TransportProtocol tp10 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "朱小飞", 2, "T2-1", new Date(), "13566660002", "xiaobai@qq.com", "吉林"));
            TransportProtocol tp11 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "牛大明", 2, "T1-1", new Date(), "13566660002", "xiaobai@qq.com", "长春"));
            TransportProtocol tp12 = new TransportProtocol(1, new User(UUID.randomUUID().toString(), "关雪儿", 2, "T2-1", new Date(), "13566660002", "xiaobai@qq.com", "深圳"));

            // 向服务端发送信息
            f.channel().writeAndFlush(tp1);
            f.channel().writeAndFlush(tp2);
            f.channel().writeAndFlush(tp3);
            f.channel().writeAndFlush(tp4);
            f.channel().writeAndFlush(tp5);
            f.channel().writeAndFlush(tp6);
            f.channel().writeAndFlush(tp7);
            f.channel().writeAndFlush(tp8);
            f.channel().writeAndFlush(tp9);
            f.channel().writeAndFlush(tp10);
            f.channel().writeAndFlush(tp11);
            f.channel().writeAndFlush(tp12);

            f.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
