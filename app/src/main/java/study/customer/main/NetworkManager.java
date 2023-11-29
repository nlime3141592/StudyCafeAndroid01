package study.customer.main;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import study.customer.ni.INetworkModule;
import study.customer.ni.IService;
import study.customer.ni.NetworkModule;
import study.customer.ni.StudyThread;

public class NetworkManager extends StudyThread
{
    private static final String SERVER_IP = "113.198.236.207";
    private static final int SERVER_PORT = 25565;

    private static NetworkManager s_m_manager;

    private NetworkModule networkModule;
    private ConcurrentLinkedQueue<IService> serviceQueue;

    private NetworkManager()
    {
        serviceQueue = new ConcurrentLinkedQueue<>();
    }

    public static NetworkManager getManager()
    {
        if(s_m_manager == null)
            s_m_manager = new NetworkManager();

        return s_m_manager;
    }

    public static boolean isConnected()
    {
        return s_m_manager != null;
    }

    @Override
    public void run()
    {
        try {
            Socket clientSocket = ServerCon.tryConnectToServer();

            if (clientSocket == null) {
                System.out.println("서버 연결 실패");
                s_m_manager = null;
                return;
            }

            networkModule = new NetworkModule(clientSocket);

            while (super.isRun()) {
                while (serviceQueue.size() > 0) {
                    serviceQueue.poll().tryExecuteService();
                }
            }

            System.out.println("클라이언트 정상 종료");

            if(networkModule != null)
                networkModule.stop();

            s_m_manager = null;
        }
        catch(Exception _ex)
        {
            System.out.println("서버 연결 실패");

            if(networkModule != null)
                networkModule.stop();

            s_m_manager = null;
        }
    }

    public void requestService(IService iService) { serviceQueue.offer(iService); }
    public INetworkModule getNetworkModule() { return networkModule; }
}
