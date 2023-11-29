package study.customer.service;



import android.os.Bundle;
import android.os.Message;

import study.customer.handler.JoinHandler;
import study.customer.ni.INetworkModule;
import study.customer.ni.INetworkService;
import study.customer.ni.NetworkLiteral;


public class JoinService implements INetworkService
{
    private JoinHandler joinHandler;
    private INetworkModule m_netModule;
    private String m_id;
    private String m_pw;
    private String m_nickname;

    public JoinService(JoinHandler joinHandler,String  _id, String _pw, String _nickname)
    {
        this.joinHandler = joinHandler;
        m_id = _id;
        m_pw = _pw;
        m_nickname = _nickname;
    }

    @Override
    public boolean tryExecuteService()
    {
        m_netModule.writeLine("JOIN_SERVICE");
        m_netModule.writeLine(m_id);
        m_netModule.writeLine(m_pw);
        m_netModule.writeLine("1");
        m_netModule.writeLine(m_nickname);
        m_netModule.writeLine(NetworkLiteral.EOF);

        String response = m_netModule.readLine();

        Message message = joinHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("response", response);

        message.setData(bundle);
        joinHandler.sendMessage(message);

        return true;
    }

    @Override
    public void bindNetworkModule(INetworkModule _netModule)
    {
        m_netModule = _netModule;
    }
}