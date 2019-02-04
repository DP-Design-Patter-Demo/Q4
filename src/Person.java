/**
 * Created by kaleab on 2/2/2019.
 */
public class Person implements IPerson { //bridge pattern (decoupling implementation of an object and its interface
    private int id;
    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void setId(int id){ this.id = id; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName){ this.lastName = lastName; }
    public int getId() { return this.id; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
}
