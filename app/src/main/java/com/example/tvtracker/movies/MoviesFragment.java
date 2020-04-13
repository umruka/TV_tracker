package com.example.tvtracker.movies;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoviesFragment extends Fragment {

    private MoviesViewModel mViewModel;
    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Sherlock Holmes","Action",4,2020));
        movies.add(new Movie("Qsen li sum","Comedy",8,2018));
        movies.add(new Movie("Koi e","Horror",4,2010));
        MovieAdapter movieAdapter = new MovieAdapter();
        movieAdapter.setMovies(movies);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        // TODO: Use the ViewModel
        //RecyclerView recyclerView =  findViewById(R.id.recycler_view);
        //recyclerView.se


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
