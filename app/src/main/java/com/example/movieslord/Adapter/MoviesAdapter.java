package com.example.movieslord.Adapter;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieslord.Activities.MainActivity;
import com.example.movieslord.Activities.PlayerActivity;
import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private List<MoviesModel> dataModels;
    private Context context;
    private List<MoviesModel> history=new ArrayList<>();
    private List<MoviesModel> favMovies=new ArrayList();
    private MoviesDAO moviesDAO =new MoviesDAO();
    public MoviesAdapter() {
    }

    public MoviesAdapter(Context context, List<MoviesModel> dataModels) {
        this.dataModels= dataModels;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_cardview_item,parent,false);
        return new MoviesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MainActivity mainActivity= (MainActivity) context;
        String user=mainActivity.getIdUser();
        moviesDAO.Favorite("/user/"+user+"/favorite",favMovies);
        moviesDAO.History("/user/"+user+"/history",history);
        Glide.with(holder.itemView).load(dataModels.get(position).getImgurl()).into(holder.imgCardviewMovie);
        holder.imgCardviewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View view=View.inflate(context,R.layout.movie_detail_dialog,null);
                builder.setView(view);
                ImageView imgDialog=view.findViewById(R.id.image_dialog);
                ImageView imgClose=view.findViewById(R.id.imgDialogClose);
                TextView tvTitleDialog=view.findViewById(R.id.tvTitleDialog);
                TextView tvDetailDialog=view.findViewById(R.id.tvDetailDialog);
                ImageView imgPlay=view.findViewById(R.id.image_dialog_play);
                ImageView imgFavorite=view.findViewById(R.id.image_dialog_favorite);
                ImageView imgRedFavorite=view.findViewById(R.id.image_dialog_red_favorite);
                Glide.with(context).load(dataModels.get(position).getImgurl()).into(imgDialog);
                tvTitleDialog.setText(dataModels.get(position).getTitle());
                tvDetailDialog.setText(dataModels.get(position).getDetail());
                Dialog dialog= builder.create();
                dialog.show();
                imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent movies=new Intent(context, PlayerActivity.class);

                        MoviesModel hisMovie=new MoviesModel(dataModels.get(position).getTitle(),dataModels.get(position).getImgurl(),dataModels.get(position).getVdurl(),dataModels.get(position).getDetail(),dataModels.get(position).getId());
                       // moivesDAO.addHistoryMovie("/user/"+"user_example"+"/history",hisMovie);
                        //Check trung` lap lich su
                        String idHis="";
                        for (MoviesModel his:history) {
                            if(dataModels.get(position).getTitle().equals(his.getTitle())){
                                idHis=his.getId();
                                moviesDAO.deleteHistory("/user/"+user+"/history",idHis,context);
                                break;
                            }
                        }
                        movies.putExtra("user",user);
                        movies.putExtra("vid",hisMovie);
                        v.getContext().startActivity(movies);
                        System.out.println(idHis);
                        idHis="";
                        dialog.dismiss();
                    }
                });
               boolean check=true;
               String idFav="";
                for (MoviesModel fav:favMovies) {
                    if(dataModels.get(position).getTitle().equals(fav.getTitle())){
                        idFav=fav.getId();
                        check=false;
                        break;
                    }
                }
                if(check){
                    imgFavorite.setVisibility(View.VISIBLE);
                    imgRedFavorite.setVisibility(View.INVISIBLE);

                } else {
                    imgFavorite.setVisibility(View.INVISIBLE);
                    imgRedFavorite.setVisibility(View.VISIBLE);
                }
                imgFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoviesModel favMovie=new MoviesModel(dataModels.get(position).getTitle(),dataModels.get(position).getImgurl(),dataModels.get(position).getVdurl(),dataModels.get(position).getDetail());
                        moviesDAO.addFavorteMovie("/user/"+user+"/favorite",favMovie, v.getContext());
                        imgFavorite.setVisibility(View.INVISIBLE);
                        imgRedFavorite.setVisibility(View.VISIBLE);
                    }
                });
                String finalIdFav = idFav;
                imgRedFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moviesDAO.deleteFavorite("/user/"+user+"/favorite", finalIdFav,v.getContext());
                        imgFavorite.setVisibility(View.VISIBLE);
                        imgRedFavorite.setVisibility(View.INVISIBLE);
                    }
                });
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        history.clear();
                    }
                });



           }
      });

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCardviewMovie;
        CardView cardviewMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCardviewMovie=itemView.findViewById(R.id.imgCardMovie);
            cardviewMovie=itemView.findViewById(R.id.cardMovie);
        }
    }


}
