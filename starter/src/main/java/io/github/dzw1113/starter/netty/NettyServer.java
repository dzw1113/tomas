package io.github.dzw1113.starter.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.util.ChannelCache;
import io.github.dzw1113.common.util.MsgpackDecoder;
import io.github.dzw1113.common.util.MsgpackEncoder;
import io.github.dzw1113.starter.netty.handler.CloseChannelAdapter;
import io.github.dzw1113.starter.netty.handler.HeartBeatRespHandlerAdapter;
import io.github.dzw1113.starter.netty.handler.MsgpackServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 13:16
 **/
public class NettyServer {
    
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);
    
    private static final Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
    private final int port;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    
    public NettyServer(int port) {
        this.port = port;
    }
    
    public static void main(String[] args) throws Exception {
        try {
            System.out.println(sun.misc.VM.isBooted());
            new Thread(() -> {
                try {
                    new NettyServer(12345).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            log.error("netty创建失败！", e);
        }
    }
    
    public static Map<String, Channel> getMap() {
        return map;
    }
    
    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelCache.addSession(socketChannel);
                            //用于处理半包
                            socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            socketChannel.pipeline().addLast(new MsgpackDecoder());
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast(new MsgpackEncoder());
                            //
//                            socketChannel.pipeline().addLast("readTimeOutHandler", new ReadTimeoutHandler(30, TimeUnit.MILLISECONDS));
                            socketChannel.pipeline().addLast("heartBeatResp", new HeartBeatRespHandlerAdapter());//心跳链
                            socketChannel.pipeline().addLast("closeChannelAdapter", new CloseChannelAdapter());//关闭链路时释放资源
                            socketChannel.pipeline().addLast(new MsgpackServerHandler());
                        }
    
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            ChannelCache.addSession(ctx.channel());
                            super.exceptionCaught(ctx, cause);
                        }
                        
                        @Override
                        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                            super.handlerRemoved(ctx);
                        }
                        
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ChannelCache.addSession(ctx.channel());
                            super.channelActive(ctx);
                        }
                        
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            
            
            ChannelFuture future = bootstrap.bind("127.0.0.1", port).sync();
            log.info("server 启动成功绑定端口： {}", port);
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    public void stop() {
        try {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}