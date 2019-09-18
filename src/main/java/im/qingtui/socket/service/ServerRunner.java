package im.qingtui.socket.service;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author qiaofeng
 */
@Component
@Order(1)
@Slf4j
public class ServerRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Autowired
    public ServerRunner(SocketIOServer socketIOServer){
        this.server = socketIOServer;
    }


    @Override
    public void run(String... args) {
        log.info("serverRunner 启动了");
        server.start();
    }
}