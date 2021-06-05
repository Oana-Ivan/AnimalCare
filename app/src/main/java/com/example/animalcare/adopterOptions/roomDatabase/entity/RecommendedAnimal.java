package com.example.animalcare.adopterOptions.roomDatabase.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recommended_animals")
public class RecommendedAnimal {
    @PrimaryKey
    private int id;
    @NonNull
    @ColumnInfo(name = "animal_id")
    private String animalId;
    private String adopterID;

    private double age;
    private String gender;
    private String species;
    private String color;
    private String description;
    private boolean disease;
    private String image;
    private String arrivingDate;
    // TODO Breed
    private String breed;
    // 3 sizes: 1 - small, 2 - medium, 3 - big
    private int size;
    // 3 types of personality: 1 - inactive, 2 - nearly-active, 3 - active
    private int personalityType;
    // 3 levels
    private int attentionLevelRequired;
    private int caringLevelRequired;

    public RecommendedAnimal(int id, String animalId, String adopterID, double age, String gender, String species, String color, String description, boolean disease, String image, String arrivingDate, String breed, int size, int personalityType, int attentionLevelRequired, int caringLevelRequired) {
        this.id = id;
        this.animalId = animalId;
        this.adopterID = adopterID;
        this.age = age;
        this.gender = gender;
        this.species = species;
        this.color = color;
        this.description = description;
        this.disease = disease;
        this.image = image;
        this.arrivingDate = arrivingDate;
        this.breed = breed;
        this.size = size;
        this.personalityType = personalityType;
        this.attentionLevelRequired = attentionLevelRequired;
        this.caringLevelRequired = caringLevelRequired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getAdopterID() {
        return adopterID;
    }

    public void setAdopterID(String adopterID) {
        this.adopterID = adopterID;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisease() {
        return disease;
    }

    public void setDisease(boolean disease) {
        this.disease = disease;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArrivingDate() {
        return arrivingDate;
    }

    public void setArrivingDate(String arrivingDate) {
        this.arrivingDate = arrivingDate;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPersonalityType() {
        return personalityType;
    }

    public void setPersonalityType(int personalityType) {
        this.personalityType = personalityType;
    }

    public int getAttentionLevelRequired() {
        return attentionLevelRequired;
    }

    public void setAttentionLevelRequired(int attentionLevelRequired) {
        this.attentionLevelRequired = attentionLevelRequired;
    }

    public int getCaringLevelRequired() {
        return caringLevelRequired;
    }

    public void setCaringLevelRequired(int caringLevelRequired) {
        this.caringLevelRequired = caringLevelRequired;
    }
}
