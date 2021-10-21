package com.fengx.bootnetty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Fengx
 * @date: 2021-09-17
 * @description: 服务端处理器
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 * 才能被Netty框架所关联，有点类似SpringMVC的适配器模式
 *
 * ChannelInboundHandlerAdapter(入站处理器)、ChannelOutboundHandler(出站处理器)
 * 入站指的是数据从底层java NIO Channel到Netty的Channel
 * 出站指的是通过Netty的Channel来操作底层的java NIO Channel
 **/
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(MyServerHandler.class);

    // ChannelHandlerContext 可以拿到channel、pipeline等对象，就可以进行读写等操作

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        SocketChannel channel = (SocketChannel) ctx.channel();
        logger.info("链接报告开始");
        logger.info("链接报告信息：有一客户端链接到本服务端");
        logger.info("链接报告IP:{}", channel.localAddress().getHostString());
        logger.info("链接报告Port:{}", channel.localAddress().getPort());
        logger.info("链接报告完毕");
        // 通知客户端链接建立成功
        String str = "服务端向客户端发送消息：与客户端链接建立成功" + " ，链接信息：" + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        // 向客户端发送消息
        ctx.writeAndFlush(str);
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("客户端断开链接{}", ctx.channel().localAddress().toString());
    }

    /**
     * 接收客户端发来的数据
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 接收msg消息
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收到消息：" + msg);
        // 通知客户端，消息已成功发送到服务端
        String str = "服务端已经收到：" + new Date() + " " + msg + "\r\n";
        ctx.writeAndFlush(str);

        // 如果Handler处理器有一些长时间的业务处理，可以交给taskQueue异步处理
        // 获取到线程池eventLoop，添加线程，执行
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // 长时间操作，不至于长时间的业务操作导致Handler阻塞
//                    Thread.sleep(1000);
//                    System.out.println("长时间的业务处理");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        // scheduleTaskQueue延时任务队列
        // 延时任务队列和上面介绍的任务队列非常相似，只是多了一个可延迟一定时间再执行的设置
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //长时间操作，不至于长时间的业务操作导致Handler阻塞
//                    Thread.sleep(1000);
//                    System.out.println("长时间的业务处理");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        },5, TimeUnit.SECONDS);//5秒后执行
    }

    /**
     * 服务端收到消息后操作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 发送消息给客户端
        ctx.writeAndFlush("服务端已收到消息，并给你发送一个问号?");
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常，关闭通道
        ctx.close();
        logger.info("异常信息：\r\n" + cause.getMessage());
    }

}
