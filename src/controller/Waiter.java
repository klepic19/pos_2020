package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Article;
import model.Item;
import model.Bill;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Waiter implements Initializable {

    @FXML
    GridPane proizvodiGrid;

    @FXML
    TableView prikazProizvodaTbl;

    @FXML
    TableColumn proizvodImeClmn;

    @FXML
    TableColumn kolicinaClmn;

    @FXML
    TableColumn cijenaClmn;

    private List<Article> odabraniProizvodi = new ArrayList<Article>();
    private ObservableList<Item> dodaniProizvodi = FXCollections.observableArrayList();
    Map<Integer, Item> map = new HashMap<Integer, Item>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.proizvodImeClmn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.kolicinaClmn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        this.cijenaClmn.setCellValueFactory(new PropertyValueFactory<>("price"));

        final ObservableList<Article> articles = (ObservableList<Article>) Article.select();
        int i = 0;
        int j = 0;
        for (final Article a : articles) {
            Button btn = new Button();
            ImageView iw = new ImageView(a.getImage());
            iw.setFitHeight(128);
            iw.setFitWidth(128);
            btn.setGraphic(iw);
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    odabraniProizvodi.add(a);



                    Item i = new Item(a.getName(), a.getPrice(), 1);
                    if (map.get(a.getID()) != null) {
                        i = map.get(a.getID());
                        i.setQuantity(i.getQuantity()+1);
                    }




                    dodaniProizvodi.remove(i);//sluzi da nepise ponovo kavu neg da samo poveca kolicinu
                    dodaniProizvodi.add(i);
                    map.put(a.getID(),i);
                    prikazProizvodaTbl.setItems(dodaniProizvodi);
                   //ovde refresh treba napravit
                }
            });
            proizvodiGrid.add(btn, i, j); //ZA BROJ ARTIKALA U REDU NISTA POSEBNO ahahahha
            i++;
            if (i >= 2) {
                i = 0;
                j++;
            }
        }
    }
}
