package com.indecisos.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrarUsuario extends AppCompatActivity {

    //Elementos
    EditText nuevoUsuarioEditText;
    EditText nuevoPassEditText;
    EditText repPassEditText;
    TextView confTextView;

    String nombre;
    String pass;
    String confPass;

    //Login - Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        nuevoUsuarioEditText = findViewById(R.id.nuevoNombreEditText);
        nuevoPassEditText = findViewById(R.id.nuevaPassEditText);
        repPassEditText = findViewById(R.id.confirmarPassEditText);
        confTextView = findViewById(R.id.confRegistroTextView);

        Button registrarButton = findViewById(R.id.crearUsuarioButton);
        registrarButton.setOnClickListener(oyenteRegistroButton);

        mAuth = FirebaseAuth.getInstance();
    }

    private final View.OnClickListener oyenteRegistroButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nombre = nuevoUsuarioEditText.getText().toString();
            nombre = nombre.trim();
            pass = nuevoPassEditText.getText().toString();
            pass = pass.trim();
            confPass = repPassEditText.getText().toString();
            confPass = confPass.trim();

            if(!(nombre.isEmpty()) && !(pass.isEmpty()) && !(confPass.isEmpty())){
                if(pass.equals(confPass)){
                    mAuth.createUserWithEmailAndPassword(nombre, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                confTextView.setText(R.string.registro_exitoso);
                            }else{
                                confTextView.setText(R.string.registro_existe);
                            }
                        }
                    });
                }else{
                    confTextView.setText(R.string.registro_falla_pass);
                }
            }

        }
    };
}