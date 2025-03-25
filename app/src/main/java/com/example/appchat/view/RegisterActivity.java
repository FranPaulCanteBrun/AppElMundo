package com.example.appchat.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.appchat.databinding.ActivityRegisterBinding;
import com.example.appchat.model.User;
import com.example.appchat.util.Validaciones;
import com.example.appchat.viewmodel.RegisterViewModel;
public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        viewModel.getRegisterResult().observe(this, result -> {
            if (result != null) {
                showToast(result);

                if (result.equals("Usuario registrado exitosamente")) {
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }, 1500);
                }
            }
        });

        manejarEventos();
    }


    private void manejarEventos() {
        // Evento volver a login
        binding.circleImageBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarRegistro();
            }
        });
    }

    private void realizarRegistro() {
        String usuario = binding.itUsuario.getText().toString().trim();
        String email = binding.itEmail.getText().toString().trim();
        String pass = binding.itPassword.getText().toString().trim();
        String pass1 = binding.itPassword1.getText().toString().trim();
        // Validaciones de entrada
        if (!Validaciones.validarTexto(usuario)) {
            showToast("Usuario incorrecto");
            return;
        }
        if (!Validaciones.validarMail(email)) {
            showToast("El correo no es válido");
            return;
        }
        String passError = Validaciones.validarPass(pass, pass1);
        if (passError != null) {
            showToast(passError);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(usuario);
        user.setPassword(pass);
        Log.d("RegisterActivity", "Usuario registrado: " + usuario + ", Email: " + email+" pass: "+pass);
        viewModel.register(user);
    }

    private void showToast(String message) {
        if (message != null) {
            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}
