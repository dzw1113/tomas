package io.github.dzw1113.common.model;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 16:36
 **/
public class NettyEncoder extends MessageToByteEncoder<Object> {
    
    private Class<?> genericClass;
    
    public NettyEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }
    
    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}