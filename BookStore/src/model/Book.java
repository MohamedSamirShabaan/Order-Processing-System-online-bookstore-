package model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import controller.Controller;
public class Book {
  
  private int ISBN ;
  private String title ;
  private String publisherName ;
  private ArrayList<String> authors;
  private String year ;
  private double price ;
  private int numberOfCopies ;
  private int threshold ;
  private String category ;
  private int salesNumber;
  
  public Book(){
    this.price = 20;
    this.numberOfCopies = 0;
    this.threshold = 0;
    authors = new ArrayList<String>();
  }
  
  public int getSalesNumber() {
    return salesNumber;
  }
  public void setSalesNumber(int salesNumber) {
    this.salesNumber = salesNumber;
  }
  
  public int getISBN() {
    return ISBN;
  }
  public void setISBN(int iSBN) {
    this.ISBN = iSBN;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getPublisherName() {
    return publisherName;
  }
  public void setPublisherName(String publisherName) {
    this.publisherName = publisherName;
  }
  public ArrayList<String> getAuthors() {
    return authors;
  }
  public void setAuthors(ArrayList<String> authors) {
    this.authors = authors;
  }
  public String getYear() {
    return year;
  }
  public void setYear(String year) {
    this.year = year;
  }
  public double getPrice() {
    return price;
  }
  public void setPrice(double price) {
    this.price = price;
  }
  public int getNumberOfCopies() {
    return numberOfCopies;
  }
  public void setNumberOfCopies(int numberOfCopies) {
    this.numberOfCopies = numberOfCopies;
  }
  public int getThreshold() {
    return threshold;
  }
  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public void addToDatabase() throws SQLException {
    String query ;
    query = "Insert into Book (ISBN,title,publisherName,category"
          + ",numberOfCopies,price,threshold)"
          + " values ( " 
          +"\'"+ this.ISBN +"\'"+ " , " 
          +"\'"+this.title +"\'"+ " , " 
          +"\'"+this.publisherName +"\'"+ " , " 
          +"\'"+this.category +"\'"+ " , " 
          +this.numberOfCopies + " , " 
          +this.price + " , " 
          +this.threshold + " ) "; 
   
    Controller.stmt.executeUpdate(query); 
    if(!this.year.equals("")){
      this.modify();
    }
      query = "Delete from BookAuthor Where "
          + "ISBN = "+ this.ISBN;
      Controller.stmt.executeUpdate(query); 
   
    for(String author : this.authors){
      query = "Insert into BookAuthor (ISBN,authorName)"
          + "Value ( "+ this.ISBN+ " , " +"\'"+ author +"\'"+" )";
      Controller.stmt.executeUpdate(query); 
    }
    
  }
  public void modify() throws SQLException{
    String query = "Update book SET title = " +"\""+ this.title +"\""+ " ,"
        +"publisherName = " +"\""+ this.publisherName +"\""+ " ,"
        +"category = " +"\""+ this.category +"\""+ " ,"
        +"numberOfCopies = " + this.numberOfCopies + " ,"
        +"price = " + this.price + " ,"
        +"threshold = " + this.threshold ;        
    if(!this.year.equals("")){
      query += " ,year = " + "\""+this.year+"\"" ;
    }
    query  += " WHERE ISBN =  " + this.ISBN;

    Controller.stmt.executeUpdate(query); 
    query = "delete from BookAuthor where "
        + "ISBN = "+ this.ISBN ;
    Controller.stmt.executeUpdate(query); 
    for(String author : this.authors){
      query = "Insert into BookAuthor (ISBN,authorName)"
          + "Value ( "+ this.ISBN+ " , " +"\'"+ author +"\'"+" )";
      Controller.stmt.executeUpdate(query); 
    }
  }
  @Override
  public int hashCode()
  {
    return this.ISBN;
  }
  
  @Override
  public boolean equals( Object book )
  {
    if(book  instanceof Book){
      return this.ISBN == ((Book)book).ISBN;
    }
    return false;
  }
}
