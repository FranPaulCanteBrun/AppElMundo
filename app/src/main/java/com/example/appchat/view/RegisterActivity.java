package com.example.appchat.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.appchat.R;
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
        viewModel.getRegisterResult().observe(this, result -> showToast(result));

        manejarEventos();
        setupListenersIndividuales(); // <- validación campo por campo
    }

    private void manejarEventos() {
        binding.circleImageBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.btRegistrar.setOnClickListener(v -> realizarRegistro());
    }

    private void realizarRegistro() {
        String usuario = binding.itUsuario.getText().toString().trim();
        String email = binding.itEmail.getText().toString().trim();
        String pass = binding.itPassword.getText().toString().trim();
        String pass1 = binding.itPassword1.getText().toString().trim();

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
        viewModel.register(user);
    }

    private void showToast(String message) {
        Log.d("TOAST_DEBUG", "Mensaje recibido: " + message);
        if (message != null && !message.isEmpty()) {
            if (message.equalsIgnoreCase("Registro exitoso")) {
                new AlertDialog.Builder(this)
                        .setTitle("🎉 Registro Exitoso")
                        .setMessage("¡Bienvenido a El Mundo! Ya podés iniciar sesión.")
                        .setCancelable(false)
                        .setPositiveButton("Ir al login", (dialog, which) -> {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .show();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
        }
    }



    private void setupListenersIndividuales() {
        Drawable errorIcon = ContextCompat.getDrawable(this, R.drawable.ic_error);
        if (errorIcon != null) {
            errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
        }

        binding.itUsuario.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                boolean valido = Validaciones.validarTexto(s.toString());
                binding.inputLayoutUsuario.setError(valido ? null : "Mínimo 4 caracteres");
                binding.inputLayoutUsuario.setErrorIconDrawable(valido ? null : errorIcon);
                verificarFormulario();
            }
        });

        binding.itEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                boolean valido = Validaciones.validarMail(s.toString());
                binding.inputLayoutEmail.setError(valido ? null : "Correo inválido");
                binding.inputLayoutEmail.setErrorIconDrawable(valido ? null : errorIcon);
                verificarFormulario();
            }
        });

        binding.itPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                boolean valido = s.length() >= 6;
                binding.inputLayoutPassword.setError(valido ? null : "Mínimo 6 caracteres");
                binding.inputLayoutPassword.setErrorIconDrawable(valido ? null : errorIcon);
                verificarFormulario();
            }
        });

        binding.itPassword1.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                String pass = binding.itPassword.getText().toString();
                boolean valido = s.toString().equals(pass);
                binding.inputLayoutPassword1.setError(valido ? null : "Las contraseñas no coinciden");
                binding.inputLayoutPassword1.setErrorIconDrawable(valido ? null : errorIcon);
                verificarFormulario();
            }
        });
    }

    private void verificarFormulario() {
        String usuario = binding.itUsuario.getText().toString().trim();
        String email = binding.itEmail.getText().toString().trim();
        String pass = binding.itPassword.getText().toString().trim();
        String pass1 = binding.itPassword1.getText().toString().trim();

        boolean habilitar = Validaciones.validarTexto(usuario)
                && Validaciones.validarMail(email)
                && Validaciones.validarPass(pass, pass1) == null;

        binding.btRegistrar.setEnabled(habilitar);
    }

    public abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
