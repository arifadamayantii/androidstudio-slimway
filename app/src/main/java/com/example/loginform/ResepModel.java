package com.example.loginform;

public class ResepModel {
    private String id, namaResep, kategori, kalori, deskripsi, photoPath, ingredients, instructions, videoPath, status
            , protein, fat, carbs;
    private int isBreakfast, isLunch, isDinner;

    public ResepModel(String id, String namaResep, String kategori, String kalori, String deskripsi, String photoPath,
                      String ingredients, String instructions, String videoPath, String status , String protein, String fat, String carbs, int isBreakfast, int isLunch, int isDinner) {
        this.id = id;
        this.namaResep = namaResep;
        this.kategori = kategori;
        this.kalori = kalori;
        this.deskripsi = deskripsi;
        this.photoPath = photoPath;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.videoPath = videoPath;
        this.status = status;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.isBreakfast = isBreakfast;
        this.isLunch = isLunch;
        this.isDinner = isDinner;
    }

    // Getters and setters for the fields
    public String getId() { return id; }
    public String getNamaResep() { return namaResep; }
    public String getKategori() { return kategori; }
    public String getKalori() { return kalori; }
    public String getDeskripsi() { return deskripsi; }
    public String getPhotoPath() { return photoPath; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getVideoPath() { return videoPath; }
    public String getStatus() { return status; }
    public String getProtein() { return protein; }
    public String getFat() { return fat; }
    public String getCarbs() { return carbs; }
    public int getIsBreakfast() { return isBreakfast; }
    public int getIsLunch() { return isLunch; }
    public int getIsDinner() { return isDinner; }
}
