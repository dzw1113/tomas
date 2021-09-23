package io.github.dzw1113.agent.client;


import java.util.logging.Logger;

import io.github.dzw1113.common.model.ProtocolMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/18 17:39
 **/
public class MsgpackDecoderHandler extends ChannelHandlerAdapter {
    
    private static final Logger logger = Logger.getLogger(MsgpackDecoderHandler.class.getName());
    
    
    public MsgpackDecoderHandler() {
    
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage) msg;//Convert Object to ByteBuf
        logger.info("服务器返回应答: " + message.getBody());
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    
}