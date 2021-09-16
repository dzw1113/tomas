package io.github.dzw1113.agent.client;


import java.util.Date;
import java.util.Scanner;

import io.github.dzw1113.agent.AgentApplication;
import io.github.dzw1113.common.model.NettyDecoder;
import io.github.dzw1113.common.model.NettyEncoder;
import io.github.dzw1113.common.model.SocketRequest;
import io.github.dzw1113.common.model.SocketResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description:注入客户端
 * @author: dzw
 * @date: 2021/09/16 13:35
 **/
public class NettyClient extends SimpleChannelInboundHandler<SocketResponse> {
    
    private final String host;
    private final int port;
    private Channel channel;
    
    //连接服务端的端口号地址和端口号
    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NettyEncoder(SocketRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new NettyDecoder(SocketResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(NettyClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_TIMEOUT, 1000)
                    .option(ChannelOption.SO_KEEPALIVE, true);
        
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(new SocketRequest()).sync();
            channel = future.channel();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public Channel getChannel() {
        return channel;
    }
    
    public static void main(String[] args) throws Exception {
        NettyClient nettyClient = new NettyClient("127.0.0.1",12345);
        nettyClient.start();
        System.out.println("启动成功");
        nettyClient.channel.writeAndFlush(new SocketRequest());
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SocketResponse socketResponse) throws Exception {
        System.out.println(socketResponse);
    }
    
}
