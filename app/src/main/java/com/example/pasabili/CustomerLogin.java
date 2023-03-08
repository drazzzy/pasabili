package com.example.pasabili;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasabili.constants.Constants;
import com.example.pasabili.constants.Messages;
import com.example.pasabili.models.CustomerModel;
import com.example.pasabili.pages.customer.customer_navbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerLogin extends AppCompatActivity {
    EditText userInput, passInput;
    Button loginBtn;
    TextView registerLink;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    AuthResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInput = findViewById(R.id.userInput);
        passInput = findViewById(R.id.passInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.accCreationLink);
        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                email = String.valueOf(userInput.getText());
                password = String.valueOf(passInput.getText());

                if(TextUtils.isEmpty(email)){
                    userInput.setError(Messages.INPUT_REQUIRED("Email"));
                } else if(TextUtils.isEmpty(password)){
                    passInput.setError(Messages.INPUT_REQUIRED("Password"));
                } else {
                    signInCustomer(email, password);
                }

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), register.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    private void signInCustomer(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(CustomerLogin.this, Messages.AUTHENTICATION_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                    Intent customerHomeIntent = new Intent(getApplicationContext(), customer_navbar.class);
                    customerHomeIntent.putExtra(Constants.USER_UID, task.getResult().getUser().getUid());
                    startActivity(customerHomeIntent);
                    finish();
                } else{
                    Toast.makeText(CustomerLogin.this, Messages.AUTHENTICATION_FAILED, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}