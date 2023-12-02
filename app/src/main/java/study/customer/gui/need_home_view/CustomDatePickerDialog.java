package study.customer.gui.need_home_view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;

import study.customer.handler.ReservableWeekdaySelectHandler;

import com.example.mysecondproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import study.customer.main.CustomerManager;
import study.customer.main.IResponsable;
import study.customer.main.LocaleManager;
import study.customer.service.ReservableWeekdaySelectService;

import customfonts.MyTextView_Poppins_Medium;

public class CustomDatePickerDialog extends Dialog {

    private DatePicker datePicker;
    private MyTextView_Poppins_Medium buttonCancel;
    private MyTextView_Poppins_Medium buttonOk;

    private ReservableWeekdaySelectHandler m_handler;
    private int m_year;
    private int m_month;
    private int m_day;

    // NOTE: DatePicker에서 값을 선택할 때마다 changed 변수가 갱신됩니다.
    private int m_changedY;
    private int m_changedM;
    private int m_changedD;

    private IResponsable<String> m_onTimePickSuccess;
    private IResponsable<String> m_onTimePickFailure;

    public CustomDatePickerDialog(@NotNull Context _context)
    {
        super(_context);
        m_handler = new ReservableWeekdaySelectHandler();
        m_handler.setOnSuccess(new onServiceSuccess());
        m_handler.setOnFailure(new onServiceFailure());
        m_handler.setOnError(new onServiceFailure());
        m_handler.setOnDefault(new onServiceFailure());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_date_picker_dialog);

        datePicker = findViewById(R.id.datePicker);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonOk = findViewById(R.id.buttonOk);

        Calendar calendar = LocaleManager.getCalendar();
        m_year = calendar.get(Calendar.YEAR);
        m_month = calendar.get(Calendar.MONTH) + 1;
        m_day = calendar.get(Calendar.DAY_OF_MONTH);
        m_changedY = m_year;
        m_changedM = m_month;
        m_changedD = m_day;
        datePicker.setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        datePicker.setMaxDate(calendar.getTimeInMillis());

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int _yyyy, int _MM, int _dd)
            {
                m_changedY = _yyyy;
                m_changedM = _MM + 1;
                m_changedD = _dd;
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
                service = new ReservableWeekdaySelectService(m_handler, String.format("%04d-%02d-%02d", m_changedY, m_changedM, m_changedD));
                CustomerManager.getManager().requestService(service);
            }
        });
    }

    public void setOnTimePickSuccess(IResponsable<String> _response)
    {
        m_onTimePickSuccess = _response;
    }

    public void setOnTimePickFailure(IResponsable<String> _response)
    {
        m_onTimePickFailure = _response;
    }

    private class onServiceSuccess implements IResponsable<Integer>
    {
        @Override
        public void onResponse(Integer _serviceEnable)
        {
            if(_serviceEnable == 1) // Service available.
            {
                m_year = m_changedY;
                m_month = m_changedM;
                m_day = m_changedD;

                // 날짜 선택 완료
                if(m_onTimePickSuccess != null)
                    // NOTE: 선택 성공한 날짜를 전송
                    m_onTimePickSuccess.onResponse(String.format("%04d-%02d-%02d", m_year, m_month, m_day));

                dismiss();
            }
            else if(_serviceEnable == 0) // Service not available.
            {
                //경고창
                // TODO: 경고창을 코드 분리할 수 있는지 생각해 볼 필요 있습니다.
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

                if(m_onTimePickFailure != null)
                    // NOTE: 선택 실패한 날짜를 전송
                    m_onTimePickFailure.onResponse(String.format("%04d-%02d-%02d", m_changedY, m_changedM, m_changedD));

                dismiss();
            }
            else
            {
                // Occurred unknown error.
                dismiss();
            }
        }
    }

    private class onServiceFailure implements IResponsable
    {
        @Override
        public void onResponse(Object _serviceEnable)
        {
            // TODO: 서버와 통신 실패하였고, 인트로 액티비티로 이동하는 코드가 여기 옵니다.
        }
    }
}
