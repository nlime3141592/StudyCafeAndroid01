package study.customer.main;

import android.os.Bundle;
import android.os.Message;

import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import study.customer.handler.IntroHandler;
import study.customer.ni.INetworkService;
import study.customer.ni.IService;
import study.customer.ni.StudyThread;

public class CustomerManager extends StudyThread
{
    private static CustomerManager s_m_manager;

    private LoginData m_loginData;

    private ConcurrentLinkedQueue<IService> serviceQueue;
    private IntroHandler introHandler;

    public static CustomerManager getManager()
    {
        // NOTE:
        // 객체를 호출할 때, 만약 존재하지 않는다면 새로운 객체를 생성해서 할당합니다.
        // 존재한다면 새로운 객체를 생성하는 코드를 무시하고 바로 반환합니다.
        // 이러한 방법으로 CustomerManager 객체가 프로그램 전체에서 딱 1개만 존재함이 보장됩니다.
        if(s_m_manager == null) {
            s_m_manager = new CustomerManager();
        }

        return s_m_manager;
    }

    // NOTE: 외부에서 new CustomerManager()를 호출할 수 없도록 생성자를 private으로 선언합니다.
    private CustomerManager()
    {
        m_loginData = new LoginData();

        serviceQueue = new ConcurrentLinkedQueue<IService>();

        super.start();
    }

    @Override
    public void run()
    {
        try
        {
            if(!NetworkManager.tryConnectToServer())
                throw new SocketException("네트워크 연결 실패");

            this.tryPublishIntroHandler("ACCEPTED");

            while(super.isRun())
            {
                while(serviceQueue.size() > 0)
                {
                    IService service = serviceQueue.poll();

                    if(service instanceof INetworkService)
                        ((INetworkService)service).bindNetworkModule(NetworkManager.getNetworkModule());

                    service.tryExecuteService();
                }
            }
        }
        catch(Exception _ex)
        {
            // NOTE: 문제 발생으로 독서실 이용자 Thread 강제 종료
            this.tryPublishIntroHandler("DENIED");
        }

        super.stop();

        // NOTE:
        // 이 곳에 진입하였다면 Thread를 안전하게 종료한 것입니다.
        // 이 곳에서 할당했던 자원을 반환합니다.

        if(s_m_manager == this)
            s_m_manager = null;
    }

    public void requestService(IService iService)
    {
        serviceQueue.offer(iService);
    }

    public LoginData getLoginData() { return m_loginData; }

    public void setIntroHandler(IntroHandler introHandler)
    {
        this.introHandler = introHandler;
    }

    private boolean tryPublishIntroHandler(String introResponse)
    {
        if(introHandler == null)
            return false;

        Message message = introHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("intro-response", introResponse);
        message.setData(bundle);
        introHandler.sendMessage(message);
        introHandler = null;
        return true;
    }
}