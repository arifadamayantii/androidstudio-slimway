package com.example.loginform;

import java.util.List;

public class KateringActivityModel {
        private String orderId;
        private List<String> menuNames;
        private String finalPrice;

        public KateringActivityModel(String orderId, List<String> menuNames, String finalPrice) {
            this.orderId = orderId;
            this.menuNames = menuNames;
            this.finalPrice = finalPrice;
        }

        public String getOrderId() {
            return orderId;
        }

        public List<String> getMenuNames() {
            return menuNames;
        }

        public String getFinalPrice() {
            return finalPrice;
        }
    }


