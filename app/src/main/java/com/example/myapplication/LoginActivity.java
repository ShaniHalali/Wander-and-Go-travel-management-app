package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
public class LoginActivity extends AppCompatActivity {

    // Register for the Firebase authentication result callback
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // If the user is not signed in, start the sign-in flow
            signIn();
        } else {
            // If the user is already signed in, check or add user data to Firebase
            checkAndAddUserToDatabase(user);
        }
    }

    private void checkAndAddUserToDatabase(FirebaseUser user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // If the user does not exist, add them to the database
                    User newUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(),user.getPhoneNumber());
                    usersRef.child(user.getUid()).setValue(newUser);

                    // Add initial data for PackingItems and Trips
                    DatabaseReference userRef = usersRef.child(user.getUid());
                    addInitialData(userRef);

                    // If the phone number exists, add it under the userKey
                    if (user.getPhoneNumber()!= null && !user.getPhoneNumber().isEmpty()) {
                        usersRef.child(user.getUid()).child("PhoneNumber").setValue(user.getPhoneNumber());
                    }

                    if (user.getDisplayName()!= null && !user.getPhoneNumber().isEmpty()) {
                        usersRef.child(user.getUid()).child("PhoneNumber").setValue(user.getPhoneNumber());
                    }

                }
                // Proceed to the main activity
                transactToMainActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors here
            }
        });
    }

    private void addInitialData(DatabaseReference userRef) {
        // Adding initial PackingItems data
        DatabaseReference packingItemsRef = userRef.child("PackingItems").child("allItems");


        // Adding initial Trips data
        DatabaseReference tripsRef = userRef.child("Trips");
        DatabaseReference day1Ref = tripsRef.child("allDays");

    }

    private void transactToMainActivity() {
        // Transition to the LottieSplashActivity
        Intent i = new Intent(this, LottieSplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.pink_plane)  // Set your logo here
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                updateNavHeader(user);
                checkAndAddUserToDatabase(user);
            }
        } else {
            // Sign in failed
            if (response == null) {
                // User canceled the sign-in flow using the back button
                // Optionally show a toast message
            } else {
                // Handle error cases here
                // Log the error or show a relevant message to the user
                // Example: response.getError().getErrorCode()
            }
        }
    }

    private void updateNavHeader(FirebaseUser user) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderTitle = headerView.findViewById(R.id.nav_header_title);

        String userName = user.getDisplayName();
        if (userName != null && !userName.isEmpty()) {
            navHeaderTitle.setText(userName);
        } else {
            navHeaderTitle.setText(getString(R.string.default_user_name));
        }
    }
}