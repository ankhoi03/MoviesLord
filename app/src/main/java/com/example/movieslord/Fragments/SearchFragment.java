package com.example.movieslord.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieslord.Activities.MainActivity;
import com.example.movieslord.Adapter.SearchAdapter;
import com.example.movieslord.DAO.MoviesDAO;
import com.example.movieslord.Models.MoviesModel;
import com.example.movieslord.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class SearchFragment extends Fragment {
    private List<MoviesModel>allMovies=new ArrayList<>(); ;
    private MoviesDAO moviesDAO =new MoviesDAO();
    private SearchAdapter adapter;
    private SearchView searchView;
    private RecyclerView rcvSearchMovie;
    private TextView textKQ;
    private ImageView micSearch;
    private final int REQUEST_CODE = 100;
    ArrayList<MoviesModel> list = new ArrayList<>();
    static String searchText = "";

    private MainActivity mainActivity;
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        rcvSearchMovie=view.findViewById(R.id.mRecyclerview);
        searchView=view.findViewById(R.id.mSearchView);
        textKQ=view.findViewById(R.id.tvkqSearch);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // Log.d(TAG, "onQueryTextChange: ");
               // filterList(newText);
                textKQ.setText(newText);
                listSearch(newText.trim());
                return true;
            }
        });
        micSearch=view.findViewById(R.id.micSearch);
        micSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                }catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        adapter=new SearchAdapter(getContext(),allMovies);
        rcvSearchMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSearchMovie.setAdapter(adapter);
        moviesDAO.loadFirebaseAllmovies("korea",allMovies,adapter);
        moviesDAO.loadFirebaseAllmovies("china",allMovies,adapter);
        moviesDAO.loadFirebaseAllmovies("viet",allMovies,adapter);
        moviesDAO.loadFirebaseAllmovies("cartoon",allMovies,adapter);
        moviesDAO.loadFirebaseAllmovies("thai",allMovies,adapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchView.setQuery(res.get(0),onQueryTextChange(res.get(0)));
                }
                break;
            }
            default:{
                Toast.makeText(getContext(),"haha",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void filterList(String text) {
        List<MoviesModel>filteredList=new ArrayList<>();
        for (MoviesModel movie:allMovies) {
            if(movie.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(movie);
            }
        }
        if(filteredList.isEmpty()){
            rcvSearchMovie.setVisibility(View.INVISIBLE);
        }else {
            rcvSearchMovie.setVisibility(View.VISIBLE);
            adapter.setFilteredList(filteredList);

        }
    }

    public static String convert(String str) {
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");
        return str;
    }
    public boolean onQueryTextChange(String newText){
        Log.d("TAG................................................................", "onQueryTextChange: ");
        listSearch(newText);
        return false;
    }
    public void listSearch(String newText){
        List<MoviesModel> textList = new ArrayList<>();
        List<String> textList2 = new ArrayList<>();
        Map<String, Integer> textList_ = new HashMap<>();
        textKQ.setText(newText.trim());
        searchText = newText.trim().toLowerCase();
        list.clear();
        String[] text_array_ = searchText.split(" ");
        List<String> list_loc_ = new ArrayList<>();
        for (String text : text_array_){
            if(text.trim().length() != 0){
                list_loc_.add(text.trim());
            }
        }
        int ok = 0;
        for (MoviesModel ten : allMovies){
            String tentam = ten.getTitle().trim().toLowerCase();
            if(searchText.equals(convert(searchText))){
                tentam = convert(tentam);
            }
            int chu = 0;
            for(String text : list_loc_){
                if(tentam.contains(" "+text+" ")){
                    chu++;
                }else if(tentam.contains(text)){
                    String[] tenchu = tentam.split(" ");
                    for(String t : tenchu){
                        if(t.trim().length() != 0 &&
                                t.trim().length() >= text.length()){
                            if(text.equalsIgnoreCase(t.trim().substring(0, text.length()))){
                                chu++;
                                break;
                            }
                        }
                    }
                }
            }
            textList_.put(ten.getTitle(),chu);
            if(chu > ok){
                ok = chu;
            }
        }
        if(newText.trim().equals("")){
            list.addAll(allMovies);
        }else{
            Set<String> keys = textList_.keySet();
            for(String key : keys){
                int valuekey = textList_.get(key);
                if(valuekey == ok && ok > 0){
                    textList2.add(key);
                }
            }
            for (String ten : textList2) {
                for(MoviesModel movie : allMovies){
                    if(movie.getTitle().toLowerCase().contains(ten.toLowerCase())){
                        textList.add(movie);
                        break;
                    }
                }

            }
            list.addAll(textList);
        }
        //adapter.notifyDataSetChanged();
        adapter.setFilteredList(list);
    }
}