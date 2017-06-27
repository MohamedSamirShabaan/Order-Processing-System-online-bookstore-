package model;

import java.sql.SQLException;

import controller.Controller;

public class Order {
  private int orderId ;
  private int ISBN ;
  private int quantity ;
  public int getOrderId() {
    return orderId;
  }
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }
  public int getISBN() {
    return ISBN;
  }
  public void setISBN(int iSBN) {
    ISBN = iSBN;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
 
  public void addToOrderTable()  throws SQLException{
    String query ;
    query = "Insert into `order` (ISBN,orderId,quantity)" 
          + " values ( " + this.ISBN + " , " 
          +this.orderId + " , " 
        +this.quantity + " ) ";
    Controller.stmt.executeUpdate(query); 
  }
  public void delete() throws SQLException{
    String query = "delete from `order`";
    query +=" WHERE orderId =  " + this.orderId;
    Controller.stmt.executeUpdate(query);  
  } 
  
}
