package io.github.dzw1113.starter.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.dzw1113.common.model.NettyDecoder;
import io.github.dzw1113.common.model.SocketRequest;
import io.github.dzw1113.common.model.SocketResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
        new NettyServer(12345).start();
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
                        public void initChannel(SocketChannel channel) {
                            channel.pipeline().addLast(
                                    /** 将RPC请求进行解码（为了处理请求）*/
                                    new NettyDecoder(SocketRequest.class))
                                    /** 将RPC响应进行编码（为了返回响应）*/
                                    .addLast(new NettyDecoder(SocketResponse.class))
                                    /** 处理RPC请求*/
                                    .addLast(new ServerHandler());
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
