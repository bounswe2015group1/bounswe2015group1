package com.boun.swe.wawwe.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by Mert on 31/10/15.
 */
public class Recipe extends BaseModel implements Parcelable {


    private int userId;
    private String name;
    private String description;
    private List<String> tags;
    private List<Ingredient> ingredients;
    private Nutrition nutritions;

    public Recipe() {
        nutritions = new Nutrition();
    }

    public Recipe(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Recipe(String name, String description, List<Ingredient> ingredients) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        name = in.readString();
        description = in.readString();
        tags = in.createStringArrayList();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        nutritions = in.readParcelable(Nutrition.class.getClassLoader());
        createdAt = new Date(in.readLong());
        rating = in.readFloat();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Nutrition getNutritions() {
        return nutritions;
    }

    public void setNutritions(Nutrition nutritions) {
        this.nutritions = nutritions;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(tags);
        dest.writeTypedList(ingredients);
        dest.writeParcelable(nutritions, flags);
        dest.writeLong(createdAt.getTime());
        dest.writeFloat(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
