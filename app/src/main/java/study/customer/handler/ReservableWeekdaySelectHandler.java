package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.mysecondproject.R;

import study.customer.gui.HomeFragment;
import study.customer.gui.IntroActivity;
import study.customer.gui.need_home_view.CustomDatePickerDialog;
import study.customer.gui.need_home_view.ShowSeatFragment;
import study.customer.ni.INetworkModule;
import study.customer.ni.INetworkService;
import study.customer.ni.Service;
import study.customer.main.NetworkManager;

class TestService extends Service implements INetworkService
{
    private INetworkModule m_netModule;
    private TextView m_view;

    public TestService(TextView _view)
    {
        m_view = _view;
    }

    @Override
    public boolean tryExecuteService()
    {
        String response = m_netModule.readLine();
        m_view.setText(response);
        return true;
    }

    @Override
    public void bindNetworkModule(INetworkModule _netModule)
    {
        m_netModule = _netModule;
        m_netModule.writeLine("TEST_SERVICE_01");
    }
}

public class ReservableWeekdaySelectHandler extends Handler {
    CustomDatePickerDialog customDatePickerDialog;
    ShowSeatFragment showSeatFragment;
    HomeFragment homeFragment;

    public ReservableWeekdaySelectHandler(CustomDatePickerDialog customDatePickerDialog) {
        super();
        this.customDatePickerDialog = customDatePickerDialog;
    }

    public ReservableWeekdaySelectHandler(HomeFragment homeFragment) {
        super();
        this.homeFragment = homeFragment;
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


            if (response.equals("<SUCCESS>")) {
                //영업일아니고
                if (serviceEnable.equals("0")) {
                    //캘린더창 연결됐을때
                    if (customDatePickerDialog != null) {
                        //경고창
                        customDatePickerDialog.updateFail();
                        //선택된날짜 텍스트 당일로 변경
                        customDatePickerDialog.setToday();


                        //홈화면 경고문변경
                        //로직 너무 꼬여서 경고문변경 보류
                        //customDatePickerDialog.noneRecords();

                    }
                    //홈화면 연결됐을때
                    else if (homeFragment != null) {
                        //홈화면 경고문
                        //homeFragment.noneRecords();
                    }
                    //좌석화면 연결됐고, 예약하기 버튼을 누를 때(ShowSeatFragment안에서 경로설정)
                    else if (showSeatFragment != null) {
                        //경고창
                        showSeatFragment.updateFail();
                    }

                }
                //영업일이고
                else if (serviceEnable.equals("1")) {

                    //다이얼로그에서 날짜를 선택했을 때
                    if (customDatePickerDialog != null) {
                        //홈화면 경고문 빈칸
                        //로직 너무 꼬여서 경고문변경 보류
                        customDatePickerDialog.onRecords();
                    }

                    //좌석화면 연결됐고, 예약하기 버튼 누를 때
                    if (showSeatFragment != null) {
                        //예약할 수 있는 시간대 보여주기

                            showSeatFragment.showTimePickerDialog(showSeatFragment.getSeatNum());
                    }
                }
            } else if (response.equals("<FAILURE>")) {
                System.out.println("N == 0");
            } else if (response.equals("<ERROR>")) {
                System.out.println("에러");
            } else {
                System.out.println("그외처리");
            }

        }
        catch(Exception _ex)
        {
            TextView view = showSeatFragment.getView().findViewById(R.id.error);

            TestService service = new TestService(view);
            service.bindNetworkModule(IntroActivity.networkModule);
            NetworkManager.getManager().requestService(service);

            // view.setText(_ex.getMessage());
        }
    }

}
