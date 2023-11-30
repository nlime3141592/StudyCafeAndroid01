package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import study.customer.gui.need_home_view.SeatSummaryFragment;
import study.customer.main.IResponsable;

import java.util.ArrayList;

public class SeatSelectHandler extends Handler {

    private IResponsable<ArrayList<String>> m_onServiceSuccess;
    private IResponsable m_onServiceFailure;
    private IResponsable m_onServiceError;
    private IResponsable m_onServiceDefault;

    public SeatSelectHandler()
    {
        super();
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");
        ArrayList<String> lines = bundle.getStringArrayList("lines");

        switch(response)
        {
            case "<SUCCESS>":
                if(m_onServiceSuccess != null)
                    m_onServiceSuccess.onResponse(lines);
                // System.out.println("좌석 예약내역 통신성공");
                break;
            case "<FAILURE>":
                if(m_onServiceFailure != null)
                    m_onServiceFailure.onResponse(null);
                // System.out.println("좌석 예약내역 없음");
                break;
            case "<ERROR>":
                if(m_onServiceError != null)
                    m_onServiceError.onResponse(null);
                // System.out.println("에러");
                break;
            default:
                if(m_onServiceDefault != null)
                    m_onServiceDefault.onResponse(null);
                // System.out.println("그 외 처리");
                break;
        }
    }

    public void setOnServiceSuccess(IResponsable<ArrayList<String>> _response)
    {
        m_onServiceSuccess = _response;
    }

    public void setOnServiceFailure(IResponsable _response)
    {
        m_onServiceFailure = _response;
    }

    public void setOnServiceError(IResponsable _response)
    {
        m_onServiceError = _response;
    }

    public void setOnServiceDefault(IResponsable _response)
    {
        m_onServiceDefault = _response;
    }
}
