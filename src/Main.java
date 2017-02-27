import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static HashMap<String, User> userHashMap = new HashMap<>();


    public static void main(String[] args) {
        System.out.println("Starting MoreMessages app...");
        Spark.init();

        Spark.get("/", (request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = userHashMap.get(name);

                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {
                        return new ModelAndView(user, "messages.html");
                    }
                },
                new MustacheTemplateEngine()
        );


        Spark.post("/index", (request, response) -> { // send and display index page showing login fields
            String name = request.queryParams("loginName");
            String password = request.queryParams("password");
            if (!userHashMap.containsKey(name)) {
                User user = new User(name, password);// assign user input to user object (login name and password)
                userHashMap.put(name, user); // put that info in the hashmap with the username as the key
            }
            Session session = request.session();
            session.attribute("userName", name); // attach cookie info to name for this session
            response.redirect("/");
            return "";
        });

        Spark.post("/messages", (request, response) -> {
            Message m1 = new Message(request.queryParams("message"));
            Session session = request.session();
            String userName = session.attribute("userName");
            User user = userHashMap.get(userName);
            if (!m1.message.isEmpty()) {
                Message message = new Message(m1.message);
                user.messageArrayList.add(message);
            }
            response.redirect("/");
            return "";
        });

        Spark.post("/logout", (request, response) -> {
            Session session = request.session();
            session.invalidate();
            response.redirect("/");
            return "";
        });

        Spark.post("/login", (request, response) -> {
            String name = request.queryParams("loginName");
            Session session = request.session();
            session.attribute("userName", name);    // track user in session cookie
            response.redirect("/");
            return "";

        });

        Spark.post("/deleteMessage", (request, response) -> {
            int deleteNumber = Integer.valueOf(request.queryParams("deleteNumber")) - 1;
            Session session = request.session();
            String userName = session.attribute("userName");
            User user = userHashMap.get(userName);
            user.messageArrayList.remove(deleteNumber);
            response.redirect("/");
            return "";
        });

        Spark.post("/editMessage", (request, response) -> {
            int editNumber = Integer.valueOf(request.queryParams("editNumber")) - 1;
            Message newMessage = new Message(request.queryParams("newMessage"));
            Session session = request.session();
            String userName = session.attribute("userName");
            User user = userHashMap.get(userName);
            if (!(editNumber == 0) && (!(newMessage == null))) {
                user.messageArrayList.set(editNumber, newMessage);
            }
            response.redirect("/");
            return "";
        });

    }
}