import java.util.List;


/**
 * Created by kaleab on 2/2/2019.
 */

class Librarian extends Person {
    public static Data db = Data.getSingletonDataAccessObject();
    //single object with global access

    private static String userName;
    private static String password;

    public Librarian(String firstName, String lastName, String userName) {
        super(firstName, lastName);
        this.userName = userName;
    }

    public static Librarian isAuthenticated(String userName, String password){
       return db.authenticateLibrarian(userName, password);
    }

    public String getUserName(){ return this.userName; }
    public List<Book> getAllBooks() {
        List<Book> bks = db.getAllBooks();
        return bks;
    }

    public boolean deleteBook(Book b) {
        return db.deleteBook(b);
    }

    public boolean addBook(Book book) {
        return this.db.addBook(book, this);
    }

    public Book findBook(String key) {

        return db.findBook(key);
    }

    public boolean updateBook(Book oldBook, Book newBook) {
        return db.updateBook(oldBook, newBook);
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
