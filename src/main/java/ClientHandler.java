import Models.TODO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.Socket;

public class ClientHandler implements Runnable {
    final Socket socket;
    private String method;
    private String resource;
    private String model;
    private int id;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        DataOutputStream output = null;
        BufferedReader input = null;

        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String received;
            while ((received = input.readLine()) != null) {
                if (received.indexOf("HTTP") != -1){
                    String[] HTTPstring = received.split(" ");
                    //Recovering method's type
                    this.method = HTTPstring[0];
                    System.out.println("Method: " + this.method);
                    //Recovering resource
                    this.resource = HTTPstring[1];
                    System.out.println("Resource: " + this.resource);
                    String[] resourceSplit = this.resource.split("/");
                    this.model = resourceSplit[1];
                    System.out.println("Model: " + this.model);
                    if (resourceSplit.length > 2){
                        this.id = Integer.parseInt(resourceSplit[2]);
                        System.out.println("id: " + this.id);
                    }
                }

/*                switch (this.method){
                    case "GET":
                        if(this.id)
                            TODO.all();
                        else
                            TODO.findID(this.id);
                        break;
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
