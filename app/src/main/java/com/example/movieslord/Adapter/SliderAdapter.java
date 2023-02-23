package com.example.movieslord.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.movieslord.Activities.MainActivity;
import com.example.movieslord.Activities.PlayerActivity;
import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.SliderModel;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder> {
    private Context context;
    private List<MoviesModel> history=new ArrayList<>();
    private MoviesDAO moviesDAO =new MoviesDAO();
    public SliderAdapter(Context context, List<SliderModel> sliderModels) {
        this.context = context;
        this.sliderModels = sliderModels;
    }

    public SliderAdapter() {
    }

    private List<SliderModel> sliderModels =new ArrayList<>();

    public void renewItems(List<SliderModel> sliderModels){
        this.sliderModels = sliderModels;
        notifyDataSetChanged();
    }

    public void deleteItems(int position){
        this.sliderModels.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        MainActivity mainActivity= (MainActivity) context;
        String user=mainActivity.getIdUser();
        moviesDAO.History("/user/"+user+"/history",history);
        Glide.with(viewHolder.itemView).load(sliderModels.get(position).getSimgurl()).into(viewHolder.slider_image);
        viewHolder.img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoviesModel moviesModel=new MoviesModel(sliderModels.get(position).getStitle(), sliderModels.get(position).getSimgurl(), sliderModels.get(position).getSvdurl(),"Detail");
               // moivesDAO.addHistoryMovie("/user/user_example/history",moviesModel);
                //Check trung` lap lich su
                String idHis="";
                for (MoviesModel his:history) {
                    if(sliderModels.get(position).getStitle().equals(his.getTitle())){
                        idHis=his.getId();
                        moviesDAO.deleteHistory("/user/"+user+"/history",idHis,context);
                        break;
                    }
                }
                Intent trailer_video=new Intent(context, PlayerActivity.class);
                trailer_video.putExtra("user",user);
                trailer_video.putExtra("vid",moviesModel);
                v.getContext().startActivity(trailer_video);
            }
        });
    }

    @Override
    public int getCount() {
        return sliderModels.size();
    }

    public class MyViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView slider_image;
        ImageView img_play;
        public MyViewHolder(View itemView) {
            super(itemView);
            slider_image=itemView.findViewById(R.id.image_thumbnail);
            img_play=itemView.findViewById(R.id.imagePlay);
        }
    }
}
