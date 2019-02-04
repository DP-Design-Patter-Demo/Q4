/**
 * Created by kaleab on 2/2/2019.
 */

public class Book {
    private int id;
    private String title;
    private Author author;
    private Librarian librarian;

    public Book(String title, Author author, Librarian librarian){
        this.title = title;
        this.author = author;
        this.librarian = librarian;
    }

    public int getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public Author getAuthor() {
        return this.author;
    }
    public Librarian getLibrarian(){
        return this.librarian;
    }
    public void setId(int id) {
        this.id = id;
    }
}
