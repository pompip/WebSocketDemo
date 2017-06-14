import bean.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Created by shff on 2017/5/10.
 */
@ServerEndpoint(value = "/echo")
public class MyWebSocket {
    private String userId;
    private UserFactory userFactory;

//    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    /**
     * Callback hook for Connection open events. This method will be invoked
     * when a client requests for a WebSocket connection.
     *
     * @param session the session which is opened.
     */
    @OnOpen
    public void onOpen(Session session) {
//        sessions.add(session);
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        userId = requestParameterMap.get("userId").get(0);
        System.out.println("onOpen,userId:" + userId);
        userFactory = UserFactory.newInstance();
        userFactory.addUser(userId, session);
    }

    /**
     * Callback hook for Connection close events. This method will be invoked
     * when a client closes a WebSocket connection.
     *
     * @param session the session which is opened.
     */
    @OnClose
    public void onClose(Session session) {
//        sessions.remove(session);
        System.out.println("onClose,UserId:" + userId);

        userFactory.removeUser(userId);
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
        Gson gson = new GsonBuilder().create();
        ChatMessage chatMessage;
        try {
            chatMessage = gson.fromJson(message, ChatMessage.class);
        } catch (JsonSyntaxException e) {
            session.getAsyncRemote().sendText(message);
            return;
        }


            if ("all".equals(chatMessage.to)) {
                for (Session remote : userFactory.getAllUserSession()) {
                    System.out.println("Sending to " + remote.getId());
                    remote.getAsyncRemote().sendText(message);
                }
            } else {
                Session userSession = userFactory.getUserSession(chatMessage.to);
                ChatMessage toChatMessage = new ChatMessage();
                toChatMessage.to = chatMessage.from;
                if (userSession == null) {
                    toChatMessage.from = "System";
                    toChatMessage.message = "用户不在线";
                    session.getAsyncRemote().sendText(gson.toJson(toChatMessage));
                } else {
                    toChatMessage.from = userId;
                    toChatMessage.message = chatMessage.message;
                    userSession.getAsyncRemote().sendText(gson.toJson(chatMessage));
                }

        }


    }


    @OnMessage
    public void onMessage(ByteBuffer message, Session session) {
        System.out.println("receive img:" + message.array().length);


    }


    @OnError
    public void onError(Throwable throwable, Session session) {
        throwable.printStackTrace();

    }
}
