package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Article {

    private int ID;
    private String name;
    private String description;
    private float price;
    private Image image;


    public Article(int ID, String name, String description, float price, Image image) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public static Article add(Article a) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(a.getImage(), null), "jpg", os);
            InputStream fis = new ByteArrayInputStream(os.toByteArray());

            PreparedStatement stmnt = Database.CONNECTION.prepareStatement("INSERT INTO proizvod VALUES (null, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, a.getName());
            stmnt.setFloat(2, a.getPrice());
            stmnt.setString(3, a.getDescription());
            stmnt.setBinaryStream(4, fis);
            stmnt.executeUpdate();

            ResultSet rs = stmnt.getGeneratedKeys();
            if (rs.next()) {
                a.setID(rs.getInt(1));
            }
            return a;
        } catch (SQLException e) {
            System.out.println("Nisam uspio dodati proizvod: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Nisam uspio dodati proizvod: " + e.getMessage());
            return null;
        }
    }

    public static boolean remove(Article a) {
        try {
            PreparedStatement stmnt = Database.CONNECTION.prepareStatement("DELETE FROM proizvod WHERE id=?");
            stmnt.setInt(1, a.getID());
            stmnt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Nisam uspio pobrisati proizvod: " + e.getMessage());
            return false;
        }
    }

    public static boolean update(Article a) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(a.getImage(), null), "jpg", os);
            InputStream fis = new ByteArrayInputStream(os.toByteArray());


            PreparedStatement stmnt = Database.CONNECTION.prepareStatement("UPDATE proizvod SET naziv=?, cijena=?, opis=?, slika=?  WHERE id=?");
            stmnt.setString(1, a.getName());
            stmnt.setFloat(2, a.getPrice());
            stmnt.setString(3, a.getDescription());
            stmnt.setBinaryStream(4, fis);
            stmnt.setInt(5, a.getID());
            stmnt.executeUpdate();
            return true;
        } catch (SQLException | IOException e) {
            System.out.println("Nisam uspio urediti proizvod: " + e.getMessage());
            return false;
        }
    }

    public static List<Article> select() {
        ObservableList<Article> articles = FXCollections.observableArrayList();;
        try {
            Statement stmnt = Database.CONNECTION.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM proizvod");


            while(rs.next()){
                Image fxSlika = null;
                try {
                    BufferedImage bImage = ImageIO.read(rs.getBinaryStream(5));
                    fxSlika = SwingFXUtils.toFXImage(bImage, null);
                } catch (NullPointerException | IOException ex) {
                    fxSlika = null;
                }

                articles.add(new Article(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(4),
                        rs.getFloat(3),
                        fxSlika
                ));
            }
            return articles;
        } catch (SQLException e) {
            System.out.println("Nisam uspio izvuci korisnike iz baze: " + e.getMessage());
            return null;
        }
    }
}
