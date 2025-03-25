package com.example.appchat.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appchat.model.Chat;
import com.example.appchat.providers.ChatsProvider;

import java.util.List;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<List<Chat>> chatListLiveData = new MutableLiveData<>();
    private ChatsProvider chatsProvider;

    public ChatViewModel() {
        chatsProvider = new ChatsProvider();
    }

    public LiveData<List<Chat>> getContactos() {
        return chatListLiveData;
    }

    public void cargarContactos(String emisorId) {
        chatsProvider.traerContactos(emisorId, new ChatsProvider.ChatCallback() {
            @Override
            public void onChatsLoaded(List<Chat> chatList) {
                chatListLiveData.postValue(chatList);
            }
            @Override
            public void onError(com.parse.ParseException e) {
                Log.e("ChatViewModel", "Error al cargar contactos", e);
            }
        });
    }
}

