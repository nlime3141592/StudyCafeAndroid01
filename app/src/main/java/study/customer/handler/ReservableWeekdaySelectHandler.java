package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.mysecondproject.R;

import study.customer.gui.need_home_view.CustomDatePickerDialog;
import study.customer.gui.need_home_view.ShowSeatFragment;
import study.customer.main.IResponsable;

public class ReservableWeekdaySelectHandler extends Handler {
    CustomDatePickerDialog customDatePickerDialog;
    ShowSeatFragment showSeatFragment;

    public ReservableWeekdaySelectHandler(CustomDatePickerDialog customDatePickerDialog) {
        super();
        this.customDatePickerDialog = customDatePickerDialog;
    }

    public ReservableWeekdaySelectHandler()
    {
        super();
    }

    public ReservableWeekdaySelectHandler(ShowSeatFragment showSeatFragment) {
        super();
        this.showSeatFragment = showSeatFragment;
    }
    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        try
        {
            Bundle bundle = message.getData();
            String response = bundle.getString("response");
            //영업여부
            String serviceEnable = bundle.getString("serviceEnable");
            //선택한날짜
            String day = bundle.getString("day");

            System.out.println(String.format("반응 : %s", response));

            if (response.equals("<SUCCESS>")) {
                //영업일아니고
                if (serviceEnable.equals("0")) {
                    //캘린더창 연결됐을때
                    if (customDatePickerDialog != null) {

                        // customDatePickerDialog.updateFail();
                        //선택된날짜 텍스트 당일로 변경
                        // customDatePickerDialog.setToday();


                        //홈화면 경고문변경
                        //로직 너무 꼬여서 경고문변경 보류
                        //customDatePickerDialog.noneRecords();

                    }
                    //좌석화면 연결됐고, 예약하기 버튼을 누를 때(ShowSeatFragment안에서 경로설정)
                    else if (showSeatFragment != null) {
                        //경고창
                        showSeatFragment.updateFail("영업일이 아닙니다.");
                    }
                    else
                    {
                        showSeatFragment.updateFail("테스트 0");
                    }

                }
                //영업일이고
                else if (serviceEnable.equals("1")) {

                    //다이얼로그에서 날짜를 선택했을 때
                    if (customDatePickerDialog != null) {
                        //홈화면 경고문 빈칸
                        //로직 너무 꼬여서 경고문변경 보류
                        // customDatePickerDialog.onRecords();
                    }

                    //좌석화면 연결됐고, 예약하기 버튼 누를 때
                    if (showSeatFragment != null) {
                        //예약할 수 있는 시간대 보여주기
                        showSeatFragment.updateFail("테스트");
                        //showSeatFragment.showTimePickerDialog(showSeatFragment.getSeatNum());
                    }
                    else
                    {
                        showSeatFragment.updateFail("테스트 1");
                    }
                }
            } else if (response.equals("<FAILURE>")) {
                System.out.println("N == 0");
            } else if (response.equals("<ERROR>")) {
                System.out.println("에러");
            } else {
                System.out.println("그외처리");
            }

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
            TextView view = showSeatFragment.getView().findViewById(R.id.error);

            // view.setText(_ex.getMessage());
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
