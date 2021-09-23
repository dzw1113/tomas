package io.github.dzw1113.agent.client;


import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import io.github.dzw1113.common.model.MessageStatusEnum;
import io.github.dzw1113.common.model.MessageTypeEnum;
import io.github.dzw1113.common.model.ProtocolMessage;
import io.github.dzw1113.common.util.MessageUtil;
import io.github.dzw1113.common.util.MsgpackDecoder;
import io.github.dzw1113.common.util.MsgpackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @description:注入客户端
 * @author: dzw
 * @date: 2021/09/16 13:35
 **/
public class Client {
    
    private static final Logger log = Logger.getLogger(Client.class.getName());
    
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    /**
     * 防并发锁,因为是单例模式，所以只有一个实体类，粒度不用整个类
     */
    private static final Object clientLocker = new Object();
    private static volatile Client instance;
    /**
     * 客户端是否关闭
     */
    private final boolean isStop = true;
    EventLoopGroup group = new NioEventLoopGroup();
    Channel channel;
    
    private Client() {
    
    }
    
    /**
     * 获得客户端实例
     */
    public static Client getInstance() {
        if (instance == null) {
            synchronized (clientLocker) {
                if (instance == null) {
                    instance = new Client();
                }
            }
        }
        return instance;
    }
    
    public static void main(String[] args) {
        Client client = Client.getInstance();
        client.startConnect();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            String text = sc.next();
            ProtocolMessage pm = MessageUtil.getProtocolMessage(text, MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
            client.channel.writeAndFlush(pm);
        }
        
    }
    
    /**
     * 连接消息服务器
     */
    public void startConnect() throws RuntimeException {
        new Thread(() -> connect("127.0.0.1", 12345)).start();
    }
    
    /**
     * 连接服务端
     *
     * @param host
     * @param port
     * @throws RuntimeException
     */
    public void connect(String host, int port) throws RuntimeException {
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel socketChannel) {
                            //用于处理半包
                            socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            socketChannel.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());

//                            心跳handler
                            socketChannel.pipeline().addLast("HeartBeatReqHandler", new HeartBeatReqHandler());
                            //消息Handler
                            socketChannel.pipeline().addLast("messageBusinessHandler", new MsgpackDecoderHandler());
                        }
                    });
            ChannelFuture cf = bs.connect(host, port).sync();
            cf.addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    channel = channelFuture.channel();
                    log.info("连接成功");
                } else {
                    log.info("连接失败");
                }
            });
            cf.channel().closeFuture().sync();
            log.info("服务器关闭连接");
        } catch (Exception e) {
            throw new RuntimeException("服务器连接异常");
        }
    }
    
    
    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMessage(Channel channel, String msg) {
        if (channel == null) {
            log.warning("未连接服务器!");
            return;
        }
        ProtocolMessage pm = MessageUtil.getProtocolMessage(msg, MessageTypeEnum.MESSAGE_BUSSINESS, MessageStatusEnum.REQUEST);
        MessageUtil.sendMessage(channel, pm);
    }
    
    /**
     * 关闭连接
     */
    public void closeConnection() throws RuntimeException {
        if (!isStop) {
            synchronized (clientLocker) {
                if (!isStop) {
                    group.shutdownGracefully();
                }
            }
        }
    }
    
}
