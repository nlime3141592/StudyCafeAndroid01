package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import study.customer.gui.need_home_view.TimePickerDialogFragment;
import study.customer.main.IResponsable;

public class ReserveHandler extends Handler {
    View view;
    TimePickerDialogFragment timePickerDialogFragment;

    private IResponsable m_onServiceSuccess;
    private IResponsable m_onServiceFailure;
    private IResponsable m_onServiceError;
    private IResponsable m_onServiceDefault;

    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");

        switch(response)
        {
            case "<SUCCESS>":
                if(m_onServiceSuccess != null)
                    m_onServiceSuccess.onResponse(null);
                // System.out.println("좌석예약성공");
                break;
            case "<FAILURE>":
                if(m_onServiceFailure != null)
                    m_onServiceFailure.onResponse(null);
                // System.out.println("N == 0");
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

    public void setOnServiceSuccess(IResponsable _response)
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
