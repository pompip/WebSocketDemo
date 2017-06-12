import sun.rmi.runtime.Log;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shff on 2017/5/10.
 */
@ServerEndpoint(value = "/echo")
public class MyWebSocket {

    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    /**
     * Callback hook for Connection open events. This method will be invoked
     * when a client requests for a WebSocket connection.
     *
     * @param session the session which is opened.
     */
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);

        System.out.println("onpen");

    }

    /**
     * Callback hook for Connection close events. This method will be invoked
     * when a client closes a WebSocket connection.
     *
     * @param session the session which is opened.
     */
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("onclose");
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a
     * client send a message.
     *
     * @param message The text message
     * @param session The session of the client
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message Received: " + message);
        for (Session remote : sessions) {
            System.out.println("Sending to " + remote.getId());
            remote.getAsyncRemote().sendText(message);
        }
        System.out.println(sessions.size());
    }
}
