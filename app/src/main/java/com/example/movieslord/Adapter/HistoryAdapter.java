package com.example.movieslord.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private List<MoviesModel> dataModels;
    private List<MoviesModel> history=new ArrayList<>();
    private MoviesDAO moviesDAO =new MoviesDAO();
    private Context context;
    public HistoryAdapter() {
    }

    public HistoryAdapter(Context context, List<MoviesModel> dataModels) {
        this.dataModels= dataModels;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_cardview_item,parent,false);
        return new HistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MainActivity mainActivity= (MainActivity) context;
        String user=mainActivity.getIdUser();
        moviesDAO.History("/user/"+user+"/history",history);
        Glide.with(holder.itemView)
                .load(dataModels.get(position).getImgurl())
                .into(holder.imgCardviewMovie);
        holder.imgCardviewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movies=new Intent(context, PlayerActivity.class);
                MoviesModel hisMovie=new MoviesModel(dataModels.get(position).getTitle(),dataModels.get(position).getImgurl(),dataModels.get(position).getVdurl(),dataModels.get(position).getDetail(),dataModels.get(position).getId(),dataModels.get(position).getLastPosition());
                String idHis="";

                for (MoviesModel his:history) {
                    if(dataModels.get(position).getTitle().equals(his.getTitle())){
                        idHis=his.getId();
                        moviesDAO.deleteHistory("/user/"+user+"/history",idHis,context);
                        dataModels.remove(position);
                        break;
                    }
                }
                movies.putExtra("user",user);
                movies.putExtra("vid",hisMovie);
                dataModels.add(hisMovie);
                notifyDataSetChanged();
                v.getContext().startActivity(movies);
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
