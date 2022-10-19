package ru.ngs.summerjob.greet;

import ru.ngs.summerjob.net.Greetable;

public class MorningGreet extends Greetable {
    @Override
    public String buildResponse(String username) {
        return "Good morning, " + username;
    }
}
