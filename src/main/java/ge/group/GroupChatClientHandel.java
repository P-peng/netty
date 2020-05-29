package ge.group;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author dengzhipeng
 * @version 1.0
 * @date 2020/5/29 0029
 */
public class GroupChatClientHandel extends SimpleChannelInboundHandler<String> {


    /**
     * 读取数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());
    }

}
