

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaleab on 2/2/2019.
 */
public class Data {


    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/java_hibernate_db";
    static Connection connection = null;
    Statement statement = null;

    //singleton interface
    private Data(){
        try{
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, "root", "");
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //thread-safe singleton object access
    public static synchronized Data getSingletonDataAccessObject(){
        return new Data();
    }

    public boolean signUpLibrarian(Librarian librarian){
        try{
            String firstName = librarian.getFirstName();
            String lastName = librarian.getLastName();
            String userName = librarian.getUserName();
            String password = librarian.getPassword();

            statement = connection.createStatement();
            String sql = "INSERT INTO librarians (first_name, last_name, user_name, password) VALUES ('"+firstName+"', '"+lastName+"', '"+userName+"', '"+password+"');";
            return statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    //factory method (building list of object)
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList();
        try{
            statement = this.connection.createStatement();
            String sql = "SELECT * FROM books;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int author_id = resultSet.getInt("author_id");
                int librarian_id = resultSet.getInt("librarian_id");

                Author author = findAuthorById(author_id);
                Librarian librarian = findLibrarianById(librarian_id);

                Book book = new Book(title, author, librarian);
                book.setId(id);
                books.add(book);
            }

        }catch (SQLException e){
        }
        return books;
    }
    public List<Author> getAllAuthors(){
        List<Author> authors = new ArrayList();
        try{
            statement = this.connection.createStatement();
            String sql = "SELECT * FROM authors;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String firstName = resultSet.getString("first_name");
                Author author = findAuthorByName(firstName);
                authors.add(author);
            }

        }catch (SQLException e){
        }
        return authors;
    }
    public boolean addBook(Book book, Librarian by){

        try{
            statement = connection.createStatement();
            int id = book.getId();
            String title = book.getTitle();
            int author_id = book.getAuthor().getId();
            int librarian_id = book.getLibrarian().getId();

            String sql = "INSERT INTO books (title, author_id, librarian_id) values ('"+title+"', "+author_id+", "+by.getId()+");";
            return  statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }
    public boolean addAuthor(Author author){
        try{
            statement = connection.createStatement();
            String firstName = author.getFirstName();
            String lastName = author.getLastName();
            String sql = "INSERT INTO authors (first_name, last_name) values ('"+firstName+"', '"+lastName+"');";
            return  statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    //factory method (building an object)
    public Book findBook(String key){
        Book book = null;
        try{
            statement = connection.createStatement();
            Author author = findAuthorByName(key);
            String sql;
            if (author != null) {
                sql = "SELECT * FROM books WHERE title = '" +key+ "' OR author_id = " + author.getId() +";";
            }else{
                sql = "SELECT * FROM books WHERE title = '" + key + "';";
            }

            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()){

                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Author auth = findAuthorById(resultSet.getInt("author_id"));
                Librarian librarian = findLibrarianById(resultSet.getInt("librarian_id"));

                book = new Book(title, auth, librarian);
                book.setId(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return book;
    }
    public boolean updateBook(Book oldBook, Book newBook){
        try{
            statement = connection.createStatement();
            String title = newBook.getTitle();
            int author_id = newBook.getAuthor().getId();
            int librarian_id = newBook.getLibrarian().getId();
            String sql = "UPDATE books SET title = '"+title+"', author_id = "+author_id+", librarian_id = "+librarian_id+" WHERE id = "+oldBook.getId()+";";
            return statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateAuthor(Author oldAuthor, Author newAuthor){
        try{
            statement = connection.createStatement();
            String firstName = newAuthor.getFirstName();
            String lastName = newAuthor.getLastName();
            int oldId = oldAuthor.getId();
            String sql = "UPDATE authors SET first_name = '"+firstName+"', last_name = '"+lastName+"' WHERE id = "+oldId+";";
            return statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(Book book){
        int id = book.getId();
        try{
            statement = connection.createStatement();
            String sql = "DELETE FROM books WHERE id = " + id + ";";
            return statement.execute(sql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteAuthor(Author author) throws SQLException {
        int id = author.getId();
        statement = connection.createStatement();
        String sql = "DELETE FROM authors WHERE id = " + id + ";";
        return statement.execute(sql);
    }
    //factory method (building an object)
    public Author findAuthorById(int id){
        IPerson author = null;
        try{
            statement = this.connection.createStatement();
            String sql = "SELECT * FROM authors WHERE id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                int returnId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                author = new Author(firstName, lastName);
                author.setId(returnId);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return (Author) author;
    }
    //factory method (building an object)
    public Author findAuthorByName(String name){
        IPerson author = null;
        try{
            statement = this.connection.createStatement();
            String sql = "SELECT * FROM authors WHERE first_name = '" + name + "' OR last_name ='"+name+"';";
            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                int returnId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                author = new Author(firstName, lastName);
                author.setId(returnId);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return (Author) author;
    }


    public List<Book> booksWritenBy(Author author){
        List<Book> result = new ArrayList();
        try{
            statement = connection.createStatement();
            String sql = "SELECT * FROM books WHERE author_id = " + author.getId() + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                Librarian librarian = findLibrarianById(resultSet.getInt("librarian_id"));
                Book book = new Book(title, author, librarian);
                book.setId(id);
                result.add(book);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    //factory method (building an object)
    public Librarian findLibrarianById(int id){
        IPerson librarian = null;
        try{
            statement = this.connection.createStatement();
            String sql = "SELECT * FROM librarians WHERE id = " + id +";";
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                int returnId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String userName = resultSet.getString("user_name");
                librarian = new Librarian(firstName, lastName, userName);
                librarian.setId(returnId);
            }
        }catch (SQLException e){
            librarian = null;
        }
        return (Librarian) librarian;
    }


    //factory method build Librarian object
    public  Librarian authenticateLibrarian(String userName, String password) {
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM librarians WHERE user_name = '" + userName + "' AND password ='" + password + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                System.out.println("First Name from db: "+resultSet.getString("first_name"));
                return findLibrarianById(id);
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
