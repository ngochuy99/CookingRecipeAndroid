package com.example.cookingrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cookingrecipe.Model.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecipeDetail extends AppCompatActivity {
    private TextView recipeName,description,forNumber,cookedTime,tutorial;
    private ImageView recipe_image;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("Recipe");
        recipe_image = findViewById(R.id.recipe_detail_image);
        recipeName = findViewById(R.id.recipe_detail_name);
        description = findViewById(R.id.recipe_detail_description);
        forNumber = findViewById(R.id.recipe_detail_fornumber);
        cookedTime = findViewById(R.id.recipe_detail_cookedTime);
        tutorial = findViewById(R.id.recipe_detail_tutorial);
        linearLayout = findViewById(R.id.detail_ingridient);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference image = storageReference.child(recipe.getImagePath().toString());
        image.getBytes(1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            recipe_image.setImageBitmap(bitmap);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.toString());
            }
        });
        recipeName.setText(recipe.getRecipeName());
        description.setText(recipe.getDescription());
        forNumber.setText(recipe.getForNumber());
        cookedTime.setText(recipe.getCookedTime());
        tutorial.setText(recipe.getTutorial());
        List<String> list = recipe.getIngridient();
        for( String ing:list){
            TextView ingrident;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(30,10,30,0);
            ingrident = (TextView) getLayoutInflater().inflate(R.layout.textview,null);
            ingrident.setLayoutParams(params);
            ingrident.setText(ing);
            linearLayout.addView(ingrident);
        }
    }
}