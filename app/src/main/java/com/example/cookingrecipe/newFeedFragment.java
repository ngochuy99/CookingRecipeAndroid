package com.example.cookingrecipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookingrecipe.Model.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class newFeedFragment extends Fragment {
    private RecyclerView recyclerView;

    public newFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.newfeed_recyclerview);
        List<Recipe> recipeList = new ArrayList<>();
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
                    NewFeedAdapter newFeedAdapter = new NewFeedAdapter(recipeList);
                    newFeedAdapter.setRecipeList(recipeList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    recyclerView.setAdapter(newFeedAdapter);
                }
                else{
                    System.out.println(task.getException());
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_feed, container, false);
    }
}