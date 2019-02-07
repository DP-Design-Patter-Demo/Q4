import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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

import java.sql.SQLException;

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

    public static EventHandler<MouseEvent> handelDeleteAuthor(Author a, Label message, Label error,  BorderPane border, Librarian librarian){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteAuthor(a, message, error, border, librarian);
            }
        };
    }
    private static void deleteAuthor(Author a, Label message,Label error, BorderPane borderPane, Librarian librarian){
        try {

            boolean check = librarian.deleteAuthor(a);
            if(check){
                message.setText("unsuccessful delete");
            }else{
                message.setText("successfully deleted");
            }
            VBox list = PaneBuilder.listAuthors(message, librarian);
            borderPane.setRight(list);
        }catch (SQLException e){
            error.setText("\nBefore deleting the author you have to delete\nall books he/she wrote");
        }
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
    public static EventHandler<MouseEvent> handelAddAuthor(TextField firstName, TextField lastName, Label message, Librarian librarian, BorderPane borderPane){
        return event -> {
            addAuthor(firstName.getText(),
                    lastName.getText(),
                    librarian,
                    message,
                    borderPane
            );

        };
    }
    private static void addAuthor(String firstName, String lastName, Librarian librarian, Label message, BorderPane borderPane){
        if(firstName.equals(null) ||
                firstName.equals("") ||
                lastName.equals(null) ||
                lastName.equals("")){
            message.setText("Fill both fields");
        }else{
            Author author = new Author(firstName, lastName);
            librarian.addAuthor(author);
            VBox list = PaneBuilder.listAuthors(message, librarian);
            borderPane.setRight(list);
            message.setText("Successfully added");
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
    public static EventHandler<MouseEvent> handleAuthorSearch(TextField key, Label message, Librarian librarian, BorderPane borderPane){
        return event -> {
            searchAuthor(key.getText(), message, librarian, borderPane);
        };
    }
    private static void searchAuthor(String key, Label message, Librarian librarian, BorderPane borderPane){
        if(key.equals(null) || key.equals("")){
            message.setText("fill the search box");
        }else{
            try{
                Author author = librarian.findAuthor(key);
                VBox pane = PaneBuilder.searchAuthorResultPane(author, message, borderPane, librarian);
                borderPane.setRight(pane);
                message.setText("Search result ...");
            }catch (NullPointerException e){
                message.setText("No author is found");
            }

        }
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
    public static EventHandler<MouseEvent> handleAuthorUpdate(TextField firstName, TextField lastName, Author author, Librarian librarian, Label message, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    Author newAuthor = new Author(firstName.getText(),lastName.getText());
                    librarian.updateAuthor(author, newAuthor);
                    VBox list = PaneBuilder.listAuthors(message, librarian);
                    borderPane.setRight(list);
                    message.setText("Updated successfully");
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

    public static EventHandler<MouseEvent> handleUpdateAuthorPane(Author a, Label message, Librarian librarian, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                message.setText("Update Author");
                VBox pane = PaneBuilder.updateAuthorPane(a, librarian, message, borderPane);
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
    public static EventHandler<MouseEvent> navAddAuthor(Label message, Librarian librarian, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                message.setText("Add Author");
                getAuthorAddPane(librarian, message, borderPane);
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

    public static EventHandler<MouseEvent> navFindAuthor(Label message, Librarian librarian, BorderPane borderPane) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                message.setText("Search author");
                getFindAuthorPane(message, librarian, borderPane);
            }
        };
    }
    private static void getAuthorAddPane(Librarian librarian, Label message, BorderPane borderPane){
            VBox addPane = new VBox();
            addPane.setPadding(new Insets(15, 0, 5,5));
            addPane.setMaxWidth(400);
            addPane.setMinWidth(400);
            addPane.setMaxHeight(400);
            addPane.setMinHeight(400);

            TextField firstName = new TextField("Author first name");
            firstName.selectAll();
            TextField lastName = new TextField("author last name");
            lastName.selectAll();
            Button addBtn = new Button("Add Author");
            addBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handelAddAuthor(firstName, lastName, message, librarian, borderPane));
            Label title = new Label("Add Author");
            addPane.getChildren().add(title);
            addPane.getChildren().add(firstName);
            addPane.getChildren().add(lastName);
            addPane.getChildren().add(addBtn);
            borderPane.setRight(addPane);
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

    private static void getFindAuthorPane(Label message, Librarian librarian, BorderPane borderPane){

        VBox container = new VBox();

        container.setPadding(new Insets(15,5,5,5));

        HBox searchPane = new HBox();
        TextField searchField = new TextField();
        searchField.setMaxWidth(300);
        searchField.setMinWidth(300);
        Button searchBtn = new Button("Search");
        searchBtn.setMaxWidth(100);
        searchBtn.setMinWidth(100);
        //Label error = new Label();
        searchBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleAuthorSearch(searchField, message, librarian, borderPane));
        searchPane.getChildren().add(searchField);
        searchPane.getChildren().add(searchBtn);

        container.getChildren().add(searchPane);

        borderPane.setRight(container);
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
                        Button listAuthorsBtn = new Button("List authors");
                        Button findAuthorBtn = new Button("Find author");
                        Button addAuthorBtn = new Button("Add author");
                        Label message = new Label();
                        Label librarianProfile = new Label("Librarians profile");
                        Label librarianFName = new Label("Librarian first name");
                        Label librarianLName = new Label("Librarian last name");
                        Label br = new Label();
                        paneBuilder.buildHomePage(globalStage,
                                result, addBtn, findBtn, homeBtn, deleteBtn,
                                listAuthorsBtn, addAuthorBtn, findAuthorBtn,
                                message, librarianFName, librarianLName, librarianProfile, br,
                                borderPane, scene);
                    }
                }
            }
        };
    }

    public static EventHandler<MouseEvent> signup(TextField firstName,
                                                   TextField lastName,
                                                   TextField userName,
                                                   PasswordField newPassword,
                                                   PasswordField confirmPassword,
                                                  Label signupMessage){

        return event -> {

            if(firstName.getText().equals("") || firstName == null ||
                    lastName.getText().equals("") || lastName.getText() == null ||
                    userName.getText().equals("") || userName.getText() == null ||
                    newPassword.getText().equals("") || newPassword.getText() == null ||
                    confirmPassword.getText().equals("") || confirmPassword.getText() == null ||
                    !newPassword.getText().equals(confirmPassword.getText())
                    ){
                signupMessage.setText("- All fields are required orPassword does not match");
            }else{
                Librarian newLibrarian = new Librarian(firstName.getText(), lastName.getText(), userName.getText());
                newLibrarian.setPassword(newPassword.getText());

                boolean notSignedUp =  Librarian.signUp(newLibrarian);
                if(notSignedUp){
                    signupMessage.setText("Un successful sign up");
                }else{
                    signupMessage.setText("Signed up successfully");
                }
                firstName.setText("");
                lastName.setText("");
                userName.setText("");
                newPassword.setText("");
                confirmPassword.setText("");
            }

        };

    }

    public static EventHandler<MouseEvent> listAuthorsPane(Label message, Librarian librarian, BorderPane borderPane){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                    VBox result = PaneBuilder.listAuthors(message, librarian);
                borderPane.setRight(result);
            }
        };
    }


}
