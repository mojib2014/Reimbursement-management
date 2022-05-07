package com.revature.entity;

import java.sql.Timestamp;

public class Ticket {
    private int id;
    private double amount;
    private String description;
    private Timestamp timestamp;
    private String status;
    private String Category;
    private int employee_id;

    public Ticket(int id, double amount, String description, Timestamp timestamp, String status, String expenseCategory) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.status = status;
        this.Category = expenseCategory;
    }

    public Ticket(double amount, String description, String status, String expenseCategory) {
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.Category = expenseCategory;
    }

    public Ticket() {
    }

    public Ticket(double amount, String description, String category) {
        this.amount = amount;
        this.description = description;
        Category = category;
    }

    public Ticket(int id, double amount, String description, Timestamp timestamp, String status, String category, int employee_id) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.status = status;
        Category = category;
        this.employee_id = employee_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpenseCategory() {
        return Category;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.Category = expenseCategory;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", amount=" + amount + ", description='" + description + '\'' + ", timestamp=" + timestamp + ", status='" + status + '\'' + ", Category='" + Category + '\'' + ", employee_id=" + employee_id + '}';
    }
}