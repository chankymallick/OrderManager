/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.backupmanager;

import com.ordermanager.security.FileCryptoUtils;
import com.ordermanager.utility.DAOHelper;
import java.sql.ResultSet;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Maliick
 */
public class BackUpSQLServer {

    public static String createBackUpFile(String FilePath, String FileName, String DatabaseName, JdbcTemplate jdbcTemplate) {
        try {
            jdbcTemplate.execute("BACKUP DATABASE " + DatabaseName + " TO DISK ='" + FilePath.concat(FileName) + "'");
            return "SUCCESS";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
