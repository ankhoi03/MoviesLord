package com.example.movieslord.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.User;
import com.example.movieslord.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextView tvRegister;
    private MoviesDAO moviesDAO =new MoviesDAO();
    private List<User> userList=new ArrayList<>();
    private GoogleSignInClient googleSignInClient;
    private ImageView btnGoogle,btnFacebook;
    private TextInputEditText edtUsername,edtPassword;
    private TextInputLayout tvInUserName, tvInPassword;
   private CallbackManager callbackManager;
   private Button btnLogin;
   private boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        readLogin();
        edtUsername=findViewById(R.id.edtLgUsername);
        edtPassword=findViewById(R.id.edtLgPassword);
        tvInUserName=findViewById(R.id.tvLGUserName);
        tvInPassword=findViewById(R.id.tvLGPass);
        //REGISTER
        tvRegister=findViewById(R.id.register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(resIntent);
                finish();
            }
        });
        //dang nhap thuong
        moviesDAO.getUser(userList);
        btnLogin=findViewById(R.id.loginbtn);
        btnLogin.setEnabled(check);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginNormal(userList);
            }
        });
        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String loi_name=loi(edtUsername.getText().toString());
                String loi_pass=loi(edtPassword.getText().toString());
                if(loi_name.equalsIgnoreCase("ok")){

                    tvInUserName.setError(null);
                    if(loi_name.equalsIgnoreCase("ok")&&loi_pass.equalsIgnoreCase("ok")){
                        check=true;
                        tvInUserName.setError(null);
                        btnLogin.setEnabled(check);
                    }
                }


                else {
                    tvInUserName.setError(loi_name);
                    check=false;
                    btnLogin.setEnabled(check);
                }
            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String loi_name=loi(edtUsername.getText().toString());
                String loi_pass=loi(edtPassword.getText().toString());
                if(loi_pass.equalsIgnoreCase("ok")){
                    tvInPassword.setError(null);
                    if(loi_name.equalsIgnoreCase("ok")){
                        check=true;
                        tvInPassword.setError(null);
                        btnLogin.setEnabled(check);
                    }
                }
                else {
                    tvInPassword.setError(loi_pass);
                    check=false;
                    btnLogin.setEnabled(check);
                }
            }
        });
        //google
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleSignInClient= GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);
        btnGoogle=findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                googleLauncher.launch(intent);
            }
        });
        //facebook


        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton=findViewById(R.id.facebooklogin);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }

            @Override
            public void onCancel() {

            }
        });

        ProfileTracker profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(@Nullable Profile profile, @Nullable Profile profile1) {
                if(profile1!=null){
                    String name=profile1.getName();
                    String id=profile1.getId();
                    String imgFacebook= String.valueOf(profile1.getProfilePictureUri(96,96));
                    Log.d(">>>>>>>TAG", "onCurrentProfileChanged: "+name);
                    Log.d(">>>>>>>TAG", "onCurrentProfileChanged: "+id);
                    writeLogin("facebook",id,name,"",imgFacebook);
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    Intent homeIntent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(homeIntent);
                    finish();

                }
            }
        };

        btnFacebook=findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    ActivityResultLauncher<Intent> googleLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data=result.getData();
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                String email=account.getEmail();
                String name= account.getDisplayName();
                String id=account.getId();
                String imgGoogle= String.valueOf(account.getPhotoUrl());
                Log.d(">>>>>TAG","onActivityResult Email: "+email);
                Log.d(">>>>>TAG","onActivityResult Name: "+name);
                writeLogin("google",id,name,email,imgGoogle);
                Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                Intent homeIntent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(homeIntent);
                finish();

            }catch (Exception e){
                Log.d(">>>>>TAG","onActivityResult ERROR: "+e.getMessage());
                Toast.makeText(LoginActivity.this,"Đăng nhập không thành công",Toast.LENGTH_LONG).show();
            }
        }
    });


    private void writeLogin(String type,String id,String name,String email,String img){
        SharedPreferences preferences=getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("isLoggedIn",true);
        editor.putString("type",type);
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("email", email);
        editor.putString("image", img);
        editor.commit();
    }

    private void readLogin(){
        SharedPreferences preferences= getSharedPreferences("LOGIN_STATUS",MODE_PRIVATE);
        Boolean isLoggedIn = preferences.getBoolean("isLoggedIn",false);
        if(isLoggedIn){
            Intent homeIntent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(homeIntent);
            finish();

        }
    }
    private String loi(String text) {
        String loi = "ok";
        if (text.equals("")) {
            loi = "không được để trống";

        }
        return loi;
    }
    private void loginNormal(List<User> userList){
       String username= edtUsername.getText().toString();
       String pass=edtPassword.getText().toString();
       boolean login=false;
        for (User user: userList) {
            if(username.equals(user.getUsername())&&pass.equals(user.getPassword())){
                login=true;
                writeLogin("normal",user.getUsername(),user.getName(),"null",user.getLinkImg());

            }
        }
        if(login==true){
            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
            Intent homeIntent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(homeIntent);
            finish();
        }else {
                Toast.makeText(LoginActivity.this,"Sai tên đăng nhập hoặc mật khẩu",Toast.LENGTH_SHORT).show();
                tvInUserName.setError("");
                tvInPassword.setError("");
        }
    }
}