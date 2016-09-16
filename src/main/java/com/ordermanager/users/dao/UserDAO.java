package com.ordermanager.users.dao;

import java.sql.ResultSet;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDAO {

    private JdbcTemplate JdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return JdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate JdbcTemplate) {
        this.JdbcTemplate = JdbcTemplate;
    }
    
    public int StudentList(){
        try {
                String query = "INSERT INTO TEST VALUES (999)";
                int x = JdbcTemplate.update(query);
                return x;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
}
