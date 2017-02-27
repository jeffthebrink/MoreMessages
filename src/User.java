import java.util.ArrayList;

/**
 * Created by jeffbrinkley on 2/21/17.
 */
public class User {
    String name;
    String password;

    ArrayList<Message> messageArrayList = new ArrayList<>();

    public User(String name, String password) {

        this.name = name;
        this.password = password;
        messageArrayList = new ArrayList<>();
    }
}