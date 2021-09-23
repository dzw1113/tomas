package io.github.dzw1113.common.util;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.dzw1113.common.model.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description:消息加密
 * @author: dzw
 * @date: 2021/09/18 17:36
 **/
public class MsgpackEncoder extends MessageToByteEncoder<Object> {
    
    private static final Logger logger = LoggerFactory.getLogger(MsgpackEncoder.class);
    
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {
        if (msg != null) {
            ProtocolMessage message = (ProtocolMessage) msg;
            ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
            byteBuf.writeBytes(objectMapper.writeValueAsBytes(message));
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("连接异常", cause);
    }
    
}