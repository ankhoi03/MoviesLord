package com.example.movieslord.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.movieslord.Activities.MainActivity;
import com.example.movieslord.Adapter.HistoryAdapter;
import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;

import java.util.ArrayList;
import java.util.List;


public class MeFragment extends Fragment {
    private List<MoviesModel> historyMovie =new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private MoviesDAO moviesDAO =new MoviesDAO();
    private Button btnLogout;

    public MeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_me, container, false);
       MainActivity mainActivity= (MainActivity) view.getContext();
       ImageView imgmefragment=view.findViewById(R.id.meFragmentImg);
        TextView tvName=view.findViewById(R.id.tv_name);
        ImageView imgProfile=view.findViewById(R.id.profile_image);
        tvName.setText(mainActivity.getName());
        Glide.with(getContext()).load(mainActivity.getImage()).into(imgProfile);
        //Logout
        btnLogout=view.findViewById(R.id.btnLogOut);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivityOut = (MainActivity) v.getContext();
                mainActivityOut.logOut();
            }
        });

        //
        Glide.with(getContext()).load(R.drawable.welcome).into(imgmefragment);
        RecyclerView rcvMovieHistory=view.findViewById(R.id.recyclerview_History);
        rcvMovieHistory.setLayoutManager(moviesDAO.layoutRecycleHorizontalManager(getContext()));
        historyAdapter=new HistoryAdapter(getContext(),historyMovie);
        rcvMovieHistory.setAdapter(historyAdapter);
        moviesDAO.loadFirebaseHistoryMovie("/user/"+mainActivity.getIdUser()+"/history",historyMovie,historyAdapter);
       return view;
    }


}