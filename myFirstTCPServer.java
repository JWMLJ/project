import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;

/**
 * TCP Server
 * 
 * Usage: java myFirstTCPServer.java <port>
 * 
 * Simply receives messages and returns them in all caps
 * 
 * Note: Be sure to run this and myFirstTCPClient in separate processes
 */
public class myFirstTCPServer {
    
    private static final int DEFAULT_PORT = 10010;
    private static final int BUFSIZE = 32; // Size of receive buffer

    public static void main(String[] args) throws IOException {

        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: <Port>");
        }

        int port = args.length == 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        var serverSocket = new ServerSocket(port);

        byte[] byteBuffer = new byte[BUFSIZE];

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println(String.format(
                    "Handling client at %s on port %d",
                    clientSocket.getInetAddress().getHostAddress(),
                    clientSocket.getPort()
                ));

                var in = clientSocket.getInputStream();
                var out = clientSocket.getOutputStream();

                int recvMsgSize = in.read(byteBuffer);
                
                String response;
                if (recvMsgSize != 2) {
                    response = "****";
                } else {
                    response = new String(byteBuffer, 0, recvMsgSize).toUpperCase();
                }
                
                out.write(response.getBytes());
                clientSocket.close();
            } catch (SocketException e) {
                System.out.println("Connection is closed");
                break;
            }
        }
    }
}
