package com.revature.entity;

public class Manager {
    private int manager_id;
    private String manager_name;
    private String manager_email;
    private String manager_password;

    public Manager(int manager_id, String manager_name, String manager_email, String manager_password) {
        this.manager_id = manager_id;
        this.manager_name = manager_name;
        this.manager_email = manager_email;
        this.manager_password = manager_password;
    }

    public Manager(String manager_name, String manager_email, String manager_password) {
        this.manager_name = manager_name;
        this.manager_email = manager_email;
        this.manager_password = manager_password;
    }

    public Manager() {
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getManager_email() {
        return manager_email;
    }

    public void setManager_email(String manager_email) {
        this.manager_email = manager_email;
    }

    public String getManager_password() {
        return manager_password;
    }

    public void setManager_password(String manager_password) {
        this.manager_password = manager_password;
    }
}
