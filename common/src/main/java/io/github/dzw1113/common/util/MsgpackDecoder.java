package io.github.dzw1113.common.util;

import java.util.List;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.dzw1113.common.model.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/18 17:35
 **/
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    
    private static final Logger logger = LoggerFactory.getLogger(MsgpackDecoder.class);
    
    /**
     * 解码
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        ProtocolMessage message = objectMapper.readValue(array, new TypeReference<ProtocolMessage>() {
        });
        list.add(message);
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("连接异常", cause);
        
    }
    
}
