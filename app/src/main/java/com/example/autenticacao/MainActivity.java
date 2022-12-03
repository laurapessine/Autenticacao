// LAURA E EVANDRO
package com.example.autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtSenha = findViewById(R.id.txtSenha);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCadastrar = findViewById(R.id.btnCadastrar);

        FirebaseAuth usuarios = FirebaseAuth.getInstance();

        Intent intent = new Intent(getApplicationContext(), MediaActivity.class);
        if (usuarios.getCurrentUser() != null) {
            startActivity(intent);
        }


        btnLogin.setOnClickListener(view -> {
            Toast.makeText(this, "Tentando logar usuário...", Toast.LENGTH_SHORT).show();
            if (usuarios.getCurrentUser() != null) {
                Toast.makeText(this, "Já tem um usuário logado: " + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
            } else {
                String email = txtEmail.getText().toString();
                String senha = txtSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(this, "E-mail e/ou senha inválidos", Toast.LENGTH_SHORT).show();
                } else {
                    usuarios.signInWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Usuário criado e logado c/ sucesso: " + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Não consegui logar o usuário", Toast.LENGTH_SHORT).show();
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    if (e != null) {
                                        Toast.makeText(MainActivity.this, "ERRO: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        btnCadastrar.setOnClickListener(view1 -> {
            Toast.makeText(this, "Tentando criar novo usuário...", Toast.LENGTH_SHORT).show();
            if (usuarios.getCurrentUser() != null) {
                Toast.makeText(this, "Já tem um usuário logado!!" + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
            } else {
                String email = txtEmail.getText().toString();
                String senha = txtSenha.getText().toString();
                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(this, "E-mail e/ou senha inválidos", Toast.LENGTH_SHORT).show();
                } else {
                    usuarios.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Usuário criado com sucesso!!!" + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Criação do usuário falhou!", Toast.LENGTH_SHORT).show();
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    if (e != null) {
                                        Toast.makeText(MainActivity.this, "ERRO: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}