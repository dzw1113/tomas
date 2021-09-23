package io.github.dzw1113.starter.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.model.ProtocolMessage;
import io.github.dzw1113.common.util.MessageUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description:服务端消息处理
 * @author: dzw
 * @date: 2021/09/18 17:59
 **/
public class MsgpackServerHandler extends ChannelHandlerAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(MsgpackServerHandler.class);
    
    /**
     * 消息业务处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage) msg;
        message.setBody("答应:" + message.getBody());
        MessageUtil.sendMessage(ctx, message);
        //=================消息最终消费者，不再向后传递========================
    }
    
    /**
     * invoke when channel read data completely
     * then the channel will flush the data
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    
}
