package ru.ngs.summerjob.greet;

import ru.ngs.summerjob.net.Greetable;

public class HelloGreet extends Greetable {
    @Override
    public String buildResponse(String username) {
        return "Hello, " + username;
    }
}
