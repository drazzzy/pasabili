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
import com.example.pasabili.pages.customer.customer_navbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Itinerant_Login extends AppCompatActivity {
    EditText userInput, passInput;
    Button loginBtn;
    TextView registerLink, travelerRedirect;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    AuthResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itenerant_login);
        userInput = findViewById(R.id.userInput);
        passInput = findViewById(R.id.passInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.accCreationLink);
        mAuth = FirebaseAuth.getInstance();
        travelerRedirect = findViewById(R.id.travelerRedirect);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                email = String.valueOf(userInput.getText());
                password = String.valueOf(passInput.getText());

                if (TextUtils.isEmpty(email)) {
                    userInput.setError(Messages.INPUT_REQUIRED("Email"));
                } else if (TextUtils.isEmpty(password)) {
                    passInput.setError(Messages.INPUT_REQUIRED("Password"));
                } else {
                    signInCustomer(email, password);
                }

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), Itinerant_Register.class);
                startActivity(registerIntent);
                finish();
            }
        });

        travelerRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itinerantLoginIntent = new Intent(getApplicationContext(), CustomerLogin.class);
                startActivity(itinerantLoginIntent);
                finish();
            }
        });
    }

    private void signInCustomer(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(Itinerant_Login.this, Messages.AUTHENTICATION_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                    Intent customerHomeIntent = new Intent(getApplicationContext(), customer_navbar.class);
                    customerHomeIntent.putExtra(Constants.USER_UID, task.getResult().getUser().getUid());
                    customerHomeIntent.putExtra(Constants.USER_TYPE, Constants.USER_TYPES.ITINERANT.toString());
                    startActivity(customerHomeIntent);
                    finish();
                } else {
                    Toast.makeText(Itinerant_Login.this, Messages.AUTHENTICATION_FAILED, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}