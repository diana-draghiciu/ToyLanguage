package View;

import Controller.Controller;
import Exceptions.MyException;
import Model.ADT.*;
import Model.PrgState;
import Model.Statement.IStmt;
import Repository.Repo;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectProgram{
    private ArrayList<IStmt> programs;

    @FXML
    private ListView<IStmt> programListView;

    @FXML
    private Button executeButton;

    public SelectProgram(ArrayList<IStmt> prg){
        this.programs=new ArrayList<>(prg);
    }

    public void runProgram() {
        int index = programListView.getSelectionModel().getSelectedIndex();

        if (index < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a program first");
            alert.showAndWait();
            return;
        }

        try {
            IStmt selectedProg = programs.get(index);
            selectedProg.typecheck(new MyDictionary<>());
            PrgState p = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap<>(), selectedProg, new MyHeap<>());
            Repo r = new Repo(p, "log" + index + ".txt");
            Controller c = new Controller(r);
            MainWindow mainController = new MainWindow(c);
            FXMLLoader fxmlLoader = new FXMLLoader(StartProgram.class.getResource("main-window.fxml"));
            fxmlLoader.setController(mainController);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (MyException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize(){
        programListView.setItems(FXCollections.observableList(programs));
        executeButton.setOnMouseClicked(mouseEvent -> runProgram());
    }
}
