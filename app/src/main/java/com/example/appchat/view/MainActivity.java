package com.example.appchat.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.appchat.databinding.ActivityMainBinding;
import com.example.appchat.util.Validaciones;
import com.example.appchat.viewmodel.MainViewModel;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this) .get(MainViewModel.class);
        manejarEventos();

        binding.itUsuarioLogin.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();

                if (!email.isEmpty()) {
                    if (!Validaciones.validarMail(email)) {
                        binding.inputLayoutEmailLogin.setError("Correo inválido");
                    } else {
                        binding.inputLayoutEmailLogin.setError(null);
                    }
                } else {
                    binding.inputLayoutEmailLogin.setError(null);
                }

                evaluarBoton();
            }
        });


        binding.itPasswordLogin.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String pass = s.toString().trim();

                if (!pass.isEmpty()) {
                    if (pass.length() < 6) {
                        binding.inputLayoutPasswordLogin.setError("Mínimo 6 caracteres");
                    } else {
                        binding.inputLayoutPasswordLogin.setError(null);
                    }
                } else {
                    binding.inputLayoutPasswordLogin.setError(null);
                }

                evaluarBoton();
            }
        });



    }

    private void evaluarBoton() {
        boolean emailValido = binding.inputLayoutEmailLogin.getError() == null &&
                !binding.itUsuarioLogin.getText().toString().trim().isEmpty();

        boolean passValido = binding.inputLayoutPasswordLogin.getError() == null &&
                !binding.itPasswordLogin.getText().toString().trim().isEmpty();

        binding.btLogin.setEnabled(emailValido && passValido);
    }


    @Override
    protected void onStart() {
        super.onStart();
      /*  if (viewModel != null) {
            viewModel.verificarSesionActiva().observe(this, si -> {
                if (Boolean.TRUE.equals(si)) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }*/
    }

    private void manejarEventos() {
        binding.tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.btLogin.setOnClickListener(v -> {
            String email = obtenerTextoSeguro(binding.itUsuarioLogin);
            String pass = obtenerTextoSeguro(binding.itPasswordLogin);

            if (!Validaciones.validarMail(email)) {
                showToast("Email incorrecto");
                return;
            }

            if (!Validaciones.controlarPasword(pass)) {
                showToast("Password incorrecto");
                return;
            }

           viewModel.login(email, pass).observe(this, user_id -> {
                if (user_id != null) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    showToast("Login fallido");
                }
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        limpiarCampos();
    }

    private void limpiarCampos() {
        if (binding != null) {
            binding.itUsuarioLogin.setText("");
            binding.itPasswordLogin.setText("");
        }
    }

    /**
     * Obtiene texto de un campo de texto asegurando que no sea nulo.
     */
    private String obtenerTextoSeguro(EditText editText) {
        if (editText == null) {
            return "";
        }
        return editText.getText().toString().trim();
    }
    private void validarCampos() {
        String email = binding.itUsuarioLogin.getText().toString().trim();
        String pass = binding.itPasswordLogin.getText().toString().trim();

        boolean emailOk = Validaciones.validarMail(email);
        boolean passOk = pass.length() >= 6;

        binding.btLogin.setEnabled(emailOk && passOk);
    }

}
