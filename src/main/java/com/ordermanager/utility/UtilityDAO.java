/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.utility;

/**
 *
 * @author Maliick
 */
public class UtilityDAO extends DAOHelper {
    public String isValueUnique(String Value,String TableName,String ColumnName){
    return this.isValueExistInTable(Value, TableName, ColumnName);
    }    
}
