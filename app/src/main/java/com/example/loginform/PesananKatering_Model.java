package com.example.loginform;

import java.util.List;

public class PesananKatering_Model {
    private int orderId;
    private String name;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String startDate; // kalau kamu pakai
    private List<MenuModel> menuList;

    public PesananKatering_Model(int orderId, String name, String address, String city, String province, String postalCode,
                                 String startDate, List<MenuModel> menuList) {
        this.orderId = orderId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.startDate = startDate;
        this.menuList = menuList;
    }

    public int getOrderId() { return orderId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getPostalCode() { return postalCode; }
    public String getStartDate() { return startDate; }
    public List<MenuModel> getMenuList() { return menuList; }
}
