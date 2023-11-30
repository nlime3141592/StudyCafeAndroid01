package study.customer.gui.need_home_view;

import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import study.customer.handler.ReservableWeekdaySelectHandler;
import study.customer.handler.SeatSelectHandler;
import study.customer.gui.HomeFragment;

import com.example.mysecondproject.R;

import study.customer.main.CustomerManager;
import study.customer.main.IResponsable;
import study.customer.service.ReservableWeekdaySelectService;
import study.customer.service.SeatSelectService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;


public class SeatSummaryFragment extends DialogFragment {
    private String seatNum;
    private String pickedDate;
    private SeatSelectHandler m_handler;
    private View view;

    public SeatSummaryFragment(String _seatNum, String _pickedDate)
    {
        seatNum = _seatNum;
        pickedDate = _pickedDate;
        m_handler = new SeatSelectHandler();
        m_handler.setOnServiceSuccess(new onSeatSelectServiceSuccess());
        m_handler.setOnServiceFailure(new onSeatSelectServiceFailure());
        m_handler.setOnServiceError(new onSeatSelectServiceError());
        m_handler.setOnServiceDefault(new onSeatSelectServiceError());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SeatSelectService seatSelectService = new SeatSelectService(m_handler, seatNum);
        CustomerManager.getManager().requestService(seatSelectService);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.seat_select_first, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.TimePickerDialogTheme);
        builder.setTitle(seatNum + "번 좌석 예약정보").setView(view);

        TextView text = view.findViewById(R.id.text);
        text.setText("서버와 통신 중 ...");

        View btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservableWeekdaySelectHandler handler = new ReservableWeekdaySelectHandler();
                handler.setOnSuccess(new onReservableWeekdaySelectHandlerSuccess());
                handler.setOnFailure(new onReservableWeekdaySelectHandlerError());
                handler.setOnError(new onReservableWeekdaySelectHandlerError());
                handler.setOnDefault(new onReservableWeekdaySelectHandlerError());
                ReservableWeekdaySelectService service = new ReservableWeekdaySelectService(handler, pickedDate);
                CustomerManager.getManager().requestService(service);
            }
        });

        View btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    private class onSeatSelectServiceSuccess implements IResponsable<ArrayList<String>>
    {
        @Override
        public void onResponse(ArrayList<String> _lines)
        {
            LinearLayout containerLayout = view.findViewById(R.id.recordsContainer1);
            TextView text0 = view.findViewById(R.id.text);
            text0.setText("");

            for(int i = 0; i < _lines.size(); i += 2)
            {
                View recordView = getLayoutInflater().inflate(R.layout.seat_record_layout, containerLayout, false);
                TextView text = recordView.findViewById(R.id.text1);

                String beginTimeString = _lines.get(i);
                String endTimeString = _lines.get(i + 1);

                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime beginTime = LocalDateTime.parse(beginTimeString, format);
                LocalDateTime endTime = LocalDateTime.parse(endTimeString, format);

                String message = String.format("%d년 %d월 %d일 %d시 ~ %d시",
                        beginTime.getYear(),
                        beginTime.getMonthValue(),
                        beginTime.getDayOfMonth(),
                        beginTime.getHour(),
                        endTime.getHour()
                );

                text.setText(message);
                containerLayout.addView(recordView);
            }
        }
    }

    private class onSeatSelectServiceFailure implements IResponsable
    {
        @Override
        public void onResponse(Object _object)
        {
            TextView text = view.findViewById(R.id.text);
            text.setText("등록된 예약내역이 없습니다.");
        }
    }

    private class onSeatSelectServiceError implements IResponsable
    {
        @Override
        public void onResponse(Object _object)
        {

        }
    }

    private class onReservableWeekdaySelectHandlerSuccess implements IResponsable<Integer>
    {
        @Override
        public void onResponse(Integer _serviceEnable)
        {
            // 타임 피커 열기
            if(_serviceEnable == 0)
            {
                System.out.println("onReservableWeekdaySelectHandlerFailure");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View dialogView = getLayoutInflater().inflate(R.layout.fail_dialog, null);
                builder.setView(dialogView);

                customfonts.MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
                MyTextView_Poppins_Medium confirmButton = dialogView.findViewById(R.id.confirm_button);

                dialogTitle.setText("영업일이 아닙니다.");
                confirmButton.setText("확인");

                AlertDialog dialog = builder.create();

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();
                dismiss();
            }
            else if(_serviceEnable == 1)
            {
                TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment(seatNum, pickedDate);
                dialogFragment.show(getParentFragmentManager(), "time_picker");
                dismiss();
            }
            else
            {

            }
        }
    }

    private class onReservableWeekdaySelectHandlerError implements IResponsable
    {
        @Override
        public void onResponse(Object _object)
        {
            // TODO: 통신 오류로 인트로 액티비티로 이동하는 코드 필요
            dismiss();
        }
    }
}
