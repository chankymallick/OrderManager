/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.users.pojo;
import java.util.ArrayList;
/**
 *
 * @author Maliick
 */
public class User {
    private String UserId;
    private String UserFullName;
    private String UserType;
    private ArrayList <Permission> Permissions;    
    private String LanguagePreference;
    private boolean Enabled;

    public boolean isEnabled() {
        return Enabled;
    }
    public void setEnabled(boolean Enabled) {
        this.Enabled = Enabled;
    }        
    public String getLanguagePreference() {
        return LanguagePreference;
    }
    public void setLanguagePreference(String LanguagePreference) {
        this.LanguagePreference = LanguagePreference;
    }  
    public String getUserId() {
        return UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
    public String getUserFullName() {
        return UserFullName;
    }
    public void setUserFullName(String UserFullName) {
        this.UserFullName = UserFullName;
    }
    public String getUserType() {
        return UserType;
    }
    public void setUserType(String UserType) {
        this.UserType = UserType;
    }
    public ArrayList<Permission> getPermissions() {
        return Permissions;
    }
    public void setPermissions(ArrayList<Permission> Permissions) {
        this.Permissions = Permissions;
    }
}
