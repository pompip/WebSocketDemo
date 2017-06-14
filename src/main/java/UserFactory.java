import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shff on 2017/6/14.
 */
public class UserFactory {
    private static UserFactory userFactory;
    //    private ConcurrentHashMap<String,Session> hashMap =new ConcurrentHashMap<>();
    private Map<String, Session> userMap = Collections.synchronizedMap(new HashMap<>());


    public static UserFactory newInstance() {
        if (userFactory == null) {
            userFactory = new UserFactory();
        }
        return userFactory;
    }

    public void addUser(String userId, Session session) {
        userMap.put(userId, session);

    }

    public void removeUser(String userId) {
        userMap.remove(userId);
    }

    ;

    public List<Session> getAllUserSession() {
        return new ArrayList<>(userMap.values());
    }

    public Session getUserSession(String userId){
        return userMap.get(userId);
    }

}
