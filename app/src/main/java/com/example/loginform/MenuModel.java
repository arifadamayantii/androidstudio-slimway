package com.example.loginform;

public class MenuModel {
    private String menuName;
    private double price;
    private int calories;

    public MenuModel(String menuName, double price, int calories) {
        this.menuName = menuName;
        this.price = price;
        this.calories = calories;
    }

    public String getMenuName() { return menuName; }
    public double getPrice() { return price; }
    public int getCalories() { return calories; }
}
