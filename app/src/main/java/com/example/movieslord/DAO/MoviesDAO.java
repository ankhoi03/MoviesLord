package com.example.movieslord.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieslord.Activities.RegisterActivity;
import com.example.movieslord.Adapter.FavoriteAdapter;
import com.example.movieslord.Adapter.HistoryAdapter;
import com.example.movieslord.Adapter.MoviesAdapter;
import com.example.movieslord.Adapter.SearchAdapter;
import com.example.movieslord.Adapter.SliderAdapter;
import com.example.movieslord.Models.SliderModel;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviesDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void loadFirebaseMovie(String collection, List<MoviesModel> movies, MoviesAdapter adapter) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> item=document.getData();
                                String title=item.get("title").toString();
                                String imgurl=item.get("imgurl").toString();
                                String vdurl=item.get("vdurl").toString();
                                String detail=item.get("detail").toString();
                                String id=document.getId();
                                MoviesModel model1=new MoviesModel(title,imgurl,vdurl,detail,id);
                                movies.add(model1);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(">>>>TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void loadFirebaseHistoryMovie(String collection, List<MoviesModel> movies, HistoryAdapter adapter) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> item=document.getData();
                                String title=item.get("Htitle").toString();
                                String imgurl=item.get("Himgurl").toString();
                                String vdurl=item.get("Hvdurl").toString();
                                String detail=item.get("Hdetail").toString();
                                long lastPosition= (long) item.get("Hlastposition");
                                String id=document.getId();
                                MoviesModel model1=new MoviesModel(title,imgurl,vdurl,detail,id,lastPosition);
                                movies.add(model1);

                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.w(">>>>TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }


    public void addHistoryMovie(String collection, MoviesModel hisMovies,long lastPosition){
        Map<String, Object> movie = new HashMap<>();
        movie.put("Htitle",hisMovies.getTitle());
        movie.put("Himgurl",hisMovies.getImgurl());
        movie.put("Hvdurl",hisMovies.getVdurl());
        movie.put("Hdetail",hisMovies.getDetail());
        movie.put("Hlastposition",lastPosition);
        String nowID=Calendar.getInstance().getTimeInMillis()+"";
        db.collection(collection)
                .document(nowID)
                .set(movie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(">>>>>>>>TAG", "Error adding document", e);

                    }
                });

    }
    public void History(String collection,List<MoviesModel> list){
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Map<String, Object> item = document.getData();
                                String title = item.get("Htitle").toString();
                                String imgurl = item.get("Himgurl").toString();
                                String vdurl = item.get("Hvdurl").toString();
                                String detail = item.get("Hdetail").toString();
                                long lastposition= (long) item.get("Hlastposition");
                                String id = document.getId();
                                MoviesModel model = new MoviesModel(title, imgurl, vdurl, detail, id,lastposition);
                                list.add(model);
                            }
                        }else{
                            Log.d(">>>>>TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void deleteHistory(String collection,String idHis,Context context){
        db.collection(collection).document(idHis).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(">>>>>>>>TAG", "Error adding document", e);
                Toast.makeText(context,"Delete Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public LinearLayoutManager layoutRecycleHorizontalManager(Context context){
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        return layoutManager;

    }

    public void loadFirebaseForSlider(List<SliderModel> sliderModels, SliderAdapter sliderAdapter) {
        db.collection("slider")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> item=document.getData();
                                String title=item.get("Stitle").toString();
                                String imgurl=item.get("Simgurl").toString();
                                String vdurl=item.get("Svdurl").toString();
                                SliderModel model=new SliderModel(title,imgurl,vdurl);
                                sliderModels.add(model);

                            }
                            sliderAdapter.notifyDataSetChanged();

                        } else {
                            Log.w(">>>>TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }


    public void loadFirebaseFavorteMovie(String collection, List<MoviesModel> movies, FavoriteAdapter adapter){
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Map<String, Object> item = document.getData();
                                String title = item.get("Ftitle").toString();
                                String imgurl = item.get("Fimgurl").toString();
                                String vdurl = item.get("Fvdurl").toString();
                                String detail = item.get("Fdetail").toString();
                                String id = document.getId();
                                MoviesModel model = new MoviesModel(title, imgurl, vdurl, detail, id);
                                movies.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.d(".......TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void addFavorteMovie(String collection, MoviesModel favMovies ,Context context){
        Map<String, Object> movie = new HashMap<>();
        movie.put("Ftitle", favMovies.getTitle());
        movie.put("Fimgurl", favMovies.getImgurl());
        movie.put("Fvdurl", favMovies.getVdurl());
        movie.put("Fdetail", favMovies.getDetail());
        String nowID = Calendar.getInstance().getTimeInMillis()+"";
        db.collection(collection)
                .document(nowID)
                .set(movie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Đã thêm vào yêu thích!!!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(">>>>>TAG", "Error adding document", e);
                    }
                });
    }
    public void Favorite(String collection,List<MoviesModel> list){
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Map<String, Object> item = document.getData();
                                String title = item.get("Ftitle").toString();
                                String imgurl = item.get("Fimgurl").toString();
                                String vdurl = item.get("Fvdurl").toString();
                                String detail = item.get("Fdetail").toString();
                                String id = document.getId();
                                MoviesModel model = new MoviesModel(title, imgurl, vdurl, detail, id);
                                list.add(model);
                            }
                        }else{
                            Log.d(">>>>>TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    public void deleteFavorite(String collection,String idFav,Context context){
        db.collection(collection).document(idFav).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context,"Đã bỏ yêu thích!!!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(">>>>>>>>TAG", "Error adding document", e);
                Toast.makeText(context,"Delete Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadFirebaseAllmovies(String collection,List<MoviesModel> list,SearchAdapter adapter){
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Map<String, Object> item = document.getData();
                                String title = item.get("title").toString();
                                String imgurl = item.get("imgurl").toString();
                                String vdurl = item.get("vdurl").toString();
                                String detail = item.get("detail").toString();
                                String id = document.getId();
                                MoviesModel model = new MoviesModel(title, imgurl, vdurl, detail, id);
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.d(".......TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void addUser(User user, Context context){
        Map<String, Object> User = new HashMap<>();
        User.put("name", user.getName());
        User.put("username", user.getUsername());
        User.put("password",user.getPassword());
        User.put("linkimg", user.getLinkImg());
        String nowID = Calendar.getInstance().getTimeInMillis()+"_"+user.getUsername();
        db.collection("account")
                .document(nowID)
                .set(User)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Đăng ký thành công!,Sẽ chuyển về Login sau 5s",Toast.LENGTH_SHORT).show();
                        RegisterActivity registerActivity=(RegisterActivity) context;
                        registerActivity.out();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(">>>>>TAG", "Error adding document", e);
                    }
                });
    }

    public void getUser(List<User> userList){
        db.collection("account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Map<String, Object> item = document.getData();
                                String name = item.get("name").toString();
                                String username = item.get("username").toString();
                                String password = item.get("password").toString();
                                String linkImg = item.get("linkimg").toString();
                                User user=new User(username,name,password,linkImg);
                                userList.add(user);

                            }
                        }else{
                            Log.d(".......TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }


}
