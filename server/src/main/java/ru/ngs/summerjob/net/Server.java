package ru.ngs.summerjob.net;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(25225, 2000);

        Map<String, Greetable> handlers = loadHandlers();

        System.out.println("Server is started.");

        while (true) {
            Socket client = socket.accept();
            new SimpleServer(client, handlers).start();
        }
    }

    private static Map<String, Greetable> loadHandlers() {
        Map<String, Greetable> result = new HashMap<>();
        try (InputStream is = Server.class.getClassLoader().getResourceAsStream("server.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            for (Object command : properties.keySet()) {
                String className = properties.getProperty(command.toString());
                Class<Greetable> cl = (Class<Greetable>) Class.forName(className);
                Greetable handler = cl.getConstructor().newInstance();
                result.put(command.toString(), handler);
            }

        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        return result;
    }
}

class SimpleServer extends Thread {
    private final Socket client;
    private final Map<String, Greetable> handlers;
    SimpleServer(Socket client, Map<String, Greetable> handlers) {
        this.client = client;
        this.handlers = handlers;
    }


    public void run() {
        handleRequest();
    }

    private void handleRequest() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            String request = br.readLine();
            String[] lines = request.split(" ");
            String command = lines[0];
            String userName = lines[1];

            System.out.println("Server got string: " + command + " // " + userName);
//            Thread.sleep(2000);

            String response = buildResponse(command, userName);
            bw.write(response);
            bw.newLine();
            bw.flush();

            bw.close();
            br.close();

            client.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    private String buildResponse(String command, String userName) {
        Greetable handler = handlers.get(command);
        if (handler != null) {
            return handler.buildResponse(userName);
        }
        return "Hello + " + userName;
    }
}
