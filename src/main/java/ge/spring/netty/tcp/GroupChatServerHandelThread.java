package ge.spring.netty.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * @author dengzhipeng
 * @version 1.0
 * @date 2020/5/29 0029
 */
public class GroupChatServerHandelThread extends SimpleChannelInboundHandler<String> {

//    private static List<Channel> channels = new ArrayList<Channel>();


    public Map<String, Channel> channels;

    public GroupChatServerHandelThread(Map<String, Channel> channels){
        this.channels = channels;
    }


    // 定义一个chnnel组，管理channel
    // GlobalEventExecutor.INSTANCE 是全局的事件执行器，单例模式
    public static ChannelGroup channelsGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * handlerAdded 表示连接建立，一旦连接，第一个被执行
     * 将当前 channel 加入到 channelGroup
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        super.handlerAdded(ctx);
        Channel channel = ctx.channel();
        // 改方法会遍历 channelsGroup 所有客户端，并发送信息
        channelsGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + sdf.format(new Date()) + "\n");
        channelsGroup.add(channel);

        String channelId = ctx.pipeline().channel().id().asShortText();
        channels.put(channelId, channel);
    }

    /**
     * 断开连接，将 xx 客户离开信息推送给当前在线用户
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        super.handlerRemoved(ctx);
        Channel channel = ctx.channel();
        channelsGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");

        String channelId = ctx.pipeline().channel().id().asShortText();
        channels.remove(channelId, channel);

    }



    /**
     * 表示 channel 处于活动状态，提示 xxx 上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        System.out.println(ctx.channel().remoteAddress() + " 上线了 ");
    }

    /**
     * 表示 channel 处于不活动状态， 提示 xx 离线了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        System.out.println(ctx.channel().remoteAddress() + " 离线了 ");

    }

    /**
     * 读取数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelsGroup.forEach(ch ->{
            // 非当前用户
            if (channel != ch){
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送消息" + msg + "\n");
            }else {
                //  显示自己发送的消息
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });

        // 把数据塞入队列的处理 塞入数据库？
        System.out.println(Thread.currentThread().getName());

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
