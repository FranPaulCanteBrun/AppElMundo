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
                Log.d("RegisterViewModel", "Respuesta recibida: " + objectId);

                if (objectId != null && objectId.equals("error_email_exist")) {
                    registerResult.setValue("Ese correo ya está registrado");
                } else if (objectId != null) {
                    registerResult.setValue("Usuario registrado exitosamente");
                } else {
                    registerResult.setValue("Error durante el registro");
                }

                result.removeObserver(this);
            }
        });
    }

}







