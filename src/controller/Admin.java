package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Article;
import model.User;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Admin implements Initializable {

    @FXML
    TextField kImeTxt;

    @FXML
    TextField kPrezimeTxt;

    @FXML
    TextField kKimeTxt;

    @FXML
    TextField kLozinkaTxt;

    @FXML
    TableView korisnikTablica;

    @FXML
    TableColumn korisnikID;

    @FXML
    TableColumn korisnikIme;

    @FXML
    TableColumn korisnikPrezime;

    @FXML
    TableColumn korisnikKorIme;

    @FXML
    TableColumn korisnikLozinka;

    @FXML
    TableColumn korisnikUloga;

    @FXML
    Button spasiBtn;


    User selectedUser = null;

    Article selectedArticle = null;

    @FXML
    TableView proizvodiTbl;

    @FXML
    TableColumn proizvodID;

    @FXML
    TableColumn proizvodNaziv;

    @FXML
    TableColumn proizvodCijena;

    @FXML
    TableColumn proizvodKol;

    @FXML
    TextField nazivTxt;

    @FXML
    TextField cijenaTxt;

    @FXML
    TextArea kolTxt;

    @FXML
    ImageView slikaView;

    @FXML
    Button dodajProizBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.korisnikID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.korisnikIme.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.korisnikPrezime.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.korisnikKorIme.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.korisnikLozinka.setCellValueFactory(new PropertyValueFactory<>("password"));
        this.korisnikUloga.setCellValueFactory(new PropertyValueFactory<>("role"));

        this.proizvodID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.proizvodNaziv.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.proizvodCijena.setCellValueFactory(new PropertyValueFactory<>("price"));
        this.proizvodKol.setCellValueFactory(new PropertyValueFactory<>("description"));


        this.popuniKorisnike();
        this.popuniProizvode();
    }

    private void popuniKorisnike () {
        ObservableList<User> users = (ObservableList<User>) User.select();
        this.korisnikTablica.setItems(users);
    }

    private void popuniProizvode () {
        ObservableList<Article> articles= (ObservableList<Article>) Article.select();
        this.proizvodiTbl.setItems(articles);
    }

    @FXML
    public void odaberiKorisnika(MouseEvent ev) {
        this.spasiBtn.setText("Uredi korisnika");
        this.selectedUser = (User) this.korisnikTablica.getSelectionModel().getSelectedItem();
        if (this.selectedUser != null) {
            this.kImeTxt.setText(this.selectedUser.getName());
            this.kPrezimeTxt.setText(this.selectedUser.getSurname());
            this.kKimeTxt.setText(this.selectedUser.getUsername());
            this.kLozinkaTxt.setText(this.selectedUser.getPassword());
        }
    }

    @FXML
    public void odaberiProizvod(MouseEvent ev) {
        this.dodajProizBtn.setText("Uredi proizvod");
        this.selectedArticle = (Article) this.proizvodiTbl.getSelectionModel().getSelectedItem();
        if(this.selectedArticle != null) {
            this.nazivTxt.setText(this.selectedArticle.getName());
            this.kolTxt.setText(this.selectedArticle.getDescription());
            this.cijenaTxt.setText(String.valueOf(this.selectedArticle.getPrice()));
            this.slikaView.setImage(this.selectedArticle.getImage());
        }
    }

    @FXML
    public void skloniKorisnika (MouseEvent ev) {
        this.spasiBtn.setText("Dodaj korisnika");
        this.dodajProizBtn.setText("Dodaj prozvod");
        this.selectedUser = null;
        this.kImeTxt.setText("");
        this.kPrezimeTxt.setText("");
        this.kKimeTxt.setText("");
        this.kLozinkaTxt.setText("");

        this.nazivTxt.setText("");
        this.kolTxt.setText("");
        this.cijenaTxt.setText("");
        this.slikaView.setImage(null);

        this.popuniProizvode();
        this.popuniKorisnike();
    }

    @FXML
    public void dodajKorisnika (ActionEvent ev) {
        String ime = this.kImeTxt.getText();
        String prezime = this.kPrezimeTxt.getText();
        String kIme = this.kKimeTxt.getText();
        String lozinka = this.kLozinkaTxt.getText();
        if (ime.equals("") || prezime.equals("") || kIme.equals("") || lozinka.equals("")) {
            return;
        }

        if (this.selectedUser != null) {
            this.selectedUser.setName(ime);
            this.selectedUser.setSurname(prezime);
            this.selectedUser.setUsername(kIme);
            this.selectedUser.setPassword(lozinka);
            User.update(this.selectedUser);
            this.selectedUser = null;
            this.spasiBtn.setText("Dodaj korisnika");
        } else {
            User u = new User(0, ime, prezime, kIme, lozinka, "KONOBAR");
            User.add(u);
        }
        this.popuniKorisnike();

        this.kImeTxt.setText("");
        this.kPrezimeTxt.setText("");
        this.kKimeTxt.setText("");
        this.kLozinkaTxt.setText("");
    }

    @FXML
    public void dodajProizvod (ActionEvent ev) {
        String naziv = this.nazivTxt.getText();
        float cijena = Float.valueOf(this.cijenaTxt.getText());
        String opis = this.kolTxt.getText();
        Image slika = this.slikaView.getImage();
        if (naziv.equals("") || opis.equals("")) {
            return;
        }
        if (this.selectedArticle != null) {
            this.selectedArticle.setName(naziv);
            this.selectedArticle.setPrice(cijena);
            this.selectedArticle.setDescription(opis);
            if(slika != null) {
                this.selectedArticle.setImage(slika);
            }
            Article.update(this.selectedArticle);
            this.selectedArticle = null;
            this.dodajProizBtn.setText("Dodaj proizvod");
        } else {
            Article a = new Article(0, naziv, opis, cijena, slika);
            Article.add(a);
        }


        this.nazivTxt.setText("");
        this.kolTxt.setText("");
        this.cijenaTxt.setText("");
        this.slikaView.setImage(null);
        this.popuniProizvode();
    }


    @FXML
    public void odaberiSliku(ActionEvent evt) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg"));
        File datoteka = fc.showOpenDialog(null);
        Image binarnaSlika = new Image(datoteka.toURI().toString());
        this.slikaView.setImage(binarnaSlika);
    }


    @FXML
    public void pobrisiKorisnika (ActionEvent ev) {
        User user = (User) this.korisnikTablica.getSelectionModel().getSelectedItem();
        User.remove(user);
        this.popuniKorisnike();
    }

    @FXML
    public void pobrisiProizvod (ActionEvent ev) {
        Article article = (Article) this.proizvodiTbl.getSelectionModel().getSelectedItem();
        Article.remove(article);
        this.popuniProizvode();
    }
}
