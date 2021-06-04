package com.example.cookingrecipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookingrecipe.Model.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class NewFeedAdapter extends RecyclerView.Adapter<NewFeedAdapter.NewFeedViewHolder>{

    private List<Recipe> recipeList;

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public NewFeedAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public NewFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.newfeed_card_recyclerview,parent,false);
        return new NewFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewFeedViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getRecipeName());
        holder.author.setText(recipe.getAuthor());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference image = storageReference.child(recipe.getImagePath().toString());
        image.getBytes(1024*1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.image.setImageBitmap(bitmap);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(recipeList!=null){
            return recipeList.size();
        }
        else return 0;
    }

    public class NewFeedViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView author,recipeName;
        public NewFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.newfeed_recipe_img);
            author = itemView.findViewById(R.id.newfeed_recipe_author);
            recipeName = itemView.findViewById(R.id.newfeed_recipe_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    System.out.println(pos);
                    System.out.println(recipeList.get(pos).getRecipeName());
                    System.out.println(recipeList.get(pos).getImagePath());
                    Intent intent = new Intent(v.getContext(),RecipeDetail.class);
//                    intent.putExtra("recipeName",recipeList.get(pos).getRecipeName());
//                    intent.putExtra("recipeAthor",recipeList.get(pos).getAuthor());
//                    intent.putExtra("recipeDescription",recipeList.get(pos).getDescription());
//                    intent.putExtra("recipeForNumber",recipeList.get(pos).getForNumber());
//                    intent.putExtra("tutorial",recipeList.get(pos).getTutorial());
//                    intent.putExtra("ingridient", (Parcelable) recipeList.get(pos).getIngridient());
                    intent.putExtra("Recipe", recipeList.get(pos));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
