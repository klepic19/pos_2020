package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private int ID;
    private Date date;
    private String number;
    private int user;

    public Bill(int id, Date date, String number, int user) {
        this.ID = id;
        this.date = date;
        this.number = number;
        this.user = user;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public static Bill add(Bill b) {
        try {
            PreparedStatement stmnt = Database.CONNECTION.prepareStatement("INSERT INTO racun VALUES (null, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmnt.setDate(1, b.getDate());
            stmnt.setString(2, b.getNumber());
            stmnt.setInt(3, b.getUser());
            stmnt.executeUpdate();

            ResultSet rs = stmnt.getGeneratedKeys();
            if (rs.next()) {
                b.setID(rs.getInt(1));
            }
            return b;
        } catch (SQLException e) {
            System.out.println("Nisam uspio dodati račun: " + e.getMessage());
            return null;
        }
    }

    public static boolean remove(Bill b) {
        try {
            PreparedStatement stmnt = Database.CONNECTION.prepareStatement("DELETE FROM racun WHERE id=?");
            stmnt.setInt(1, b.getID());
            stmnt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Nisam uspio pobrisati racun: " + e.getMessage());
            return false;
        }
    }

    public static boolean update(Bill b) {
        try {
            PreparedStatement stmnt = Database.CONNECTION.prepareStatement("UPDATE racun SET datum=?, broj=?, izdao_korisnik_fk=?  WHERE id=?");
            stmnt.setDate(1, b.getDate());
            stmnt.setString(2, b.getNumber());
            stmnt.setInt(3, b.getUser());
            stmnt.setInt(4, b.getID());
            stmnt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Nisam uspio urediti racun: " + e.getMessage());
            return false;
        }
    }

    public static List<Bill> select() {
        List<Bill> bills = new ArrayList<>();
        try {
            Statement stmnt = Database.CONNECTION.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM racun");


            while(rs.next()){
                bills.add(new Bill(
                        rs.getInt(1),
                        rs.getDate(2),
                        rs.getString(3),
                        rs.getInt(4)
                ));
            }
            return bills;
        } catch (SQLException e) {
            System.out.println("Nisam uspio izvuci racune iz baze: " + e.getMessage());
            return bills;
        }
    }
}