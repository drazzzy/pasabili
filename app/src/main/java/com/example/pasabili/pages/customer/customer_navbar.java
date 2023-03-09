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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pasabili.CustomerLogin;
import com.example.pasabili.Itinerant_Login;
import com.example.pasabili.components.Messages;
import com.example.pasabili.components.Notifications;
import com.example.pasabili.R;
import com.example.pasabili.components.customer_home;
import com.example.pasabili.constants.Constants;
import com.example.pasabili.models.CustomerModel;
import com.example.pasabili.models.ItinerantModel;
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
    TextView email, fullName, userName, firstName, lastName, email2, password, logoutBtn;
    Boolean isTraveler;

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
        isTraveler = intent.getStringExtra(Constants.USER_TYPE).equals(Constants.USER_TYPES.TRAVELER.toString());
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
            db.collection(
                    isTraveler?
                            Constants.DB_USER : Constants.DB_ITINERANT
                    )
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        email = findViewById(R.id.profile_email);
                        fullName = findViewById(R.id.profile_name);
                        userName = findViewById(R.id.profile_username);
                        firstName = findViewById(R.id.profile_firstname);
                        lastName = findViewById(R.id.profile_lastname);
                        email2 = findViewById(R.id.profile_email2);
                        password = findViewById(R.id.profile_password);
                        if(isTraveler){
                            CustomerModel resultModel = new CustomerModel(documentSnapshot);
                            setValuesForTraveler(resultModel);
                        } else {
                            ItinerantModel resultModel = new ItinerantModel(documentSnapshot);
                            setValuesForItinerant(resultModel);
                        }
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

    private void setValuesForTraveler(CustomerModel customerModel){
        email.setText(customerModel.getEmail());
        fullName.setText(customerModel.getFirstname().concat(" ").concat(customerModel.getFirstname()));
        userName.setText(customerModel.getUserId());
        firstName.setText(customerModel.getFirstname());
        lastName.setText(customerModel.getLastname());
        email2.setText(customerModel.getEmail());
        password.setText(customerModel.getPassword());
    }

    private void setValuesForItinerant(ItinerantModel itinerantModel){
        email.setText(itinerantModel.getEmail());
        fullName.setText(itinerantModel.getFirstname().concat(" ").concat(itinerantModel.getFirstname()));
        userName.setText(itinerantModel.getUserId());
        firstName.setText(itinerantModel.getFirstname());
        lastName.setText(itinerantModel.getLastname());
        email2.setText(itinerantModel.getEmail());
        password.setText(itinerantModel.getPassword());
    }

    public void logoutBtnClicked(View view) {
        Log.i("clicked", "clicked");
        Intent loginIntent = new Intent(getApplicationContext(), isTraveler?  CustomerLogin.class : Itinerant_Login.class);
        startActivity(loginIntent);
        finish();
    }
}