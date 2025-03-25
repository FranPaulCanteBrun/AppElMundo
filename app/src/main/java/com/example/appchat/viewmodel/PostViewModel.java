package com.example.appchat.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import com.example.appchat.model.Post;
import com.example.appchat.providers.PostProvider;
import com.parse.ParseObject;

import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<String> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;
    private final MutableLiveData<List<ParseObject>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public PostViewModel() {
        posts = new MutableLiveData<>();
        postProvider = new PostProvider();
    }

    public LiveData<String> getPostSuccess() {
        return postSuccess;
    }

    public LiveData<String> publicar(Post post) {
        MutableLiveData<String> resultLiveData = new MutableLiveData<>();

        postProvider.addPost(post)
                .observeForever(result -> {
                    postSuccess.setValue(result);
                    resultLiveData.setValue(result);
                });

        return resultLiveData;
    }


    public LiveData<List<Post>> getAllPosts() {
        posts = postProvider.getAllPosts();
        return posts;
    }

    public LiveData<List<Post>> getPostsByCurrentUser() {
        posts = postProvider.getPostsByCurrentUser();
        return posts;
    }

}


