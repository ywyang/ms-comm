package demo.ms.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.Session;

@Getter
@Setter
@AllArgsConstructor
public class WebSocketContext {
    private Session session;
    private JwtUserInfo userInfo;
}
