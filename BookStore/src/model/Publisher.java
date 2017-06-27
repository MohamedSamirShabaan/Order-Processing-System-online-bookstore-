package model;

import java.sql.SQLException;
import java.util.*;

import controller.Controller;

public class Publisher {
  private String name;
  private ArrayList<String> addresses ;
  private ArrayList<String> phoneNumbers ;
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<String> getAddresses() {
    return addresses;
  }

  public void setAddresses(ArrayList<String> addresses) {
    this.addresses = addresses;
  }

  public ArrayList<String> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  
  public Publisher(String name){
    this.name = name;
    this.addresses = new ArrayList<String>();
    this.phoneNumbers = new ArrayList<String>();
  }

  public Publisher(){
	  this.addresses = new ArrayList<String>();
	  this.phoneNumbers = new ArrayList<String>();
  }
  
  public void addPhoneNumber(String phone){
    this.phoneNumbers.add(phone);
  }
  public void addAddress(String address){
    this.addresses.add(address);
  }
  public void addPublisher() throws SQLException{
    String query = "Insert into publisher (name)"
        + "Value ( \'"+ this.name+"\' )";
    Controller.stmt.executeUpdate(query); 

    for(String phone : this.phoneNumbers){
       query = "Insert into publisherPhone (name,phone)"
          + "Value ( "
           +"\'"+ this.name+"\'"+ " , " 
          + "\'"+phone +"\'"+" )";
      Controller.stmt.executeUpdate(query); 
    }
    for(String address : addresses){
      query = "Insert into publisherAddress (name,address)"
         + "Value ( "
          +"\'"+ this.name+ "\'"+" , " 
         +"\'"+ address +"\'"+" )";
     Controller.stmt.executeUpdate(query); 
   }
  }
  
  
}
