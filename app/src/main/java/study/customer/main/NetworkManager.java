package study.customer.main;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import study.customer.ni.INetworkModule;
import study.customer.ni.IService;
import study.customer.ni.NetworkModule;
import study.customer.ni.StudyThread;

public class NetworkManager
{
    private static final String SERVER_IP = "113.198.236.207";
    private static final int SERVER_PORT = 25565;

    private static NetworkModule networkModule;

    public static boolean tryConnectToServer()
    {
        try
        {
            Socket clientSocket = ServerCon.connectToServer();

            if (clientSocket == null) {
                System.out.println("서버 연결 실패");
                return false;
            }

            networkModule = new NetworkModule(clientSocket);
            return true;
        }
        catch(Exception _ex)
        {
            if(networkModule != null)
                networkModule.stop();

            return false;
        }
    }

    public static INetworkModule getNetworkModule() { return networkModule; }
}
