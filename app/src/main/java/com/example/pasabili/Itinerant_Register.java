package com.example.pasabili;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasabili.constants.Constants;
import com.example.pasabili.constants.Messages;
import com.example.pasabili.models.CustomerModel;
import com.example.pasabili.models.ItinerantModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Itinerant_Register extends AppCompatActivity {

    EditText email, username, firstName, lastName, address, password, confirmPass;
    TextView back, login;
    Button registerButton;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itenerant_register);
        email = findViewById(R.id.emailInput);
        username = findViewById(R.id.usernameInput);
        firstName = findViewById(R.id.firstnameInput);
        lastName = findViewById(R.id.lastnameInput);
        address = findViewById(R.id.addressInput);
        password = findViewById(R.id.passwordInput);
        confirmPass = findViewById(R.id.confirmPassInput);
        back = findViewById(R.id.backBtn);
        login = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.registerBtn);
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordStr = password.getText().toString();
                Boolean checkIfContainsDigit = passwordStr.chars()
                        .mapToObj(character -> (char) character)
                        .anyMatch(c -> Character.isDigit(c));
                if (!checkIfContainsDigit || passwordStr.length() < 6) {
                    address.setText(checkIfContainsDigit.toString() + passwordStr.length());
                    password.setError(Messages.INPUT_PASSWORD_ERROR);
                } else {
                    registerAccount(
                            new ItinerantModel(
                                    email.getText().toString(),
                                    password.getText().toString(),
                                    firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    address.getText().toString()
                            )
                    );
                }
            }
        });

        //redirect to Login screen when back button is clicked
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Itinerant_Login.class);
                startActivity(loginIntent);
                finish();
            }
        });

        //redirect to Login screen when Login button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), Itinerant_Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void registerAccount(ItinerantModel itinerantModel) {
        mAuth.createUserWithEmailAndPassword(itinerantModel.getEmail(), itinerantModel.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            itinerantModel.setUserId(user.getUid());
                            db = FirebaseFirestore.getInstance();

                            db.collection(Constants.DB_ITINERANT).document(user.getUid()).set(itinerantModel);

                            Intent loginIntent = new Intent(getApplicationContext(), CustomerLogin.class);
                            startActivity(loginIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Itinerant_Register.this, Messages.AUTHENTICATION_FAILED,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}