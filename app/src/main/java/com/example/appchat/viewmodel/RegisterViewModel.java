package com.example.appchat.viewmodel;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.example.appchat.model.User;
import com.example.appchat.providers.AuthProvider;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> registerResult = new MutableLiveData<>();
    private final AuthProvider authProvider;


    public RegisterViewModel() {
        this.authProvider = new AuthProvider();
    }


    public LiveData<String> getRegisterResult() {
        return registerResult;
    }


    public void register(User user) {

        LiveData<String> result = authProvider.signUp(user);


        result.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String objectId) {
                if (objectId != null) {

                    registerResult.setValue(objectId);
                    Log.d("RegisterViewModel", "Usuario registrado con ID: " + objectId);
                } else {
                    // Registration failed
                    registerResult.setValue(null);
                    Log.e("RegisterViewModel", "Error durante el registro.");
                }

                result.removeObserver(this);
            }
        });
    }
}







