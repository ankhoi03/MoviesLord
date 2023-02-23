package com.example.movieslord.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.movieslord.Adapter.MoviesAdapter;
import com.example.movieslord.Adapter.SliderAdapter;
import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.SliderModel;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;

import com.google.firebase.firestore.FirebaseFirestore;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {
    private List<SliderModel> sliderModels;
    private List<MoviesModel> KmoviesModels =new ArrayList<>();
    private List<MoviesModel> CmoviesModels =new ArrayList<>();
    private List<MoviesModel> VmoviesModels =new ArrayList<>();
    private List<MoviesModel> CtmoviesModels =new ArrayList<>();
    private List<MoviesModel> TmoviesModels = new ArrayList<>();
    private SliderAdapter sliderAdapter;
    private MoviesAdapter KmoviesAdapter;
    private MoviesAdapter CmoviesAdapter;
    private MoviesAdapter VmoviesAdapter;
    private MoviesAdapter CtmoviesAdapter;
    private MoviesAdapter TmoviesAdapter;

    private MoviesDAO moviesDAO =new MoviesDAO();
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView rcvMovieKorean=view.findViewById(R.id.rcvMovieKorean);
        RecyclerView rcvMovieChina=view.findViewById(R.id.rcvMovieChina);
        RecyclerView rcvMovieVie=view.findViewById(R.id.rcvMovieVie);
        RecyclerView rcvMovieCartoon=view.findViewById(R.id.rcvMovieCartoon);
        RecyclerView rcvMovieThai=view.findViewById(R.id.rcvMovieThai);
        //SliderView
        SliderView sliderView=view.findViewById(R.id.imageSlider);
        sliderAdapter=new SliderAdapter(getContext(), sliderModels);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
        reNewItem(sliderView);
        moviesDAO.loadFirebaseForSlider(sliderModels,sliderAdapter);


        //RecycleView  Movies

        rcvMovieKorean.setLayoutManager(moviesDAO.layoutRecycleHorizontalManager(getContext()));
        rcvMovieChina.setLayoutManager(moviesDAO.layoutRecycleHorizontalManager(getContext()));
        rcvMovieVie.setLayoutManager(moviesDAO.layoutRecycleHorizontalManager(getContext()));
        rcvMovieCartoon.setLayoutManager(moviesDAO.layoutRecycleHorizontalManager(getContext()));
        rcvMovieThai.setLayoutManager(moviesDAO.layoutRecycleHorizontalManager(getContext()));
        KmoviesAdapter =new MoviesAdapter(getContext(), KmoviesModels);
        CmoviesAdapter =new MoviesAdapter(getContext(), CmoviesModels);
        VmoviesAdapter =new MoviesAdapter(getContext(), VmoviesModels);
        CtmoviesAdapter=new MoviesAdapter(getContext(),CtmoviesModels);
        TmoviesAdapter=new MoviesAdapter(getContext(),TmoviesModels);
        rcvMovieKorean.setAdapter(KmoviesAdapter);
        rcvMovieChina.setAdapter(CmoviesAdapter);
        rcvMovieVie.setAdapter(VmoviesAdapter);
        rcvMovieCartoon.setAdapter(CtmoviesAdapter);
        rcvMovieThai.setAdapter(TmoviesAdapter);
        moviesDAO.loadFirebaseMovie("korea",KmoviesModels,KmoviesAdapter);
        moviesDAO.loadFirebaseMovie("china",CmoviesModels,CmoviesAdapter);
        moviesDAO.loadFirebaseMovie("viet",VmoviesModels,VmoviesAdapter);
        moviesDAO.loadFirebaseMovie("cartoon",CtmoviesModels,CtmoviesAdapter);
        moviesDAO.loadFirebaseMovie("thai",TmoviesModels,TmoviesAdapter);
        return view;
    }




    public void reNewItem(View view){
        sliderModels =new ArrayList<>();
        SliderModel dataItems=new SliderModel();
        sliderModels.add(dataItems);
        sliderAdapter.renewItems(sliderModels);
        sliderAdapter.deleteItems(0);
    }


}