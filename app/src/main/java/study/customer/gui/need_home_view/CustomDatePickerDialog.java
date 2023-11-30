package study.customer.gui.need_home_view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import study.customer.handler.ReservableWeekdaySelectHandler;
import study.customer.gui.HomeFragment;
import study.customer.gui.IntroActivity;

import com.example.mysecondproject.R;

import org.jetbrains.annotations.NotNull;

import study.customer.main.CustomerManager;
import study.customer.main.IResponsable;
import study.customer.service.ReservableWeekdaySelectService;

import java.text.SimpleDateFormat;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;

public class CustomDatePickerDialog extends Dialog {

    HomeFragment homeFragment;
    private String selectedDate;
    private DatePicker datePicker;
    private MyTextView_Poppins_Medium buttonCancel;
    private MyTextView_Poppins_Medium buttonOk;
    private TextView textViewDate;
    private String dayOfWeekString;

    ///////////////////////////////////
    private TextView m_textViewDate;
    private TextView m_textViewOnair;
    private ReservableWeekdaySelectHandler m_handler;
    private int m_yyyy;
    private int m_MM;
    private int m_dd;

    public CustomDatePickerDialog(TextView _textViewDate, TextView _textViewOnair, @NotNull Context _context)
    {
        super(_context);
        m_textViewDate = _textViewDate;
        m_textViewOnair = _textViewOnair;
        m_handler = new ReservableWeekdaySelectHandler();

        m_handler.setOnSuccess(new onResponseSuccess());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_date_picker_dialog);

        datePicker = findViewById(R.id.datePicker);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonOk = findViewById(R.id.buttonOk);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int _yyyy, int _MM, int _dd)
            {
                m_yyyy = _yyyy;
                m_MM = _MM;
                m_dd = _dd;
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservableWeekdaySelectService service;
                service = new ReservableWeekdaySelectService(m_handler, getDateString(m_yyyy, m_MM, m_dd));
                CustomerManager.getManager().requestService(service);

                dismiss();
            }
        });
    }

    private class onResponseSuccess implements IResponsable<Integer>
    {
        @Override
        public void onResponse(Integer _serviceEnable)
        {
            if(_serviceEnable == 1)
            {
                // Service available.
                // 날짜 선택 완료
                m_textViewOnair.setText("예약할 수 있는 날짜입니다.");
            }
            else if(_serviceEnable == 0)
            {
                // Service not available.
                // 원래 updateFail() 함수에 있던 내용들
                //경고창
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomDatePickerDialog.this.getContext());

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

                //선택된날짜 텍스트 당일로 변경
                String defaultDateMessage = String.format("선택된 날짜 : %s (%s)", getTodayDateString(), getDayOfWeekString());
                m_textViewDate.setText(defaultDateMessage);
                m_textViewOnair.setText("영업일이 아닙니다.");
            }
            else
            {
                // Occurred unknown error.
            }
        }
    }

    private String getDateString(int _yyyy, int _MM, int _dd)
    {
        return String.format("%04d-%02d-%02d", _yyyy, _MM, _dd);
    }

    private String getTodayDateString()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getDayOfWeekString()
    {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return "일월화수목금토".substring(dayOfWeek - 1, dayOfWeek);
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }
}
