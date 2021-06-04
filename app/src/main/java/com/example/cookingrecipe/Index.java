package com.example.cookingrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Index extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_index);
        loadFragment(new newFeedFragment());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            System.out.println(item.getItemId());
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    fragment = new Profile();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_search:
                    fragment = new searchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_new:
                    fragment = new postFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_newfeed:
                    fragment = new newFeedFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sign_out:
                System.out.println(mAuth.getCurrentUser().getDisplayName());
                mAuth.signOut();
                Intent intent = new Intent(this.getApplicationContext(),Login.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this.getApplicationContext(),Login.class);
            startActivity(intent);
        }
    }
}