package ge.group;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author dengzhipeng
 * @version 1.0
 * @date 2020/5/29 0029
 */
public class GroupChatServer {

    private int port;

    private GroupChatServerHandel groupChatServerHandel;

    public GroupChatServer(int port){
        this.port = port;
    }

    public void run(){
        // 基础双线程组 不指定默认启动 8条线程
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(boosGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
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
                            groupChatServerHandel = new GroupChatServerHandel();

                            pipeline.addLast(groupChatServerHandel);
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

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer(8888);
        Thread thread = new Thread(() ->{
            System.out.println("-- 读取输入 --");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                groupChatServer.groupChatServerHandel.toAll();
            }
        });
        thread.start();

        groupChatServer.run();

    }
}
