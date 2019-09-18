package im.qingtui.socket.controller;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.BroadcastAckCallback;
import com.corundumstudio.socketio.SocketIOServer;
import im.qingtui.socket.msg.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiaofeng
 */
@RestController
@Slf4j
public class IndexController {

    @Autowired
    SocketIOServer socketIOServer;


    @RequestMapping(value = "msg")
    public void contextLoads(String msg) {
        socketIOServer.getRoomOperations("9999").sendEvent("message", msg, new AckCallback<String>(String.class, 1000) {
            @Override
            public void onSuccess(String result) {
                log.info("反馈信息" + result);
            }
        });
    }
}