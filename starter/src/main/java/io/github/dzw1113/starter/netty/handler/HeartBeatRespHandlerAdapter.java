package io.github.dzw1113.starter.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.model.Header;
import io.github.dzw1113.common.model.MessageStatusEnum;
import io.github.dzw1113.common.model.MessageTypeEnum;
import io.github.dzw1113.common.model.ProtocolMessage;
import io.github.dzw1113.common.util.ChannelCache;
import io.github.dzw1113.common.util.MessageUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/22 16:00
 **/
public class HeartBeatRespHandlerAdapter extends ChannelHandlerAdapter {
    
    private static final Logger log = LoggerFactory.getLogger(HeartBeatRespHandlerAdapter.class);
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage message = (ProtocolMessage) msg;
        Header header = message.getHeader();
        
        if((header != null) && (header.getType() == MessageTypeEnum.HEART_BEAT_REQ.getMessageCode())){
            log.info("收到客户端的心跳请求");
            ProtocolMessage pm =  MessageUtil.getProtocolMessage(MessageTypeEnum.HEART_BEAT_RES, MessageStatusEnum.REQUEST);
            MessageUtil.sendMessage(ctx, pm);
        }
        else{
            ctx.fireChannelRead(msg);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
    
}
