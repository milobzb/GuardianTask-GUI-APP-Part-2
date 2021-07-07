package ucf.assignments;

import javafx.application.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App extends Application {

    private TableView<TodoList> todoList = new TableView<>();
    private final ObservableList<TodoList> toDoListData =
            FXCollections.observableArrayList(
                    new TodoList("List 1"),
                    new TodoList("List 2"));

    private TableView<Task> tableChron = new TableView<>();

    final HBox hb = new HBox();
    Label response;

    private Desktop desktop = Desktop.getDesktop();
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        setHomeScreen();
        primaryStage.setWidth(750);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Task"); // Names the window.
        primaryStage.show(); // Shows the stage.
    }

    private void openFile(File opensFile) {
        try {
            desktop.open(opensFile);
        } catch (IOException ex) {
            Logger.getLogger(
                    App.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    private void saveFile(File savesFile) {
        try {
            desktop.open(savesFile);
        } catch (IOException ex) {
            Logger.getLogger(
                    App.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    private void setHomeScreen() {
        VBox vBox1 = new VBox(); // Create new vertical box.
        Scene scene = new Scene(vBox1); // Create scene.
        vBox1.setSpacing(7); // Spacing for box.

        response = new Label("Menu");

        final Label label = new Label("To Do List"); // Main window heading label.
        label.setFont(new Font("Arial", 18));
        label.setPadding(new Insets(10, 10, 10, 10));

        todoList = new TableView<>();
        todoList.setEditable(true);

        TableColumn titleCol = new TableColumn("Title");
        TableColumn editCol = new TableColumn<>("Modify");
        TableColumn deleteCol = new TableColumn<>("Delete");

        titleCol.setCellValueFactory(
                new PropertyValueFactory<TodoList, String>("Title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TodoList, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TodoList, String> t) {
                        ((TodoList) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTitle(t.getNewValue());
                    }
                }
        );

        editCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<TodoList, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TodoList, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        editCol.setCellFactory(
                new Callback<TableColumn<TodoList, Boolean>, TableCell<TodoList, Boolean>>() {
                    @Override
                    public TableCell<TodoList, Boolean> call(TableColumn<TodoList, Boolean> p) {
                        return new ModifyCell(todoList);
                    }
                });

        deleteCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<TodoList, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TodoList, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        deleteCol.setCellFactory(
                new Callback<TableColumn<TodoList, Boolean>, TableCell<TodoList, Boolean>>() {
                    @Override
                    public TableCell<TodoList, Boolean> call(TableColumn<TodoList, Boolean> p) {
                        return new DeleteCell<>(todoList);
                    }
                });

        todoList.setItems(toDoListData);
        todoList.getColumns().addAll(titleCol, editCol, deleteCol);
        // Set width
        todoList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        titleCol.setMinWidth(400);

        // Create horizontal box.
        HBox hbox1 = new HBox();
        hbox1.setSpacing(8);
        hbox1.setPadding(new Insets(10, 10, 10, 10));

        // Create text fields so the user can enter new tasks into the table.
        TextField addTitle = new TextField();

        // Set initial text in fields.
        addTitle.setText("Add title");

        // Set length
        addTitle.setPrefWidth(400);

        // Create add button.
        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                toDoListData.add(new TodoList(
                        addTitle.getText()));
                addTitle.clear();
            }
        });

        // Get user entry fields all in a horizontal row.
        hbox1.getChildren().addAll(addTitle, btnAdd);

        // Get all items in a vertical, stacking format.
        vBox1.getChildren().addAll(getMenu(), label, todoList, hbox1);

        setScene(scene);
    }

    private void setTodoListScene(TodoList todoList) {
        VBox vBox1 = new VBox(); // Create new vertical box.
        Scene scene = new Scene(vBox1); // Create scene.
        vBox1.setSpacing(7); // Spacing for box.

        response = new Label("Menu");

        final Label label = new Label("To Do List"); // Main window heading label.
        label.setFont(new Font("Arial", 18));
        label.setPadding(new Insets(10, 10, 10, 10));

        tableChron = new TableView<>();
        tableChron.setEditable(true);

        // Column headings in the tableChron.
        TableColumn descriptionCol = new TableColumn("Description");
        TableColumn deadlineCol = new TableColumn("DueDate");
        TableColumn completedCol = new TableColumn("Completed");
        TableColumn deleteCol = new TableColumn("Delete");

        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<Task, String>("Description"));
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Task, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Task, String> t) {
                        ((Task) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDescription(t.getNewValue());
                    }
                }
        );

        deadlineCol.setCellValueFactory(
                new PropertyValueFactory<Task, String>("DueDate"));
        deadlineCol.setCellFactory(TextFieldTableCell.forTableColumn());
        deadlineCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Task, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Task, String> t) {
                        ((Task) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDueDate(t.getNewValue());
                    }
                }
        );

        completedCol.setCellValueFactory(new PropertyValueFactory<Task, Boolean>("Completed"));
        completedCol.setCellFactory(tc -> new CheckBoxTableCell<>());
        completedCol.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Task, Boolean>>) t -> ((Task) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCompleted(t.getNewValue())
        );

        deleteCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Task, Boolean>,
                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Task, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        deleteCol.setCellFactory(
                new Callback<TableColumn<Task, Boolean>, TableCell<Task, Boolean>>() {
                    @Override
                    public TableCell<Task, Boolean> call(TableColumn<Task, Boolean> p) {
                        return new DeleteCell<>(tableChron);
                    }
                });

        tableChron.setItems(todoList.getTasks());
        tableChron.getColumns().addAll(descriptionCol, deadlineCol, completedCol, deleteCol); // Place the column headings in tableChron.
        // Set width
        tableChron.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        descriptionCol.setMinWidth(250);
        deadlineCol.setMinWidth(150);
        completedCol.setMinWidth(50);

        // Create horizontal box.
        HBox hbox1 = new HBox();
        hbox1.setSpacing(8);
        hbox1.setPadding(new Insets(10, 10, 10, 10));

        // Create text fields so the user can enter new tasks into the table.
        TextField addDescription = new TextField();
        TextField addDueDay = new TextField();

        // Set initial text in fields.
        addDescription.setText("Add description");
        addDueDay.setText("Enter due date");

        // Set length
        addDescription.setPrefWidth(250);
        addDueDay.setPrefWidth(150);

        // Create add button.
        Button btnAdd = new Button("Add");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                todoList.addTask(new Task(
                        addDescription.getText(),
                        addDueDay.getText()));
                addDescription.clear();
                addDueDay.clear();
            }
        });


        // Create add button.
        Button goHome = new Button("Go to Home");
        goHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setHomeScreen();
            }
        });

        // Get user entry fields all in a horizontal row.
        hbox1.getChildren().addAll(addDescription, addDueDay, btnAdd, goHome);

        // Get all items in a vertical, stacking format.
        vBox1.getChildren().addAll(getMenu(), label, tableChron, hbox1);

        setScene(scene);
    }

    private MenuBar getMenu() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("_File"); // Alt-f shortcut for file
        MenuItem open = new MenuItem("Open...");
        MenuItem save = new MenuItem("Save As...");
        MenuItem exit = new MenuItem("Exit");
        SeparatorMenuItem separator = new SeparatorMenuItem();

        fileMenu.getItems().add(open);
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(separator);
        fileMenu.getItems().add(exit);

        menuBar.getMenus().add(fileMenu);

        // Create one event handler that will handle menu action events.
        EventHandler<ActionEvent> MEHandler =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        String name = ((MenuItem) ae.getTarget()).getText();

                        // if Exit is chosen, the program is  terminated.
                        if (name.equals("Exit")) Platform.exit();

                        response.setText(name + " selected");
                    }
                };

        open.setOnAction(MEHandler);
        save.setOnAction(MEHandler);
        exit.setOnAction(MEHandler);

        final FileChooser fileChooser = new FileChooser();

        open.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File opensFile = fileChooser.showOpenDialog(stage);
                        if (opensFile != null) {
                            openFile(opensFile);
                        }
                    }

                });

        save.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        File savesFile = fileChooser.showSaveDialog(stage);
                        if (savesFile != null) {
                            saveFile(savesFile);
                        }
                    }

                });

        return menuBar;
    }

    private void setScene(Scene scene) {
        stage.setScene(scene);
    }

    //Define the button cell
    private class ModifyCell extends TableCell<TodoList, Boolean> {
        final Button cellButton = new Button("Modify");

        ModifyCell(final TableView tblView){
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    int selectedIndex = getTableRow().getIndex();
                    TodoList todoList = (TodoList) tblView.getItems().get(selectedIndex);
                    setTodoListScene(todoList);
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }

    //Define the button cell
    private class DeleteCell<T> extends TableCell<T, Boolean> {
        final Button cellButton = new Button("Delete");

        DeleteCell(final TableView tblView){
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    int selectedIndex = getTableRow().getIndex();
                    T item = (T) tblView.getItems().get(selectedIndex);
                    tblView.getItems().remove(item);
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }

}

