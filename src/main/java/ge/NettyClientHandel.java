package ge;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 *
 */
public class NettyClientHandel extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        System.out.println("client" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("client hello 666666", CharsetUtil.UTF_8));
    }


    /**
     * 读取客户端数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("接受服务器消息 = " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址 = " + ctx.channel().remoteAddress());
    }
//
//    /**
//     * 读取完成
//     * @param ctx
//     * @throws Exception
//     */
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
////        super.channelReadComplete(ctx);
//
//        // 将数据写入缓存，并且刷新
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hellow", CharsetUtil.UTF_8));
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
////        super.exceptionCaught(ctx, cause);
//        ctx.channel().close();
//
//    }

}
