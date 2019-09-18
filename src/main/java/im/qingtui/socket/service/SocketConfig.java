package im.qingtui.socket.service;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * socket相关配置
 *
 * @author qiaofeng
 */
@Configuration
@PropertySource("classpath:sys.propertise")
public class SocketConfig {

    @Value("${socket.hostname}")
    private String hostname;

    @Value("${socket.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer(){
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        //握手超时时间
        config.setUpgradeTimeout(2000);
        //维持心跳时，客户端发送心跳的时间间隔
        config.setPingInterval(3000);
        //10秒内没有接收到心跳就会发送超事件，断开连接
        config.setPingTimeout(10000);
        //每次连接进来的session都不一样，表示可以一个浏览器开多个窗口，默认为false，通过cookie生成的sessionId，一个浏览器只能连接一个长连接
        config.setRandomSession(true);
        config.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData handshakeData) {
                //默认通过验证，接入socket连接成功
                return true;
            }
        });
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}