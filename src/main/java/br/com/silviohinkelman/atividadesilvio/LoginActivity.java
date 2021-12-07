package br.com.silviohinkelman.atividadesilvio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail,txtSenha;
    private Button btnEntrar,btnCadastrar;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrar();
            }
        });
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = auth.getCurrentUser();
                if ( usuario != null ){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity( intent );
                }
            }
        };

        auth.addAuthStateListener( authStateListener );

    }
    private void cadastrar(){
        String email= txtEmail.getText().toString();
        String senha= txtSenha.getText().toString();
        if (email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this, "Preencha todos os campos!",Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword( email, senha )
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if ( task.isSuccessful() ){
                                usuario = auth.getCurrentUser();
                            } else {
                                task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Não foi possível cadastrar usuário!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void entrar(){
        String email= txtEmail.getText().toString();
        String senha= txtSenha.getText().toString();
        if (email.isEmpty() || senha.isEmpty() ){
            Toast.makeText(this, "Preencha todos os campos!",Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword( email, senha )
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if ( task.isSuccessful() ){
                                usuario = auth.getCurrentUser();
                            } else {
                                task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Não foi possível Logar!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}