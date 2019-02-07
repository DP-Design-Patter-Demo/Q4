import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.util.List;

/**
 * Created by kaleab on 2/4/2019.
 */
public class PaneBuilder {

    public static VBox listBooksPane(Label message, Librarian librarian) {
        VBox listBooksPane = new VBox();
        listBooksPane.setPadding(new Insets(15, 0, 5,5));
        listBooksPane.setMaxWidth(400);
        listBooksPane.setMinWidth(400);
        listBooksPane.setMaxHeight(400);
        listBooksPane.setMinHeight(400);
        List<Book> result = librarian.getAllBooks();
        if(result.size() < 1){
            message.setText("No book to show");
        }else{

            for (int i = 0; i < result.size(); i++) {
                VBox oneList = new VBox();
                Label title = new Label("TITLE: " + result.get(i).getTitle());
                Label author = new Label("AUTHOR: " + result.get(i).getAuthor().getFirstName());
                Label newLine = new Label();
                oneList.getChildren().add(title);
                oneList.getChildren().add(author);
                oneList.getChildren().add(newLine);
                listBooksPane.getChildren().add(oneList);
            }
            message.setText("List of available books");
        }

        return listBooksPane;
    }


    public static VBox listAuthors(Label message, Librarian librarian) {
        VBox listAuthorsPane = new VBox();
        listAuthorsPane.setPadding(new Insets(15, 0, 5,5));
        listAuthorsPane.setMaxWidth(400);
        listAuthorsPane.setMinWidth(400);
        listAuthorsPane.setMaxHeight(400);
        listAuthorsPane.setMinHeight(400);
        List<Author> result = librarian.getAllAuthors();
        if(result.size() < 1){
            message.setText("No author to show");
        }else{

            for (int i = 0; i < result.size(); i++) {
                VBox oneList = new VBox();
                Label firstName = new Label("First Name: " + result.get(i).getFirstName());
                Label lastName = new Label("Last Name: " + result.get(i).getLastName());
                Label newLine = new Label();
                oneList.getChildren().add(firstName);
                oneList.getChildren().add(lastName);
                oneList.getChildren().add(newLine);
                listAuthorsPane.getChildren().add(oneList);
            }
            message.setText("List of registered authors");
        }

        return listAuthorsPane;
    }

