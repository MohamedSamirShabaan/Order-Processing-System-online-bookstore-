package model;
import controller.Controller;





import java.sql.*;
import java.util.*;

public class User {
  private String userName ;
  private String password ;
  private String firstName ;
  private String lastName ;
  private String email ;
  private String phoneNumber ;
  private String shippingAddress ;
  private int salesNumber;

  private LinkedHashMap<Book,Integer> shoppingCart ;
  
  public User(){
    this.shoppingCart = new LinkedHashMap<Book,Integer>();
    
  }
 
  
  public void editInformation() throws SQLException{
    String query = "Update `user` SET"
        + " password = " 
        +"\'"+ this.password +"\'"+" ,"
        +"FName = " 
        +"\'"+ this.firstName +"\'"+ " ,"
        +"LName = " 
        +"\'"+ this.lastName + "\' ,"
        +"email = " 
        +"\'"+ this.email +"\'";
    if(this.phoneNumber != null){
       query  += " , phoneNumber = " + "\'"+this.phoneNumber+"\'" ;
    }
    if(this.shippingAddress != null){
      query  += " , shippingAddress = " +"\'"+ this.shippingAddress +"\'";
    }
    query +=" WHERE name =  " + "\'"+this.userName+"\'";
    Controller.stmt.executeUpdate(query);  
  }
  
  public ResultSet searchBook(String attrubite , String value) throws SQLException{
    if(attrubite.equals("author")){
      return searchBookByAuthor(value);
    }
    
    String query;
    if(attrubite.equals("ISBN") ||attrubite.equals("price") || attrubite.equals("numberOfCopies") ||  attrubite.equals("threshold")){
      query= "Select * from Book where " + attrubite + " = " + value;
    }else{
      query= "Select * from Book where " + attrubite + " = " +"\'" +value+"\'";

    }
    
    return Controller.stmt.executeQuery(query); 
  }
  
  private ResultSet searchBookByAuthor(String author) throws SQLException{
    String query = "Select * from book where ISBN in ( select "
        + "ISBN from bookAuthor where authorName = " 
        +"\'"+ author +"\'"+ " )";
    return Controller.stmt.executeQuery(query); 
  }
  

  public void addBookToShoppingCart(Book book , int tobuy){
    this.shoppingCart.put(book,tobuy);
  }
  public void addBookToShoppingCart(Book book){
    this.shoppingCart.put(book,1);
  }
  public void removeBookFromShoppingCart(int ISBN){
    for(Book book : this.shoppingCart.keySet()){
       if(book.getISBN() == ISBN){
         this.shoppingCart.remove(book);
         break;
       }
    }
  }
  public double getShoppingCartPrice(){
    double t = 0;
    for(Book book : this.shoppingCart.keySet()){
      t += book.getPrice()*this.shoppingCart.get(book);
    }
    return t;
  }
  
  public void checkOut() throws SQLException {
    for(Book book : this.shoppingCart.keySet()){
      int tobuy = this.shoppingCart.get(book);
      int numberOfCopies = book.getNumberOfCopies()-tobuy;
      String query = "Update book" 
          +" SET  numberOfCopies = " + numberOfCopies ;
      query  += " WHERE ISBN =  " + book.getISBN();
      Controller.stmt.executeUpdate(query); 
      String add = "Insert into sales (ISBN,userName,sellingDate,"
          + "sellingTime,salesNumber,price)"
          + "values (" + book.getISBN()+ " ,"
          + " "
          +"\'"+this.userName +"\'"+ " ,"
          + " current_date() ,  current_time() , "+ tobuy + " , " + tobuy * book.getPrice() + " ) ";
      Controller.stmt.executeUpdate(add); 
    }
    this.shoppingCart = new LinkedHashMap<Book,Integer>();

  }
  
  
  public LinkedHashMap<Book, Integer> getShoppingCart() {
    return this.shoppingCart;
  }
  
  // Setter and Getters

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getShippingAddress() {
    return shippingAddress;
  }
  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }
  
  public void promote() throws SQLException{
    String query = "Update `user` SET isManager = true";
    query +=" WHERE name =  " + "\'"+this.userName+"\'";
    Controller.stmt.executeUpdate(query);  
  }
  public int getSalesNumber() {
    return salesNumber;
  }
  public void setSalesNumber(int salesNumber) {
    this.salesNumber = salesNumber;
  }
  
  public void removeAll(){
	this.shoppingCart = new LinkedHashMap<Book,Integer>();
  }
  
}
