/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.users.model;

/**
 *
 * @author Maliick
 */
public class Permission {
    private String PermissionId;
    private String PermissionName;
    private boolean PermissionStatus;

    public String getPermissionId() {
        return PermissionId;
    }

    public void setPermissionId(String PermissionId) {
        this.PermissionId = PermissionId;
    }

    public String getPermissionName() {
        return PermissionName;
    }

    public void setPermissionName(String PermissionName) {
        this.PermissionName = PermissionName;
    }

    public boolean isPermissionStatus() {
        return PermissionStatus;
    }

    public void setPermissionStatus(boolean PermissionStatus) {
        this.PermissionStatus = PermissionStatus;
    }
    
}
