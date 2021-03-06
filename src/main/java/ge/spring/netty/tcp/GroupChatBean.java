package ge.spring.netty.tcp;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * @author dengzhipeng
 * @version 1.0
 * @date 2020/5/29 0029
 */
@Component
public class GroupChatBean {

    /**
     * 该Map装载了所有 tcp的连接
     * k -> 连接的 channelId
     * v -> 连接的 channel
     */
    public Map<String, Channel> channels = new ConcurrentHashMap<>(64);

}
