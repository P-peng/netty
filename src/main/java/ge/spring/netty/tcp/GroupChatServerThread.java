package ge.spring.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author dengzhipeng
 * @version 1.0
 * @date 2020/5/29 0029
 */
@Service
public class GroupChatServerThread implements Runnable{

    @Resource
    GroupChatBean groupChatBean;

    private int port = 8888;
    private Thread thread;

    public GroupChatServerThread(){
    }

    @PostConstruct
    public void init() {
        thread = new Thread(this);
        thread.start();
    }

    public void run(){
        // 基础双线程组 不指定默认启动 8条线程
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(boosGroup, workGroup)
                    // 通道采用 nio 类型
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            // 加入自己业务得handel
                            pipeline.addLast(new GroupChatServerHandelThread(groupChatBean.channels));
                        }
                    });
            System.out.println("netty 服务器启动");

            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
