package com.example.movieslord.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieslord.Activities.MainActivity;
import com.example.movieslord.Adapter.FavoriteAdapter;
import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    private List<MoviesModel> favMovie =new ArrayList<>();
    private FavoriteAdapter moviesAdapter;
    private RecyclerView recyclerFavorite;
    private MoviesDAO moviesDAO =new MoviesDAO();

    public FavoriteFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_favorite, container, false);
        MainActivity mainActivity= (MainActivity) view.getContext();
         recyclerFavorite=view.findViewById(R.id.recyclerview_Favorite);
         GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
//         gridLayoutManager.setReverseLayout(true);
        recyclerFavorite.setLayoutManager(gridLayoutManager);
        moviesAdapter=new FavoriteAdapter(getContext(),favMovie);
        recyclerFavorite.setAdapter(moviesAdapter);
        moviesDAO.loadFirebaseFavorteMovie("/user/"+mainActivity.getIdUser()+"/favorite",favMovie,moviesAdapter);
        return view;
    }


}