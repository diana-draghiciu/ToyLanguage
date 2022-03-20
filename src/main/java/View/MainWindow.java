package View;

import Controller.Controller;
import Model.ADT.*;
import Model.PrgState;
import Model.Statement.IStmt;
import Model.Value.StringValue;
import Model.Value.Value;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class MainWindow{
    private Controller controller;
    private PrgState progState;

    @FXML
    private TableView<Map.Entry<Integer, Pair<Integer, List<Integer>>>> semaphore;

    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, String> semafIndexColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, String> semafValueColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, String> semafListColumn;

    //The main window that displays the following information:
    // (a)the number of PrgStates as a TextField

    @FXML
    private TextField nrProgStates;

    // (b)the HeapTable as a TableView with two columns: address and Value

    @FXML
    private TableView<Map.Entry<Integer, Value>> heapTable;

    @FXML
    private TableColumn<Map.Entry<Integer,Value>,String> heapAddressColumn;

    @FXML
    private TableColumn<Map.Entry<Integer,Value>,String> heapValueColumn;

    // (c)the Out as a ListView

    @FXML
    private ListView<Value> outTable;

    // (d)the FileTable as a ListView

    @FXML
    private ListView<StringValue> fileTable;

    // (e)the list of PrgState identifiers as a ListView

    @FXML
    private ListView<Integer> progStateIdentifiers;

    private int selectedID;

    // (f)a Table View with two columns: variable name and Value, which displays the SymTable of the PrgState whose ID has been selected from the list described at (e)

    @FXML
    private TableView<Map.Entry<String, Value>> symTable;
    @FXML
    private TableColumn<Map.Entry<String,Value>, String> symVarNameColumn;
    @FXML
    private TableColumn<Map.Entry<String,Value>, String> symValueColumn;

    // (g)a List View  which displays the ExeStack of the PrgState whose ID has been selected from the list described at (e).
    // First element of the ListView is a string representation of the top of ExeStack, the second element of the ListView represents the second element from the ExeStack and so on.

    @FXML
    private ListView<String> exeStack;

    // (h)A button "Run one step" that runs oneStepForAllPrg (you have to design and implement the handler of this button based on the previous method allStep).
    // After each run the displayed information is updated. You may want to write a service which wraps the repository and signals any change of the list of PrgStates.

    @FXML
    private Button runOneStepButton;


    public MainWindow(Controller c){
        this.controller=c;

        //default: we populate with the first id in the identifiers list
        this.selectedID= controller.getRepo().getLst().stream().map(PrgState::getId).collect(Collectors.toList()).get(0);
        this.progState=this.controller.getRepo().getProgWithID(selectedID);
    }

    private void populateAll(){
        populateIdentifiers();
        populateOutput(this.progState);
        populateFileTable(this.progState);
        populateHeapTable(this.progState);
        populateSymbolTable(this.progState);
        populateExecutionStack(this.progState);
        populateSemaphore(this.progState);
    }

    private void populateSemaphore(PrgState state){
        MyIHeap<Integer,Pair<Integer,List<Integer>>> semaf=state.getSemaphore();
        this.semaphore.setItems(FXCollections.observableList(semaf.getContent().entrySet().stream().toList()));
        this.semaphore.refresh();
    }

    private void populateIdentifiers() {
        List<PrgState> programStates = controller.getRepo().getLst();
        progStateIdentifiers.setItems(FXCollections.observableList(programStates.stream().map(PrgState::getId).collect(Collectors.toList()))); //set the identifiers
        nrProgStates.setText(""+programStates.size());
    }

    private void populateHeapTable(PrgState state) {
        MyIDictionary<Integer, Value> heap = state.getHeap();
        this.heapTable.setItems(FXCollections.observableList(heap.getContent().entrySet().stream().toList()));
        this.heapTable.refresh();
    }

    private void populateFileTable(PrgState state) {
        MyIDictionary<StringValue,BufferedReader> file = state.getFileTable();
        this.fileTable.setItems(FXCollections.observableList(file.getContent().keySet().stream().toList()));
        this.fileTable.refresh();
    }

    private void populateOutput(PrgState state) {
        MyIList<Value> output = state.getOutList();
        this.outTable.setItems(FXCollections.observableList(output.getContent()));
        this.outTable.refresh();
    }

    private void populateSymbolTable(PrgState state) {
        MyIDictionary<String, Value> symbolTable = state.getSymTable();
        if(state.getId()==selectedID) {
            this.symTable.setItems(FXCollections.observableList(symbolTable.getContent().entrySet().stream().toList()));
            this.symTable.refresh();
        }
    }

    private void populateExecutionStack(PrgState state) {
        MyIStack<IStmt> execStack = state.getStk();
        if(state.getId()==selectedID) {
            this.exeStack.setItems(FXCollections.observableList(execStack.getContent().stream().map(Object::toString).toList()));
            this.exeStack.refresh();
        }
    }

    private void getSelectedID(){
        int index = this.progStateIdentifiers.getSelectionModel().getSelectedIndex();

        if(index < 0)
            return;

       this.selectedID= this.progStateIdentifiers.getSelectionModel().getSelectedItem();
       this.progState=controller.getRepo().getProgWithID(selectedID);
       populateExecutionStack(progState);
       populateSymbolTable(progState);
    }

    private void runOneStepHandler() {
        try {
            this.controller.oneStep();
            populateAll();
        } catch (InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize(){
        populateAll();
        heapAddressColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getKey().toString()));
        heapValueColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getValue().toString()));
        this.symVarNameColumn.setCellValueFactory(data->new ReadOnlyStringWrapper(data.getValue().getKey()));
        this.symValueColumn.setCellValueFactory(data->new ReadOnlyStringWrapper(data.getValue().getValue().toString()));
        this.semafIndexColumn.setCellValueFactory(data->new ReadOnlyStringWrapper(data.getValue().getKey().toString()));
        this.semafValueColumn.setCellValueFactory(data->new ReadOnlyStringWrapper(data.getValue().getValue().getKey().toString()));
        this.semafListColumn.setCellValueFactory(data->new ReadOnlyStringWrapper(data.getValue().getValue().getValue().toString()));

        //Handlers
        this.progStateIdentifiers.setOnMouseClicked(mouseEvent -> {getSelectedID();});
        this.runOneStepButton.setOnMouseClicked(mouseEvent -> {runOneStepHandler(); });
    }
}
