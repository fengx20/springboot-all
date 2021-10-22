package com.fengx.bootlog.netty.server;

import com.fengx.bootlog.codec.ObjDecoder;
import com.fengx.bootlog.codec.ObjEncoder;
import com.fengx.bootlog.entity.TransportProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author: Fengx
 * @date: 2021-09-17
 * @description: 通道
 **/
@Service
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private MyServerHandler myServerHandler;

    @Override
    protected void initChannel(SocketChannel channel) {
        // 对象传输处理
        channel.pipeline().addLast(new ObjDecoder(TransportProtocol.class));
        channel.pipeline().addLast(new ObjEncoder(TransportProtocol.class));
        // 基于换行符号
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        // 在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(myServerHandler);
    }

}
