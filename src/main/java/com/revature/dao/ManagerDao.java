package com.revature.dao;

import com.revature.entity.Manager;

import java.sql.*;

public class ManagerDao {
    Connection conn = ConnectionFactory.getConnection();

    public boolean managementLogin(String manager_email, String manager_password) {
        boolean isLoginSuccess = false;
        String query = "SELECT * from managers where manager_email = ? and manager_password = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, manager_email);
            ps.setString(2, manager_password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Manager manager = getManagerFromRS(rs);
            if (manager.getManager_email().equals(manager_email) && manager.getManager_password().equals(manager_password)) {
                isLoginSuccess = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return isLoginSuccess;
    }

    private Manager getManagerFromRS(ResultSet rs) {
        Manager manager = null;
        //int manager_id, String manager_name, String manager_email, String manager_password
        try {
            manager = new Manager(rs.getInt("manager_id"),
                    rs.getString("manager_name"),
                    rs.getString("manager_email"),
                    rs.getString("manager_password"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return manager;
    }
}
