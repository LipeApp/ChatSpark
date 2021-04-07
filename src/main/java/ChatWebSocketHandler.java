import api.Message;
import api.User;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Date;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + Chat.nextUserNumber++;
        User u = new User(username, "https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
        Chat.userUsernameMap.put(user, u);

    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        User u = Chat.userUsernameMap.get(user);
        Chat.userUsernameMap.remove(user);
        Chat.broadcastMessage(sender = "Server", msg = new Gson().toJson(new Message(new User("Alisa","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Alisa_Yandex.svg/1200px-Alisa_Yandex.svg.png"),u.getName()+"\tLeft chat", new Date().toString())));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Message msge = new Gson().fromJson(message, Message.class);
        if(msge.getMessage().equals("iimthis")){
            User u = Chat.userUsernameMap.get(user);
            u.setAvatar(msge.getUser().getAvatar());
            u.setName(msge.getUser().getName());
            Chat.userUsernameMap.put(user, u);
            Chat.broadcastMessage(sender = "Server", msg = new Gson().toJson(new Message(new User("Alisa","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Alisa_Yandex.svg/1200px-Alisa_Yandex.svg.png"),u.getName()+"\tJoined chat", new Date().toString())));
        }else {
            Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user).getName(), msg = message);
        }
    }


}