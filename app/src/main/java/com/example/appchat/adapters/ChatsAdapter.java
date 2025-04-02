package com.example.appchat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appchat.R;
import com.example.appchat.model.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private final List<Chat> chatList;
    private final Context context;
    private final OnChatClickListener listener;
    public ChatsAdapter(Context context, List<Chat> chatList, OnChatClickListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        if (chat == null) {
            Log.e("ChatAdapter", "Chat en la posición " + position + " es null");
            return;
        }
        if (chat.getReceptor() == null) {
            Log.e("ChatAdapter", "El emisor del chat en la posición " + position + " es null");
            holder.nombreTextView.setText("Usuario desconocido");
            holder.circleImageView.setImageResource(R.drawable.ic_person);
        } else {
            Log.d("ChatAdapter", "Emisor: " + chat.getReceptor() .getUsername());
            String fotoUrl = chat.getReceptor().getString("foto_perfil");
            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                Picasso.get()
                        .load(fotoUrl)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(holder.circleImageView);
            } else {
                holder.circleImageView.setImageResource(R.drawable.ic_person);
            }
            holder.nombreTextView.setText(chat.getReceptor().getUsername());
        }
        holder.mensajeTextView.setText(chat.getMensaje() != null ? chat.getMensaje() : "Mensaje no disponible");
        holder.itemView.setOnClickListener(v -> listener.onChatClick(chat));
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public final TextView mensajeTextView;
        public final TextView nombreTextView;
        public final ImageView circleImageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mensajeTextView = itemView.findViewById(R.id.mensajeTextView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            circleImageView = itemView.findViewById(R.id.circleImageView);
        }
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }
}