    public static VBox searchResultPane(Book book, Label message, BorderPane border, Librarian librarian){
        VBox bookDetail = new VBox();
        bookDetail.setPadding(new Insets(15, 0, 5,5));
        bookDetail.setMaxWidth(400);
        bookDetail.setMinWidth(400);
        bookDetail.setMaxHeight(400);
        bookDetail.setMinHeight(400);

        Label title = new Label("Book detail");
        Label titleLabel = new Label("Title: " + book.getTitle());
        Label authorLabel = new Label("Author: " + book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
        bookDetail.getChildren().add(title);
        bookDetail.getChildren().add(titleLabel);
        bookDetail.getChildren().add(authorLabel);

        HBox controlPanel = new HBox();
        Button update = new Button("Edit book");
        update.setMaxWidth(200);
        update.setMinWidth(200);
        update.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleUpdatePane(book, message, librarian, border));
        controlPanel.getChildren().add(update);
        Button delete = new Button("Delete book");
        delete.setMaxWidth(200);
        delete.setMinWidth(200);
        delete.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handelDelete(book, message, border, librarian));
        controlPanel.getChildren().add(delete);
        bookDetail.getChildren().add(controlPanel);
        return bookDetail;

    }
    public static VBox searchAuthorResultPane(Author author, Label message, BorderPane border, Librarian librarian){
        VBox authorDetail = new VBox();
        authorDetail.setPadding(new Insets(15, 0, 5,5));
        authorDetail.setMaxWidth(400);
        authorDetail.setMinWidth(400);
        authorDetail.setMaxHeight(400);
        authorDetail.setMinHeight(400);

        Label title = new Label("Author detail");
        Label titleLabel = new Label("Name: " + author.getFirstName() + " " + author.getLastName());
        String booksWrote = "Books she/he wrote:";
        List<Book> books = librarian.booksWrittenBy(author);
        for (Book b: books) {
            booksWrote += "\n\n\tTitle: " + b.getTitle();
        }
        Label authorLabel = new Label(booksWrote);
        Label error = new Label();
        authorDetail.getChildren().add(title);
        authorDetail.getChildren().add(titleLabel);
        authorDetail.getChildren().add(authorLabel);
        authorDetail.getChildren().add(error);


        HBox controlPanel = new HBox();
        Button update = new Button("Edit Author");
        update.setMaxWidth(200);
        update.setMinWidth(200);

        update.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleUpdateAuthorPane(author, message, librarian, border));
        controlPanel.getChildren().add(update);
        Button delete = new Button("Delete Author");

        delete.setMaxWidth(200);
        delete.setMinWidth(200);
        delete.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handelDeleteAuthor(author, message, error, border, librarian));
        controlPanel.getChildren().add(delete);
        authorDetail.getChildren().add(controlPanel);
        return authorDetail;

    }
    public static VBox updatePane(Book book, Librarian librarian, Label message, BorderPane borderPane){
        VBox updatePane = new VBox();
        updatePane.setPadding(new Insets(15, 0, 5,5));
        updatePane.setMaxWidth(400);
        updatePane.setMinWidth(400);
        updatePane.setMaxHeight(400);
        updatePane.setMinHeight(400);
        Label title = new Label("Update book");
        Label newLine = new Label();
        Label titleLabel = new Label("Title");
        TextField bookTitle = new TextField(book.getTitle());
        Label authorLabel = new Label("Author");
        TextField authorName = new TextField(book.getAuthor().getFirstName());
        Button updateBtn = new Button("Update");

        Author auth = librarian.findAuthor(authorName.getText());

        //new book then oldbook

        updateBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleUpdate(bookTitle, authorName, book, librarian, message, borderPane));
        updatePane.getChildren().add(title);
        updatePane.getChildren().addAll(newLine);
        updatePane.getChildren().add(titleLabel);
        updatePane.getChildren().add(bookTitle);
        updatePane.getChildren().add(authorLabel);
        updatePane.getChildren().add(authorName);
        updatePane.getChildren().add(updateBtn);
        return updatePane;
    }

    public static VBox updateAuthorPane(Author author, Librarian librarian, Label message, BorderPane borderPane){
        VBox updatePane = new VBox();
        updatePane.setPadding(new Insets(15, 0, 5,5));
        updatePane.setMaxWidth(400);
        updatePane.setMinWidth(400);
        updatePane.setMaxHeight(400);
        updatePane.setMinHeight(400);
        Label title = new Label("Update author");
        Label newLine = new Label();
        Label firstNameLabel = new Label("First Name");
        TextField firstName = new TextField(author.getFirstName());
        Label lastNameLabel = new Label("Last name");
        TextField lastName = new TextField(author.getLastName());
        Button updateBtn = new Button("Update Author");


        updateBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleAuthorUpdate(firstName, lastName, author, librarian, message, borderPane));
        updatePane.getChildren().add(title);
        updatePane.getChildren().addAll(newLine);
        updatePane.getChildren().add(firstNameLabel);
        updatePane.getChildren().add(firstName);
        updatePane.getChildren().add(lastNameLabel);
        updatePane.getChildren().add(lastName);
        updatePane.getChildren().add(updateBtn);
        return updatePane;
    }
    public static VBox getNavBar(Button addBtn, Button deleteBtn, Button homeBtn, Button findBtn,
                                 Button listAuthors, Button addAuthor, Button findAuthor,
                           Librarian librarian, Label librarianFName, Label librarianLName,
                           Label message, Label librarianProfile, Label br){
        VBox navigationBar = new VBox();
        navigationBar.setMaxWidth(150);
        navigationBar.setMinWidth(150);
        navigationBar.setMaxHeight(400);
        navigationBar.setMinHeight(400);
        navigationBar.setPadding(new Insets(15, 15, 5, 5));
        addBtn.setMinWidth(130);
        addBtn.setAlignment(Pos.CENTER_LEFT);
        deleteBtn.setMinWidth(130);
        deleteBtn.setAlignment(Pos.CENTER_LEFT);
        findBtn.setMinWidth(130);
        findBtn.setAlignment(Pos.CENTER_LEFT);
        homeBtn.setMinWidth(130);
        homeBtn.setAlignment(Pos.CENTER_LEFT);

        listAuthors.setMinWidth(130);
        listAuthors.setAlignment(Pos.CENTER_LEFT);
        addAuthor.setMinWidth(130);
        addAuthor.setAlignment(Pos.CENTER_LEFT);
        findAuthor.setMinWidth(130);
        findAuthor.setAlignment(Pos.CENTER_LEFT);

        String[] profile = librarian.getProfile();
        librarianFName.setText(profile[1]);
        librarianFName.minWidth(130);
        librarianLName.minWidth(130);

        librarianFName.setAlignment(Pos.BASELINE_CENTER);
        librarianLName.setAlignment(Pos.BASELINE_CENTER);
        librarianLName.setText(profile[2]);

        navigationBar.getChildren().add(message);

        navigationBar.getChildren().add(homeBtn);
        navigationBar.getChildren().add(addBtn);
        navigationBar.getChildren().add(findBtn);


        navigationBar.getChildren().add(listAuthors);
        navigationBar.getChildren().add(addAuthor);
        navigationBar.getChildren().add(findAuthor);

        navigationBar.getChildren().add(br);
        navigationBar.getChildren().add(librarianProfile);
        navigationBar.getChildren().add(librarianFName);
        navigationBar.getChildren().add(librarianLName);

        return navigationBar;
    }
    public void buildHomePage(Stage globalStage, Librarian librarian,
                            Button addBtn, Button findBtn, Button homeBtn, Button deleteBtn,
                              Button listAuthorBtn, Button addAuthorBtn, Button findAuthorBtn,
                                     Label message, Label librarianFName, Label librarianLName, Label librarianProfile, Label br,
                                     BorderPane borderPane, Scene scene){
        globalStage.setMaxWidth(600);
        globalStage.setMinWidth(600);
        globalStage.setMaxHeight(400);
        globalStage.setMinHeight(400);

        addBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.navAdd(message, librarian, borderPane));
        findBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.navFind(message, librarian, borderPane));
        homeBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.handleHome(message, librarian, borderPane));
        listAuthorBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.listAuthorsPane(message, librarian, borderPane));
        addAuthorBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.navAddAuthor(message, librarian, borderPane));
        findAuthorBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.navFindAuthor(message, librarian, borderPane));

        VBox navBar = PaneBuilder.getNavBar(addBtn, deleteBtn, homeBtn, findBtn,
                listAuthorBtn, addAuthorBtn, findAuthorBtn,
                librarian, librarianFName, librarianLName, message, librarianProfile, br);
        VBox mainPane = PaneBuilder.listBooksPane(message, librarian);
        borderPane.setLeft(navBar);
        borderPane.setRight(mainPane);
        globalStage.setScene(scene);
        globalStage.show();
    }
    public static void loginPane(Stage globalStage){
        BorderPane borderPane = new BorderPane();
        //login controls
        Label labelUserName = new Label("User name");
        TextField userName = new TextField();
        Label labelPassword = new Label("Password");
        PasswordField passwordField = new PasswordField();
        Button loginBtn = new Button("Login");
        //signup controls
        Label signUpMessage = new Label("");
        Label labelFirstName= new Label("First Name");
        TextField firstName = new TextField();
        Label labelLastName = new Label("Last Name");
        TextField lastName = new TextField();
        Label labelUName = new Label("User name");
        TextField uName = new TextField("");
        Label labelnewPassword = new Label("New password");
        PasswordField newPassword = new PasswordField();
        Label labelConfirmPasswrod = new Label("Confirm password");
        PasswordField confirmPassword = new PasswordField();

        globalStage.setMaxWidth(600);
        globalStage.setMinWidth(600);
        globalStage.setMaxHeight(400);
        globalStage.setMinHeight(400);

        VBox loginPane = new VBox();

        loginPane.getChildren().add(labelUserName);
        loginPane.getChildren().addAll(userName);
        loginPane.getChildren().add(labelPassword);
        loginPane.getChildren().add(passwordField);
        loginPane.getChildren().add(loginBtn);

        Button signup = new Button("Sign up");

        VBox signupPane = new VBox();
        signupPane.getChildren().add(signUpMessage);
        signupPane.getChildren().add(labelFirstName);
        signupPane.getChildren().add(firstName);
        signupPane.getChildren().add(labelLastName);
        signupPane.getChildren().add(lastName);
        signupPane.getChildren().add(labelUName);
        signupPane.getChildren().add(uName);
        signupPane.getChildren().add(labelnewPassword);
        signupPane.getChildren().add(newPassword);
        signupPane.getChildren().add(labelConfirmPasswrod);
        signupPane.getChildren().add(confirmPassword);
        signupPane.getChildren().add(signup);

        signup.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.signup(firstName, lastName, uName, newPassword, confirmPassword, signUpMessage));
        borderPane.setLeft(signupPane);
        borderPane.setRight(loginPane);
        Scene scene = new Scene(borderPane);
        loginBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, EventObserver.authenticateLibrarian(userName, passwordField, borderPane, globalStage, scene));
        globalStage.setScene(scene);
        globalStage.show();

    }





}
