package controller;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.*;

public class Controller {
  public static Statement stmt;
  public static Connection con;
  private User user;
  private static Controller controller ;
  private Controller() {
    startController();
  }
  public static Controller getInstance(){
    if(controller == null){
      controller = new Controller();
    }
    return controller;
  }

  private void startController() {
    // TODO Auto-generated method stub
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.con = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/bookstore", "root", "root");
      Controller.stmt = this.con.createStatement();

    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage());

    }

    // delete dates longer than 3 months
    String query = "delete from `sales`"
        + "WHERE sellingDate  < (current_Date() - Interval 3 Month)";
    try {
      Controller.stmt.executeUpdate(query);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());

    }
  }

  // if user not found ( wrong password or user name ) return null
  public User login(String name, String password) {
    String query = "select * from `user`" + "WHERE"
        + " name  = " +" \'"+ name+"\' "+" and password = "+"\'"+  password+"\'";
    try {
      User us = null;
      ResultSet rs = Controller.stmt.executeQuery(query);
      if (rs.next()) {
        boolean isManager = rs.getBoolean("isManager");
        if (isManager) {
          us = new Manager();
        } else {
          us = new User();
        }
        us.setEmail(rs.getString("Email"));
        us.setFirstName(rs.getString("Fname"));
        us.setLastName(rs.getString("Lname"));
        us.setPassword(rs.getString("password"));
        us.setPhoneNumber(rs.getString("phoneNumber"));
        us.setShippingAddress(rs.getString("shippingAddress"));
        us.setUserName(rs.getString("name"));
      }
      if(us == null){
        JOptionPane.showMessageDialog(null, "Wrong UserName or Password");

      }
      this.user = us;
      return us;
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
    JOptionPane.showMessageDialog(null, "Wrong UserName or Password");

    return null;
  }

  public boolean signup(User user) {
    String query = new String();
    if (user.getShippingAddress() != null && user.getPhoneNumber() != null) {
      query = "Insert into `user` (name,password,Lname,Fname"
          + ",Email,phoneNumber,shippingAddress)" + " "
          + "values ( "
          + "\'"+ user.getUserName() +"\'"+  " ,"
          + "\'"+ user.getPassword() +"\'"+ " , "
          + "\'"+ user.getLastName() +"\'"+  " , "
          + "\'"+ user.getFirstName()+"\'"+ " , "
          + "\'"+ user.getEmail()    +"\'" + " , " 
          + "\'"+ user.getPhoneNumber() +"\'"+  " , "
          + "\'"+ user.getShippingAddress() +"\'"+  " ) ";
    } else if (user.getShippingAddress() == null
        && user.getPhoneNumber() == null) {
      query = "Insert into `user` (name,password,Lname,Fname" + ",Email)"
          + " values ( " 
          + "\'"+user.getUserName() +"\'"+ " , "
          + "\'"+user.getPassword() +"\'"+ " , " 
          + "\'"+user.getLastName() +"\'"+ " , " 
          + "\'"+user.getFirstName()+"\'"+ " , "
          + "\'"+user.getEmail() +"\'"+ " ) ";
    } else if (user.getShippingAddress() == null) {
      query = "Insert into `user` (name,password,Lname,Fname"
          + ",Email,phoneNumber)" + " values ( " 
          +"\'"+ user.getUserName() +"\'"+ " , "
          +"\'"+ user.getPassword() +"\'"+ " , "
          +"\'"+ user.getLastName() +"\'"+ " , "
          +"\'"+ user.getFirstName() +"\'"+ " , "
          +"\'"+user.getEmail() +"\'"+ " , "
          +"\'"+ user.getPhoneNumber() + "\'"+" ) ";
    } else {
      query = "Insert into `user` (name,password,Lname,Fname"
          + ",Email,shippingAddress)" + " values ( " 
          +"\'"+ user.getUserName() +"\'"+ " , "
          +"\'"+ user.getPassword() +"\'"+ " , " 
          +"\'"+ user.getLastName() +"\'"+ " , "
          +"\'"+ user.getFirstName() +"\'"+ " , " 
          +"\'"+ user.getEmail() +"\'"+ " , "
          +"\'"+ user.getShippingAddress() + "\'"+" ) ";
    }
    try {
      Controller.stmt.executeUpdate(query);
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
      return false;
    }
    return true;

  }

  // ----------------------------------------------- user
  // --------------------------------------------
  public boolean editInformation(User us) {
    try {
      us.editInformation();
      this.user = us;
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
      return false;
    }
    return true;
  }

  public ArrayList<Book> searchBook(String attrubite, String value) {
    try {
      ResultSet rs = (this.user).searchBook(attrubite, value);
      ArrayList<Book> books = new ArrayList<>();
      while (rs.next()) {
        Book book = new Book();
        book.setCategory(rs.getString("category"));
        book.setISBN(rs.getInt("ISBN"));
        book.setNumberOfCopies(rs.getInt("numberOfCopies"));
        book.setPrice(rs.getDouble("price"));
        book.setPublisherName(rs.getString("publisherName"));
        book.setThreshold(rs.getInt("threshold"));
        book.setTitle(rs.getString("title"));
        book.setYear(rs.getString("year"));
        books.add(book);
       
      }
      for(Book book:books){
        String query = " Select authorName from bookauthor where ISBN = "+book.getISBN() ;
        ResultSet rs2 =  Controller.stmt.executeQuery(query); 
        ArrayList<String> authors = new ArrayList<>();
        while (rs2.next()) {
          authors.add(rs2.getString("authorName"));
        }
        book.setAuthors(authors);
      }
      return books;
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return null;
  }

  public boolean checkout() {
    try {
      this.con.setAutoCommit(false);
      this.user.checkOut();
      this.con.commit();
      JOptionPane.showMessageDialog(null,"Commit Done");

    } catch (SQLException e1) {
      try {
        this.con.rollback();
        JOptionPane.showMessageDialog(null,"RollBack Done");

        return false;
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        return false;
      }
    } finally {

      try {
        this.con.setAutoCommit(true);
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        return false;
      }
    }
    return true;

  }

  // -----------------------------------manager--------------------------
  public boolean addBook(Book book) {
    if (user instanceof Manager) {
      try {
        ((Manager) this.user).addBook(book);
      } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage());
        return false;
      }
    } else {
      
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return false;
    }
    return true;
  }

  public boolean modifyBook(Book book) {
    if (user instanceof Manager) {
      try {
        ((Manager) this.user).modifyBook(book);
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        // View.showError(e.getMessage());
        return false;
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");
      // View.showError("NOT ALLOWED");
      return false;
    }
    return true;
  }

  public boolean placeOrder(Order order) {
    if (user instanceof Manager) {
      try {
        ((Manager) this.user).placeOrder(order);
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
        return false;
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return false;
    }
    return true;
  }

  public boolean confirmOrder(Order order) {
    if (user instanceof Manager) {
      try {
        ((Manager) this.user).confirmOrder(order);
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
        return false;
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return false;
    }
    return true;
  }

  public boolean promoteCustomer(User user) {
    if (user instanceof Manager) {
      try {
        ((Manager) this.user).promoteCustomer(user);
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
        return false;
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return false;
    }
    return true;

  }

  public Double getTotalSales() {
    if (user instanceof Manager) {
      try {
        ResultSet rs = ((Manager) this.user).getTotalSales();
        if (rs.next()) {
          return rs.getDouble(1);
        } else {
          return 0.0;
         }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
    }
    return null;
  }

  public ArrayList<User> getTopFiveCustomers() {
    if (user instanceof Manager) {
      try {
        ResultSet rs = ((Manager) this.user).getTopFiveCustomers();
        ArrayList<User> users = new ArrayList<>();
        while (rs.next()) {
          User us = new User();
          us.setSalesNumber(rs.getInt("sum(salesNumber)"));
          us.setUserName(rs.getString("userName"));
          users.add(us);
        }
        return users;
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
    }
    return null;
  }

  public ArrayList<Book> getTopTenBooks() {
    if (user instanceof Manager) {
      try {
        ResultSet rs = ((Manager) this.user).getTopTenBooks();
        ArrayList<Book> books = new ArrayList<>();
        while (rs.next()) {
          Book book = new Book();
          book.setISBN(rs.getInt("ISBN"));
          book.setSalesNumber(rs.getInt("sum(salesNumber)"));
          books.add(book);
        }
        return books;
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
    }
    return null;
  }

  public ArrayList<User> getAllCustomers() {
    if (user instanceof Manager) {
      try {
        ResultSet rs = ((Manager) this.user).getAllCustomers();
        ArrayList<User> users = new ArrayList<>();
        while (rs.next()) {
          boolean isManager = rs.getBoolean("isManager");
          User us;
          if (isManager) {
            us = new Manager();
          } else {
            us = new User();
          }
          us.setEmail(rs.getString("Email"));
          us.setFirstName(rs.getString("Fname"));
          us.setLastName(rs.getString("Lname"));
          //us.setPassword(rs.getString("password"));
          us.setPhoneNumber(rs.getString("phoneNumber"));
          us.setShippingAddress(rs.getString("shippingAddress"));
          us.setUserName(rs.getString("name"));
          users.add(us);
        }
        return users;
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
    }
    return null;
  }

  public boolean promote(User us) {
    if (user instanceof Manager) {
      try {
        us.promote();

      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
        return false;

      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return false;

    }
    return true;

  }

  public boolean addPublisher(Publisher publisher) {
    if (user instanceof Manager) {
      try {
        ((Manager) this.user).addPublisher(publisher);

      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // View.showError(e.getMessage());
        return false;

      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return false;

    }
    return true;

  }
  public ArrayList<Order> getAllOrder(){
    ArrayList<Order> orders = new ArrayList<Order>();
    if (user instanceof Manager) {
      try {
        ResultSet rs = ((Manager) this.user).getAllOrder();

        while (rs.next()) {
          Order o = new Order();
          o.setOrderId(rs.getInt("OrderId"));
          o.setISBN(rs.getInt("ISBN"));
          o.setQuantity(rs.getInt("quantity"));
          orders.add(o);
          
        }
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        // View.showError(e.getMessage());
        return null;

      }
    } else {
      JOptionPane.showMessageDialog(null, "Unauthorized Access");

      // View.showError("NOT ALLOWED");
      return null;

    }
    return orders;
  }
  public void close(){
    try {
    con.close();
    System.out.println("closed");
  } catch (SQLException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  }

}
