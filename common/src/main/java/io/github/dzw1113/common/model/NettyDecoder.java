package io.github.dzw1113.common.model;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 16:32
 **/
public class NettyDecoder extends ByteToMessageDecoder {
    
    private Class<?> genericClass;
    
    public NettyDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }
    
    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        
        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);
    }
    
//    public static class RpcEncoder extends MessageToByteEncoder<Object> {
//
//        private Class<?> genericClass;
//
//        public RpcEncoder(Class<?> genericClass) {
//            this.genericClass = genericClass;
//        }
//
//        @Override
//        public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
//            if (genericClass.isInstance(in)) {
//                byte[] data = SerializationUtil.serialize(in);
//                out.writeInt(data.length);
//                out.writeBytes(data);
//            }
//        }
//    }
}