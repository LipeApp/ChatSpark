import api.User;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.*;

public class Chat {
    static Map<Session, User> userUsernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1; //Used for creating the next username
    public static void main(String[] args) {
        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        webSocket("/chat", ChatWebSocketHandler.class);
        init();
    }

    //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                User u = userUsernameMap.get(session);
                if (!u.getName().equals(sender)){
                    session.getRemote().sendString(String.valueOf(new JSONObject()
                            .put("userMessage", message)
                            .put("userlist", userUsernameMap.values())
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
