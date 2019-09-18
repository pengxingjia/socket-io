package im.qingtui.socket.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息处理类
 *
 * @author qiaofeng
 */
@Component
@Slf4j
public class MsgEventHandler {

    public static Map<String, UUID> map = new HashMap<>();

    private final SocketIOServer server;

    @Autowired
    public MsgEventHandler(SocketIOServer server) {
        this.server = server;
    }

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (client != null) {
            String openid = client.getHandshakeData().getSingleUrlParam("openid");
            String applicationId = client.getHandshakeData().getSingleUrlParam("appid");
            log.info("连接成功, applicationId=" + applicationId + ", openid=" + openid +
                ", sessionId=" + client.getSessionId().toString() );
            client.joinRoom(applicationId);
            map.put(openid, client.getSessionId());

        } else {
            log.error("客户端为空");
        }
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String openid = client.getHandshakeData().getSingleUrlParam("openid");
        log.info("客户端断开连接, openid=" + openid + ", sessionId=" + client.getSessionId().toString());

        map.remove(openid);
        client.disconnect();
    }

    // 消息接收入口
    @OnEvent(value = "message")
    public void onEvent(SocketIOClient client, AckRequest ackRequest, Object data) {
        String openid = client.getHandshakeData().getSingleUrlParam("openid");

        log.info("openid:{}接收到客户端消息{}", openid, data);
        boolean isAck = ackRequest.isAckRequested();
        if (isAck) {
            // send ack response with data to client
            ackRequest.sendAckData("服务器回答Socket.EVENT_MESSAGE", "好的");
        }
    }

    // 广播消息接收入口
    @OnEvent(value = "broadcast")
    public void onBroadcast(SocketIOClient client, AckRequest ackRequest, Object data) {
        log.info("接收到广播消息");
        // 房间广播消息
        String room = client.getHandshakeData().getSingleUrlParam("appid");
        server.getRoomOperations(room).sendEvent("broadcast", "广播啦啦啦啦");
        if (ackRequest.isAckRequested()){
            ackRequest.sendAckData("服务器回答Socket.EVENT_MESSAGE", "好的");
        }
    }

}