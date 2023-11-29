package study.customer.service;

import android.os.Bundle;
import android.os.Message;

import study.customer.handler.ReservableWeekdaySelectHandler;
import study.customer.ni.INetworkModule;
import study.customer.ni.INetworkService;

public class ReservableWeekdaySelectService implements INetworkService {
    private INetworkModule m_netModule;
    ReservableWeekdaySelectHandler reservableWeekdaySelectHandler;
    private String day;

    public ReservableWeekdaySelectService(ReservableWeekdaySelectHandler reservableWeekdaySelectHandler, String day) {
        this.reservableWeekdaySelectHandler = reservableWeekdaySelectHandler;
        this.day = day;
    }

    @Override
    public boolean tryExecuteService() {
        m_netModule.writeLine("RESERVABLE_WEEKDAY_SELECT_SERVICE");
        m_netModule.writeLine(day);


        String serviceEnable = m_netModule.readLine();
        String response = m_netModule.readLine();

        Message message = reservableWeekdaySelectHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("response", response);
        bundle.putString("serviceEnable", serviceEnable);
        bundle.putString("day", day);

        message.setData(bundle);
        reservableWeekdaySelectHandler.sendMessage(message);

        return true;
    }

    @Override
    public void bindNetworkModule(INetworkModule _netModule)
    {
        m_netModule = _netModule;
    }
}