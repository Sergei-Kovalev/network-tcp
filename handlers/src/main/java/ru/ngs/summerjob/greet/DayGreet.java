package ru.ngs.summerjob.greet;

import ru.ngs.summerjob.net.Greetable;

public class DayGreet extends Greetable {
    @Override
    public String buildResponse(String username) {
        return "Good day, " + username;
    }
}
