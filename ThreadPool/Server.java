package ThreadPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadpool;

    public Server(int poolSize){
        this.threadpool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clienSocket){
        try{
            PrintWriter toSocket = new PrintWriter(clienSocket.getOutputStream(), true);
            toSocket.println("Hello from the server"+ clienSocket.getInetAddress());
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public static void main(String args[]){
        int port = 8010;
        int poolSize = 10;
        Server server = new Server(poolSize);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                server.threadpool.execute(()->server.handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            server.threadpool.shutdown();
        }

    }
}
