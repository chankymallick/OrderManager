/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.order.dao;
import com.ordermanager.order.model.Order;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Maliick
 */
public class OrderDAO {
    public boolean addNewOrder(){
    return false;
    }    
    public boolean editOrder(){
    return false;
    }
    public Order getSingleOrder(int BillNo){
    return new Order();
    }      
    public List<Order> getAllOrdersByWeek(){
    return new ArrayList<>();
    }
    public List<Order> getAllOrdersByMonth(){
    return new ArrayList<>();
    }
    /*Required Parameter from/to */
    public List<Order> getAllOrdersByDate(){
    return new ArrayList<>();
    }
    
}
