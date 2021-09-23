package io.github.dzw1113.agent.client;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.model.Header;
import io.github.dzw1113.common.model.MessageStatusEnum;
import io.github.dzw1113.common.model.MessageTypeEnum;
import io.github.dzw1113.common.model.ProtocolMessage;
import io.github.dzw1113.common.util.MessageUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/20 22:21
 **/
public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HeartBeatReqHandler.class);
    private final Object heartBeatLocker = new Object();
    private volatile ScheduledFuture<?> heartBeat;
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ProtocolMessage message = (ProtocolMessage) msg;
        //返回心跳应答消息
        Header header = message.getHeader();
        //服务器发回的验证成功
        if ((header != null)
                && (header.getType() == MessageTypeEnum.AUTH_CHANNEL_RES.getMessageCode()) ) {
            if (heartBeat == null) {//避免开启两次的心跳定时器
                synchronized (heartBeatLocker) {
                    if (heartBeat == null) {
                        heartBeat = ctx.executor().scheduleAtFixedRate(
                                new HeartBeatReqHandler.HeartBeatTask(ctx)
                                , 500, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
                    }
                }
            }
            log.debug("认证成功启动心跳定时器");
        } else if ((header != null) && (header.getType() == MessageTypeEnum.HEART_BEAT_RES.getMessageCode())) {
            log.debug("收到服务端的心跳回复");
        } else {
            ctx.fireChannelRead(msg);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
    
    /**
     * 心跳TASK
     */
    public class HeartBeatTask implements Runnable {
        
        private final Logger log = LoggerFactory.getLogger(HeartBeatTask.class);
        private ChannelHandlerContext ctx;
        
        HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        public void run() {
            ProtocolMessage msg = MessageUtil.getProtocolMessage(MessageTypeEnum.HEART_BEAT_REQ, MessageStatusEnum.REQUEST);
            MessageUtil.sendMessage(ctx,msg);
            log.debug("客户端发起心跳请求");
        }
        
    }
    
}
