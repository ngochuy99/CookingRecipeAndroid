package com.example.cookingrecipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cookingrecipe.Model.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class postFragment extends Fragment {
    private Button btnAddIngredient,btnSave;
    private EditText cookedTime,forNumber,recipeName,description,tutorial;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 111;
    private Bitmap photo;
    public postFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        List<EditText> ingredient = new ArrayList<>();
        btnAddIngredient = getView().findViewById(R.id.btnAddNewIngridient);
        linearLayout = getView().findViewById(R.id.ingridientLayout);
        btnSave = getView().findViewById(R.id.btnSave);
        imageView = getView().findViewById(R.id.recipe_Image);
        recipeName = getView().findViewById(R.id.recipe_name);
        cookedTime = getView().findViewById(R.id.cookedtime);
        description = getView().findViewById(R.id.recipe_description);
        forNumber = getView().findViewById(R.id.fornumber);
        tutorial = getView().findViewById(R.id.tutorial);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        btnAddIngredient.setOnClickListener(v -> {
            EditText ing = new EditText(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(30,10,30,0);
            ing = (EditText) getLayoutInflater().inflate(R.layout.edittext,null);
            ing.setLayoutParams(params);
            linearLayout.addView(ing);
            ingredient.add(ing);
        });
        btnSave.setOnClickListener(v->{

            FirebaseStorage storage = FirebaseStorage.getInstance();
            DatabaseReference root = mDatabase.getReference();
            DatabaseReference recipeRef = root.child("recipe");
            String key = recipeRef.push().getKey();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/"+key+".jpg");
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100,boas);
            byte[] data = boas.toByteArray();
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println(taskSnapshot);
                }
            });

            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeName.getText().toString());
            recipe.setCookedTime(cookedTime.getText().toString());
            recipe.setDescription(description.getText().toString());
            recipe.setForNumber(forNumber.getText().toString());
            recipe.setTutorial(tutorial.getText().toString());
            recipe.setAuthor(user.getDisplayName());
            recipe.setImagePath("images/"+key+".jpg");
            List<String> list = new ArrayList<>();
            for(EditText e:ingredient){
                list.add(e.getText().toString());
            }
            recipe.setIngridient(list);
            recipeRef.child(key).setValue(recipe);
            Toast.makeText(this.getContext(), "Chia sẻ thành công", Toast.LENGTH_LONG).show();
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(photoPickerIntent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(photoPickerIntent,CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST&&resultCode == Activity.RESULT_OK){
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            return;
        }
    }
}