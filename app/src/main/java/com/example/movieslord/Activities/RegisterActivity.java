package com.example.movieslord.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.User;
import com.example.movieslord.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private ImageView imgUser;
    private List<User> userList=new ArrayList<>();
    private MoviesDAO moviesDAO =new MoviesDAO();
    private String username,name,passwords,linkImg="";
    private TextInputEditText edtName, edtUsername,edtPassword, edtCfmPassword;
    private TextInputLayout tvInName, tvInUserName, tvInPassword, tvInCfmPassword;
    private Button btnResgiter;
    private boolean chk= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imgUser=findViewById(R.id.imgUser);
        edtName=findViewById(R.id.name);
        edtUsername=findViewById(R.id.username);
        edtPassword=findViewById(R.id.password);
        edtCfmPassword=findViewById(R.id.confirmpassword);
        //textinput
        tvInName=findViewById(R.id.tvInName);
        tvInUserName=findViewById(R.id.tvInUserName);
        tvInPassword=findViewById(R.id.tvInPass);
        tvInCfmPassword=findViewById(R.id.tvInCFMPass);


        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }

        });

        btnResgiter=findViewById(R.id.btnRegister);
        btnResgiter.setEnabled(false);
        //bat loi
        moviesDAO.getUser(userList);
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals("")){
                    tvInName.setError("Hãy cho tôi biết tên của bạn");
                    chk=false;
                    btnResgiter.setEnabled(chk);

                }else{
                    tvInName.setError(null);
                    String loi_name = loi(edtUsername.getText().toString().trim(),userList);
                    String loi_pass = loi(edtPassword.getText().toString().trim(),userList);
                    String loiname=loi_name(edtName.getText().toString());
                    if(loi_name.equalsIgnoreCase("ok")){

                        if(loi_pass.equalsIgnoreCase("ok") &&
                                edtPassword.getText().toString().trim().equals(edtCfmPassword.getText().toString().trim())&& loiname.equalsIgnoreCase("ok")){
                            chk = true;
                            btnResgiter.setEnabled(chk);
                        }else{
                            chk = false;
                            btnResgiter.setEnabled(chk);
                        }
                    }else{
                        chk = false;
                        btnResgiter.setEnabled(chk);
                    }

                }
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
                String loi_name = loi(edtUsername.getText().toString().trim(),userList);
                String loi_pass = loi(edtPassword.getText().toString().trim(),userList);
                String loiname=loi_name(edtName.getText().toString());
                if(loi_name.equalsIgnoreCase("ok")){
                    tvInUserName.setError(null);
                    if(loi_pass.equalsIgnoreCase("ok") &&
                            edtPassword.getText().toString().trim().equals(edtCfmPassword.getText().toString().trim())&& loiname.equalsIgnoreCase("ok")){
                        chk = true;
                        btnResgiter.setEnabled(chk);
                    }else{
                        chk = false;
                        btnResgiter.setEnabled(chk);
                    }
                }else{
                    tvInUserName.setError("Username "+loi_name);
                    chk = false;
                    btnResgiter.setEnabled(chk);
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
                String loi_name = loi(edtUsername.getText().toString().trim(),userList);
                String loi_pass = loi(edtPassword.getText().toString().trim(),userList);
                String loiname=loi_name(edtName.getText().toString());
                if(loi_pass.equalsIgnoreCase("ok")){
                    tvInPassword.setError(null);
                    if(loi_name.equalsIgnoreCase("ok") &&
                            s.toString().trim().equals(edtCfmPassword.getText().toString().trim())&& loiname.equalsIgnoreCase("ok")){
                        chk = true;
                        btnResgiter.setEnabled(chk);
                    }else{
                        chk = false;
                        btnResgiter.setEnabled(chk);
                    }
                }else{
                    tvInPassword.setError("Mật khẩu "+loi_pass);
                    chk = false;
                    btnResgiter.setEnabled(chk);
                }
            }
        });

        edtCfmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String loi_name = loi(edtUsername.getText().toString().trim(),userList);
                String loi_pass = loi(edtPassword.getText().toString().trim(),userList);
                String loiname=loi_name(edtName.getText().toString());
                if(s.toString().trim().equals(edtPassword.getText().toString().trim())){
                    tvInCfmPassword.setError(null);
                    if(loi_name.equalsIgnoreCase("ok") && loi_pass.equalsIgnoreCase("ok")&& loiname.equalsIgnoreCase("ok")){
                        chk = true;
                        btnResgiter.setEnabled(chk);
                    }
                }else{
                    tvInCfmPassword.setError("Mật khẩu xác nhận không chính xác");
                    chk = false;
                    btnResgiter.setEnabled(chk);
                }
            }
        });






        btnResgiter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   resgister();

                }
            });

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp;
                bmp = BitmapFactory.decodeStream(bufferedInputStream);
                imgUser.setImageBitmap(bmp);
                upLoadStorage(bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...

        }
    }

    private void upLoadStorage(Bitmap bitmap){
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference();
        StorageReference imageReference=storageReference.child("imgProfile/"+Calendar.getInstance().getTimeInMillis()+".jpg");
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        UploadTask uploadTask=imageReference.putBytes(bytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(task.isSuccessful()){
                    return imageReference.getDownloadUrl();
                }
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri=task.getResult();
                linkImg= String.valueOf(downloadUri);
                Log.d(">>>>>TAG", "onComplete: "+downloadUri);

            }
        });

    }

    private void resgister(){
        username=edtUsername.getText().toString();
        name=edtName.getText().toString();
        passwords=edtPassword.getText().toString();
        if(linkImg.equals("")){
            linkImg="https://firebasestorage.googleapis.com/v0/b/duan01-dde44.appspot.com/o/imgProfile%2Fdefault_avata.jpg?alt=media&token=fe6239d5-1b91-407c-ae2d-5dcdad4dd163";
        }
        User user=new User(username,name,passwords,linkImg);
        moviesDAO.addUser(user,this);
    }
    public void out(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent resIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(resIntent);
                finish();
            }
        },4000);
    }

    private String loi(String text, List<User> list){
        String loi = "ok";
        if(text.equals("")){
            loi = "không được để trống";
        }else if(text.matches("[\\w]*[\\W]+[\\w]*")){
            loi = "không được có kí tự đặt biệt";
        }
        else if(text.matches("[\\D]+") || text.matches("[\\d]+")){
            loi = "phải có số và kí tự";
        }else {
            if(!list.isEmpty()){
                for(User itemList : list){
                    if(text.equals(itemList.getUsername())){
                        loi = "đã tồn tại";
                    }
                }
            }
        }
        return loi;
    }
    private String loi_name(String text) {
        String loi = "ok";
        if (text.equals("")) {
            loi = "không được để trống";

        }
        return loi;
    }
}