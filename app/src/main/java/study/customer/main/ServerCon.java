package study.customer.main;

import java.net.Socket;

public class ServerCon
{
    public static synchronized Socket tryConnectToServer()
    {
        //본인아이피
        final String SERVER_IP = "113.198.236.207";

        //포트
        final int SERVER_PORT = 25565;

        try {
            Socket clientSocket = new Socket(SERVER_IP, SERVER_PORT);
            clientSocket.getOutputStream().write(1);
            clientSocket.getOutputStream().flush();
            return clientSocket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
