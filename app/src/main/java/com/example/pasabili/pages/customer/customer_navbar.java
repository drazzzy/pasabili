package com.example.pasabili.pages.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.pasabili.components.Messages;
import com.example.pasabili.components.Notifications;
import com.example.pasabili.R;
import com.example.pasabili.components.customer_home;
import com.example.pasabili.constants.Constants;
import com.example.pasabili.models.CustomerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class customer_navbar extends AppCompatActivity {

    BottomNavigationView botNavbar;
    MaterialToolbar topAppBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;
    String uid;
    TextView email;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_navbar);
        botNavbar = findViewById(R.id.bottomNav);
        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        switchFragment(new customer_home());

        Intent intent = getIntent();
        uid = intent.getStringExtra(Constants.USER_UID);
        botNavbar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.navigation_home:
                    switchFragment(new customer_home());
                    break;
                case R.id.navigation_messages:
                    switchFragment(new Messages());
                    break;
                case R.id.navigation_notifications:
                    switchFragment(new Notifications());
                    break;
            }
            return true;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, topAppBar, R.string.action_sign_in, R.string.action_sign_in);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(!TextUtils.isEmpty(uid)){
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            db = FirebaseFirestore.getInstance();
            db.collection(Constants.DB_USER)
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        documentSnapshot.getData();
                        CustomerModel resultModel = new CustomerModel(documentSnapshot);
                        email = findViewById(R.id.profile_email);
                        email.setText(resultModel.getEmail());
                        username = findViewById(R.id.profile_name);
                        username.setText(resultModel.getFirstname().concat(" ").concat(resultModel.getFirstname()));
                    }});

        }
    }

    private void switchFragment(Fragment fragmentToSwitch){
        Intent intent = getIntent();
        uid = intent.getStringExtra(Constants.USER_UID);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_UID, uid);
        fragmentToSwitch.setArguments(bundle);
        fragmentTransaction.replace(R.id.customerFrame, fragmentToSwitch);
        fragmentTransaction.commit();
    }
}