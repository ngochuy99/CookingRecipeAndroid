package com.example.cookingrecipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.cookingrecipe.Model.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class searchFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    public searchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.search_recyclerView);
        List<Recipe> recipeList = new ArrayList<>();
        NewFeedAdapter newFeedAdapter = new NewFeedAdapter(recipeList);
        newFeedAdapter.setRecipeList(recipeList);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = mDatabase.getReference();
        recipeRef.child("recipe").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Recipe recipe = new Recipe();
                        recipe.setAuthor(data.child("author").getValue().toString());
                        recipe.setRecipeName(data.child("recipeName").getValue().toString());
                        recipe.setDescription(data.child("description").getValue().toString());
                        recipe.setTutorial(data.child("tutorial").getValue().toString());
                        recipe.setForNumber(data.child("forNumber").getValue().toString());
                        recipe.setImagePath(data.child("imagePath").getValue().toString());
                        recipe.setCookedTime(data.child("cookedTime").getValue().toString());
                        List<String> listIng;
                        listIng = (List<String>) data.child("ingridient").getValue();
                        recipe.setIngridient(listIng);
                        recipeList.add(recipe);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                }
                else{
                    System.out.println(task.getException());
                }
            }
        });
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Recipe> searchList = new ArrayList<>();
                String searchText = searchView.getQuery().toString();
                for(Recipe temp:recipeList){
                    if(temp.getRecipeName().contains(searchText)){
                        searchList.add(temp);
                    }
                }
                newFeedAdapter.setRecipeList(searchList);
                recyclerView.setAdapter(newFeedAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}