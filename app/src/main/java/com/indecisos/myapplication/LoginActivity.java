package com.indecisos.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private String nombre;
    private String pass;
    private EditText nombreEditText;
    private EditText passEditText;

    //login - firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(oyenteLoginButton);

        Button nuevoUsuarioButton = findViewById(R.id.nuevoUsuarioButton);
        nuevoUsuarioButton.setOnClickListener(oyenteNuevoUsuarioButton);

        nombreEditText = findViewById(R.id.nombreEditText);
        passEditText = findViewById(R.id.passEditText);

        mAuth = FirebaseAuth.getInstance();
    }

    private final View.OnClickListener oyenteLoginButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nombre = nombreEditText.getText().toString();
            nombre = nombre.trim();
            pass = passEditText.getText().toString();
            pass = pass.trim();

            if(!(nombre.isEmpty()) && !(pass.isEmpty())){
                mAuth.signInWithEmailAndPassword(nombre, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser sesion = mAuth.getCurrentUser();
        if(sesion != null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private final View.OnClickListener oyenteNuevoUsuarioButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(LoginActivity.this, RegistrarUsuario.class);
            startActivity(i);
        }
    };
}