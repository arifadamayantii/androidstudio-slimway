package com.example.loginform;
public class KateringModel {
    private int id;
    private String emailUser, menuCategory, packageOption, menuName, description, imagePath, storeLocation;
    private int calories;
    private double price;

    public KateringModel(int id, String emailUser, String menuCategory, String packageOption, String menuName,
                         String description, int calories, double price, String imagePath, String storeLocation) {
        this.id = id;
        this.emailUser = emailUser;
        this.menuCategory = menuCategory;
        this.packageOption = packageOption;
        this.menuName = menuName;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.imagePath = imagePath;
        this.storeLocation = storeLocation;
    }

    // Getter methods
    public int getId() { return id; }
    public String getEmailUser() { return emailUser; }
    public String getMenuCategory() { return menuCategory; }
    public String getPackageOption() { return packageOption; }
    public String getMenuName() { return menuName; }
    public String getDescription() { return description; }
    public int getCalories() { return calories; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public String getStoreLocation() { return storeLocation; }
}
