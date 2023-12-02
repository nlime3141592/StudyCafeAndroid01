package study.customer.gui;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

import study.customer.handler.ReservableWeekdaySelectHandler;

import com.example.mysecondproject.R;

import study.customer.gui.need_home_view.CustomDatePickerDialog;
import study.customer.gui.need_home_view.SeatSummaryFragment;
import study.customer.main.CustomerManager;
import study.customer.main.IResponsable;
import study.customer.main.LocaleManager;
import study.customer.service.ReservableWeekdaySelectService;

import java.time.LocalDateTime;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;

public class HomeFragment extends Fragment {
    private TextView textViewDate;
    private TextView textViewOnair;
    private String m_pickedDate;
    private boolean m_isServiceEnabledDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LocaleManager.setLocale("ko");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewDate = view.findViewById(R.id.textViewDate);
        textViewDate.setText("");
        textViewOnair = view.findViewById(R.id.textOnair);
        textViewOnair.setText("");
        textViewOnair.setTextColor(Color.RED);

        // 오늘 날짜를 설정
        LocalDateTime now = LocalDateTime.now();
        String nowDateString = String.format("%04d-%02d-%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        ReservableWeekdaySelectHandler handler = new ReservableWeekdaySelectHandler();
        handler.setOnSuccess(new OnServicableCheckSuccess(nowDateString));
        handler.setOnFailure(new OnServicableCheckFailure());
        handler.setOnError(new OnServicableCheckFailure());
        handler.setOnDefault(new OnServicableCheckFailure());
        ReservableWeekdaySelectService service;
        service = new ReservableWeekdaySelectService(handler, nowDateString);
        CustomerManager.getManager().requestService(service);

        //날짜선택버튼
        MyTextView_Poppins_Medium btnDateSelect = view.findViewById(R.id.buttonLogin);
        btnDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        setSeatButtonClickListeners(view);

        return view;
    }

    // 좌석등록
    private void setSeatButtonClickListeners(View view) {
        int[] seatButtonIds = {
                R.id.seat_1, R.id.seat_2, R.id.seat_3, R.id.seat_4, R.id.seat_5,
                R.id.seat_6, R.id.seat_7, R.id.seat_8, R.id.seat_9, R.id.seat_10,
                R.id.seat_11, R.id.seat_12, R.id.seat_13, R.id.seat_14, R.id.seat_15,
                R.id.seat_16, R.id.seat_17, R.id.seat_18, R.id.seat_19, R.id.seat_20,
                R.id.seat_21, R.id.seat_22, R.id.seat_23, R.id.seat_24, R.id.seat_25,
                R.id.seat_26, R.id.seat_27, R.id.seat_28, R.id.seat_29, R.id.seat_30,
                R.id.seat_31, R.id.seat_32, R.id.seat_33, R.id.seat_34, R.id.seat_35
        };

        for (int seatButtonId : seatButtonIds) {
            View seatButton = view.findViewById(seatButtonId);
            seatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String seatNum = ((TextView) v).getText().toString();
                    showSeat(seatNum);
                }
            });
        }
    }

    public void showSeat(String seatNum) {
        SeatSummaryFragment showSeat = new SeatSummaryFragment(seatNum, m_pickedDate);
        showSeat.show(getParentFragmentManager(), "show_seat");
    }

    private void updateGUI()
    {
        textViewDate.setText(String.format("선택한 날짜 : %s", m_pickedDate));
        if(m_isServiceEnabledDate)
            textViewOnair.setText("");
        else
            textViewOnair.setText("영업하지 않는 날짜입니다.");
    }

    // 날짜선택 다이얼로그
    private void showDatePicker() {
        CustomDatePickerDialog customDatePickerDialog;
        customDatePickerDialog = new CustomDatePickerDialog(requireContext());
        customDatePickerDialog.setOnTimePickSuccess(new OnTimePickSuccess());
        customDatePickerDialog.setOnTimePickFailure(new OnTimePickFailure());
        customDatePickerDialog.show();
    }

    private class OnServicableCheckSuccess implements IResponsable<Integer>
    {
        private String m_now;

        public OnServicableCheckSuccess(String _now)
        {
            m_now = _now;
        }

        @Override
        public void onResponse(Integer _serviceEnable)
        {
            m_pickedDate = m_now;
            m_isServiceEnabledDate = (_serviceEnable == 1);
            updateGUI();
        }
    }

    private class OnServicableCheckFailure implements IResponsable
    {
        @Override
        public void onResponse(Object _eventData)
        {
            // TODO: 인트로 액티비티로 이동하는 코드가 여기 들어옵니다.
        }
    }

    private class OnTimePickSuccess implements IResponsable<String>
    {
        @Override
        public void onResponse(String _date)
        {
            m_pickedDate = _date;
            m_isServiceEnabledDate = true;
            updateGUI();
        }
    }

    private class OnTimePickFailure implements IResponsable<String>
    {
        @Override
        public void onResponse(String _date)
        {
            m_pickedDate = _date;
            m_isServiceEnabledDate = false;
            updateGUI();
        }
    }
}
