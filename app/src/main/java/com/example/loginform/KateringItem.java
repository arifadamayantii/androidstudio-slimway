package com.example.loginform;

public class KateringItem {
    private String menuCategory, packageOption, menuName, description, imagePath, storeLocation;
    private int calories, id;
    private double price;

    // Tambahan: status apakah item ini dipilih
    private boolean isSelected = false;

    public KateringItem(int id, String menuCategory, String packageOption, String menuName, String description, int calories, double price, String imagePath, String storeLocation) {
        this.id = id;
        this.menuCategory = menuCategory;
        this.packageOption = packageOption;
        this.menuName = menuName;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.imagePath = imagePath;
        this.storeLocation = storeLocation;

    }

    public String getMenuCategory() { return menuCategory; }
    public String getPackageOption() { return packageOption; }
    public int getId() { return id; }
    public String getMenuName() { return menuName; }
    public String getDescription() { return description; }
    public int getCalories() { return calories; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public String getStoreLocation() { return storeLocation; }

    // Tambahan: getter & setter untuk isSelected
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
