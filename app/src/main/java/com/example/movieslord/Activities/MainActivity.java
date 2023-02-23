package com.example.movieslord.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.movieslord.Fragments.FavoriteFragment;
import com.example.movieslord.Fragments.HomeFragment;
import com.example.movieslord.Fragments.MeFragment;
import com.example.movieslord.Fragments.SearchFragment;
import com.example.movieslord.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;
    private GoogleSignInClient googleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnv=findViewById(R.id.bottomNavigationView);
        replaceFragment(new HomeFragment());
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottomNavHome:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.bottomNavSearch:
                        replaceFragment(new SearchFragment());
                        break;

                    case R.id.bottomNavFavorite:
                        replaceFragment(new FavoriteFragment());
                        break;
                    case R.id.bottomNavMe:
                        replaceFragment(new MeFragment());
                        break;
                }return true;
            }
        });
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {

                        if(isOpen){

                            bnv.setVisibility(View.INVISIBLE);

                        }else{

                            bnv.setVisibility(View.VISIBLE);

                        }
                    }
                });

    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    public void logOut(){

        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        if(preferences.getString("type","null").equals("google")){
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
            googleSignInClient.signOut();
        }
        if(preferences.getString("type","null").equals("facebook")){
            LoginManager.getInstance().logOut();
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("id");
        editor.remove("type");
        editor.remove("name");
        editor.remove("email");
        editor.remove("image");
        editor.apply();
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public String getName(){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        String name=preferences.getString("name","Võ Hoàng Minh Thư");
        return name;
    }
    public String getIdUser(){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        String id=preferences.getString("id","minhthu");
        return id;
    }
    public Uri getImage(){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        Uri linkImg= Uri.parse(preferences.getString("image","null"));
        return linkImg;
    }
}
