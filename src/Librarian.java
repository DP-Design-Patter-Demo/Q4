import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by kaleab on 2/2/2019.
 */

class Librarian extends Person {
    public static Data db = Data.getSingletonDataAccessObject();
    //single object with global access

    private static String userName;
    private String password;

    public Librarian(String firstName, String lastName, String userName) {
        super(firstName, lastName);
        this.userName = userName;
    }

    public static boolean signUp(Librarian librarian){
        return db.signUpLibrarian(librarian);
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

    public String getUserName(){ return this.userName; }
    public List<Book> getAllBooks() {
        List<Book> bks = db.getAllBooks();
        return bks;
    }
    public List<Author> getAllAuthors(){
        List<Author> authors = db.getAllAuthors();
        return authors;
    }

    public boolean addAuthor(Author author){
            return db.addAuthor(author);
    }

    public List<Book> booksWrittenBy(Author author){
        return db.booksWritenBy(author);
    }
    public boolean deleteBook(Book b) {
        return db.deleteBook(b);
    }
    public boolean deleteAuthor(Author a) throws SQLException {
        return db.deleteAuthor(a);
    }

    public boolean addBook(Book book) {
        return db.addBook(book, this);
    }

    public Book findBook(String key) {

        return db.findBook(key);
    }

    public boolean updateBook(Book oldBook, Book newBook) {
        return db.updateBook(oldBook, newBook);
    }

    public boolean updateAuthor(Author oldAuthor, Author newAuthor){
        return db.updateAuthor(oldAuthor, newAuthor);
    }

    public Author findAuthor(String key){
        Author result = db.findAuthorByName(key);
        return result;
    }

    public String[] getProfile(){
        String[] profile = new String[3];
        profile[0] = Integer.toString(this.getId());
        profile[1] = this.getFirstName();
        profile[2] = this.getLastName();
        return profile;
    }

}
