package io.github.dzw1113.common.util;

import io.github.dzw1113.common.model.Header;
import io.github.dzw1113.common.model.MessageStatusEnum;
import io.github.dzw1113.common.model.MessageTypeEnum;
import io.github.dzw1113.common.model.ProtocolMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/18 17:24
 **/
public class MessageUtil {
    
    
    /**
     * 生成一个消息包，不需要验证串，一般服务器使用用于返回数据
     * @param msg
     * @param messageType
     * @param messateStatus
     * @return
     */
    public static ProtocolMessage getProtocolMessage(String msg
            , MessageTypeEnum messageType, MessageStatusEnum messateStatus){
        ProtocolMessage message = new ProtocolMessage(getHeader(messageType, messateStatus), msg);
        return message;
    }
    
    
    public static ProtocolMessage getProtocolMessage(MessageTypeEnum messageType, MessageStatusEnum messateStatus){
        return getProtocolMessage(messageType, messateStatus);
    }
    
    public static Header getHeader(MessageTypeEnum messageType, MessageStatusEnum messateStatus){
        Header header = new Header();
        header.setType(messageType.getMessageCode());
        header.setLength(0);
        header.setSessionID(0);
        return header;
    }
    
    public static void sendMessage(ChannelHandlerContext ctx, ProtocolMessage pm) {
        sendMessage(ctx.channel(),pm);
    }
    
    public static void sendMessage(Channel channel, ProtocolMessage pm) {
        channel.writeAndFlush(pm);
    }
    
}
