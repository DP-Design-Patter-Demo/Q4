import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by kaleab on 2/4/2019.
 */
public class EventObserver {

    //Observer class for events (MOUSE_CLICK_EVENT)

    public static EventHandler<MouseEvent> handelDelete(Book b, Label message, BorderPane border, Librarian librarian){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteBook(b, message, border, librarian);
            }
        };
    }
    private static void deleteBook(Book b, Label message, BorderPane borderPane, Librarian librarian){
        boolean check = librarian.deleteBook(b);
        if(check){
            message.setText("unsuccessful delete");
        }else{
            message.setText("successfully deleted");
        }
        VBox list = PaneBuilder.listBooksPane(message, librarian);
        borderPane.setRight(list);
    }


    public static EventHandler<MouseEvent> handelAddBook(TextField addtitle, TextField addauthor, Librarian librarian, Label message, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addBook(addtitle.getText(),
                        addauthor.getText(),
                        librarian,
                        message,
                        borderPane
                        );
            }
        };
    }
    private static void addBook(String title, String author, Librarian librarian, Label message, BorderPane borderPane){
        Author auth = librarian.findAuthor(author);
        if(auth == null){
            message.setText("Author is not registered");
        }else{
            if(title.equals(null) ||
                    title.equals("") ||
                    author.equals(null) ||
                    author.equals("")){
                message.setText("Fill both fields");
            }else{
                Book b = new Book(title, auth, librarian);
                librarian.addBook(b);
                VBox list = PaneBuilder.listBooksPane(message, librarian);
                borderPane.setRight(list);
                message.setText("Successfully added");
            }
        }
    }


    public static EventHandler<MouseEvent> handleHome(Label message, Librarian librarian, BorderPane borderPane){
        return event -> {
            message.setText("");
            VBox list = PaneBuilder.listBooksPane(message, librarian);
            borderPane.setRight(list);
        };
    }


    public static EventHandler<MouseEvent> handleSearch(TextField key, Label message, Librarian librarian, BorderPane borderPane){
        return event -> {
            search(key.getText(), message, librarian, borderPane);
        };
    }
    private static void search(String key, Label message, Librarian librarian, BorderPane borderPane){
        if(key.equals(null) || key.equals("")){
            message.setText("fill the search box");
        }else{
            try{
                Book returnedBook = librarian.findBook(key);
                VBox pane = PaneBuilder.searchResultPane(returnedBook, message, borderPane, librarian);
                borderPane.setRight(pane);
                message.setText("Search result ...");
            }catch (NullPointerException e){
                message.setText("No book is found");
            }

        }
    }

    public static EventHandler<MouseEvent> handleUpdate(TextField title, TextField author, Book oldBook, Librarian librarian, Label message, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Author auth = librarian.findAuthor(author.getText());
                if(auth == null){
                    message.setText("Author is not registered");
                }else{
                    Book newBook = new Book(title.getText(), auth, librarian);
                    librarian.updateBook(oldBook, newBook);
                    VBox list = PaneBuilder.listBooksPane(message, librarian);
                    borderPane.setRight(list);
                    message.setText("Updated successfully");
                }
            }
        };
    }
    public static EventHandler<MouseEvent> handleUpdatePane(Book b, Label message, Librarian librarian, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                message.setText("Update book");
                VBox pane = PaneBuilder.updatePane(b, librarian, message, borderPane);
                borderPane.setRight(pane);
            }
        };
    }


    public static EventHandler<MouseEvent> navAdd(Label message, Librarian librarian, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                message.setText("Add book");
                getAddPane(librarian, message, borderPane);
            }
        };
    }
    public static EventHandler<MouseEvent> navFind(Label message, Librarian librarian, BorderPane borderPane) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                message.setText("Search book");
                getFindPane(message, librarian, borderPane);
            }
        };
    }

    private static void getAddPane(Librarian librarian, Label message, BorderPane borderPane){

        VBox addPane = new VBox();
        addPane.setPadding(new Insets(15, 0, 5,5));
        addPane.setMaxWidth(400);
        addPane.setMinWidth(400);
        addPane.setMaxHeight(400);
        addPane.setMinHeight(400);

        TextField titleField = new TextField("Book title");
        titleField.selectAll();
        TextField authorField = new TextField("Book author");
        authorField.selectAll();
        Button addBtn = new Button("Add Book");
        addBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handelAddBook(titleField, authorField, librarian, message, borderPane));

        Label title = new Label("Add Book");
        addPane.getChildren().add(title);
        addPane.getChildren().add(titleField);
        addPane.getChildren().add(authorField);
        addPane.getChildren().add(addBtn);
        borderPane.setRight(addPane);
    }
    private static void getFindPane(Label message, Librarian librarian, BorderPane borderPane){

        VBox container = new VBox();

        container.setPadding(new Insets(15,5,5,5));

        HBox searchPane = new HBox();
        TextField searchField = new TextField();
        searchField.setMaxWidth(300);
        searchField.setMinWidth(300);
        Button searchBtn = new Button("Search");
        searchBtn.setMaxWidth(100);
        searchBtn.setMinWidth(100);
        searchBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleSearch(searchField, message, librarian, borderPane));
        searchPane.getChildren().add(searchField);
        searchPane.getChildren().add(searchBtn);

        container.getChildren().add(searchPane);

        borderPane.setRight(container);
    }

    public static EventHandler<MouseEvent> authenticateLibrarian(
            TextField userName, PasswordField passwordField, BorderPane borderPane,
            Stage globalStage, Scene scene){

        PaneBuilder paneBuilder = new PaneBuilder();


        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(userName.getText() == null || userName.getText().equals("")
                        || passwordField.getText() == null || passwordField.getText().equals("")){
                    userName.setText("Wrong password or username");
                }else{
                    Librarian result = Librarian.db.authenticateLibrarian(userName.getText(), passwordField.getText());
                    if(result == null){
                        userName.setText("Wrong password or username");
                    }else{
                        Button addBtn = new Button("Add Book");
                        Button deleteBtn= new Button("Delete Book");
                        Button findBtn= new Button("Find Book");
                        Button homeBtn = new Button("Home");
                        Label message = new Label();
                        Label librarianProfile = new Label("Author profile");
                        Label librarianFName = new Label("Librarian first name");
                        Label librarianLName = new Label("Librarian last name");
                        Label br = new Label();
                        paneBuilder.buildHomePage(globalStage,
                                result, addBtn, findBtn, homeBtn, deleteBtn,
                                message, librarianFName, librarianLName, librarianProfile, br,
                                borderPane, scene);
                    }
                }
            }
        };
    }

}
