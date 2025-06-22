package com.example.loginform;

public class UserModel {
    private String id;
    private String statusAkun;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String status;

    public UserModel(String id, String statusAkun, String firstName, String lastName,
                     String email, String phone, String password, String status) {
        this.id = id;
        this.statusAkun = statusAkun;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.status = status;

    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getStatusAkun() {
        return statusAkun;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}