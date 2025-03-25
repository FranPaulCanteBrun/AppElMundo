package com.example.appchat.providers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.appchat.model.Chat;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsProvider {

    public interface ChatCallback {
        void onChatsLoaded(List<Chat> chatList);
        void onError(ParseException e);
    }

    public void traerChats(String emisor, String receptor, ChatCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");
        query.whereEqualTo("emisor", ParseObject.createWithoutData("_User", emisor));
        query.whereEqualTo("receptor", ParseObject.createWithoutData("_User", receptor));
        query.addAscendingOrder("createdAt");

        query.findInBackground((mensajes, e) -> {
            if (e == null) {
                List<Chat> chatList = new ArrayList<>();
                for (ParseObject m : mensajes) {
                    chatList.add(parseChat(m));
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onChatsLoaded(chatList));
            } else {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
            }
        });
    }

    public void traerContactos(String emisor, ChatCallback callback) {
        ParseQuery<Chat> queryEmisor = ParseQuery.getQuery(Chat.class);
        queryEmisor.whereEqualTo("emisor", ParseObject.createWithoutData("_User", emisor));

        ParseQuery<Chat> queryReceptor = ParseQuery.getQuery(Chat.class);
        queryReceptor.whereEqualTo("receptor", ParseObject.createWithoutData("_User", emisor));

        List<ParseQuery<Chat>> queries = new ArrayList<>();
        queries.add(queryEmisor);
        queries.add(queryReceptor);
        ParseQuery<Chat> mainQuery = ParseQuery.or(queries);
        mainQuery.include("emisor");
        mainQuery.include("receptor");
        mainQuery.orderByDescending("createdAt");
        mainQuery.findInBackground((mensajes, e) -> {
            Map<String, Chat> contactosMap = new HashMap<>();

            if (e == null) {
                for (ParseObject m : mensajes) {

                    Chat chat = parseChat(m);
                    String userId = chat.getEmisor().getObjectId().equals(queryReceptor)
                            ? chat.getReceptor().getObjectId()
                            : chat.getEmisor().getObjectId();

                    if (!contactosMap.containsKey(userId)) {
                        contactosMap.put(userId, chat);
                    }
                }
            }
            ParseQuery<ParseUser> usersQuery = ParseUser.getQuery();
            usersQuery.whereNotEqualTo("objectId", emisor);
            usersQuery.findInBackground((usuarios, error) -> {
                if (error == null) {
                    for (ParseUser usuario : usuarios) {
                        if (!contactosMap.containsKey(usuario.getObjectId())) {

                            Chat chat = ParseObject.create(Chat.class);
                            ParseUser currentUser = ParseUser.getCurrentUser();
                            if (currentUser != null) {
                                String emisor_ = currentUser.getObjectId();
                                Log.d("ChatDebug", "ID del emisor: " + emisor_);
                            } else {
                                Log.e("ChatError", "El usuario no está autenticado.");
                            }
                            chat.setReceptor(usuario);
                            chat.setMensaje("Sin conversación");
                            contactosMap.put(usuario.getObjectId(), chat);
                        }
                    }
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onChatsLoaded(new ArrayList<>(contactosMap.values())));
            });
        });
    }

    public void enviarMensaje(String emisor, String receptor, String mensaje) {
        ParseObject chatMessage = new ParseObject("Chat");
        chatMessage.put("emisor", ParseObject.createWithoutData("_User", emisor));
        chatMessage.put("receptor", ParseObject.createWithoutData("_User", receptor));
        chatMessage.put("mensaje", mensaje);
        chatMessage.put("isRead", false);
        chatMessage.saveInBackground();
    }

    private Chat parseChat(ParseObject m) {
        Chat chat = ParseObject.createWithoutData(Chat.class, m.getObjectId());
        chat.setEmisor((ParseUser) m.getParseObject("emisor"));
        chat.setReceptor((ParseUser) m.getParseObject("receptor"));
        chat.setMensaje(m.getString("mensaje"));
        return chat;
    }
}

