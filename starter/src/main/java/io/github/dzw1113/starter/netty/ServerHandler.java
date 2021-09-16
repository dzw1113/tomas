package io.github.dzw1113.starter.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.model.SocketRequest;
import io.github.dzw1113.common.model.SocketResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:入站接收处理
 * @author: dzw
 * @date: 2021/09/16 13:54
 **/
public class ServerHandler extends SimpleChannelInboundHandler<SocketRequest> {
    
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
    
    @Override
    public void channelRead0(final ChannelHandlerContext ctx, SocketRequest request) throws Exception {
        SocketResponse response = new SocketResponse();
        System.out.println("读取：" + request);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server caught exception", cause);
        ctx.close();
    }
}
