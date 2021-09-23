package io.github.dzw1113.starter.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.util.ChannelCache;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.ReadTimeoutException;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/18 18:04
 **/
public class CloseChannelAdapter extends ChannelHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(CloseChannelAdapter.class);
    
    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        //客户端非正常退出时，需要手动关闭句柄
        log.info("客户端非正常退出，移除缓存数据");
        ChannelCache.removeSession(ctx.channel().id().asLongText());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            log.info("指定时间内未操作,退出!");
        }
        super.exceptionCaught(ctx, cause);
    }
    
}
