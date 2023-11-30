package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;


import study.customer.main.IResponsable;

public class ReservableWeekdaySelectHandler extends Handler {
    public ReservableWeekdaySelectHandler()
    {
        super();
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        try
        {
            Bundle bundle = message.getData();
            String response = bundle.getString("response");
            String serviceEnable = bundle.getString("serviceEnable");
            System.out.println(String.format("반응 : %s", response));

            switch(response)
            {
                case "<SUCCESS>":
                    if(m_onSuccess != null)
                        m_onSuccess.onResponse(Integer.parseInt(serviceEnable));
                    break;
                case "<FAILURE>":
                    if(m_onFailure != null)
                        m_onFailure.onResponse(null);
                    // System.out.println("N == 0");
                    break;
                case "<ERROR>":
                    if(m_onError != null)
                        m_onError.onResponse(null);
                    // System.out.println("에러");
                    break;
                default:
                    if(m_onDefault != null)
                        m_onDefault.onResponse(null);
                    // System.out.println("그외처리");
                    break;
            }
        }
        catch(Exception _ex)
        {
            System.out.println("폴로: 너는 이번 실행에서 '치명적인' 예외를 발생시켰군.");
        }
    }

    private IResponsable<Integer> m_onSuccess;
    private IResponsable m_onFailure;
    private IResponsable m_onError;
    private IResponsable m_onDefault;

    public void setOnSuccess(IResponsable<Integer> _response) { m_onSuccess = _response; }
    public void setOnFailure(IResponsable _response) { m_onFailure = _response; }
    public void setOnError(IResponsable _response) { m_onError = _response; }
    public void setOnDefault(IResponsable _response) { m_onDefault = _response; }
}
