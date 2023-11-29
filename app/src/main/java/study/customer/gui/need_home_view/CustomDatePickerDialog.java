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
import study.customer.service.ReservableWeekdaySelectService;

import java.text.SimpleDateFormat;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;

public class CustomDatePickerDialog extends Dialog {

    CustomDatePickerDialog customDatePickerDialog;
    HomeFragment homeFragment;
    private String selectedDate;
    private DatePicker datePicker;
    private MyTextView_Poppins_Medium buttonCancel;
    private MyTextView_Poppins_Medium buttonOk;
    private TextView textViewDate;
    private String dayOfWeekString;

    public CustomDatePickerDialog(HomeFragment homeFragment, @NonNull Context context,
                                  DatePickerDialog.OnDateSetListener listener) {

        super(context);
        this.homeFragment = homeFragment;
        ReservableWeekdaySelectHandler reservableWeekdaySelectHandler;
        reservableWeekdaySelectHandler = new ReservableWeekdaySelectHandler(this);
        textViewDate = homeFragment.getTextViewDate();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_date_picker_dialog);

        datePicker = findViewById(R.id.datePicker);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonOk = findViewById(R.id.buttonOk);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                listener.onDateSet(null, year, month, day);
                ReservableWeekdaySelectService reservableWeekdaySelectService =
                        new ReservableWeekdaySelectService(reservableWeekdaySelectHandler, selectedDate);
                reservableWeekdaySelectService.bindNetworkModule(IntroActivity.networkModule);
                IntroActivity.networkThread.requestService(reservableWeekdaySelectService);

                dismiss();
            }
        });
    }



    public void updateFail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

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
    }

    public void setToday() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.getTime()) + " ";

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeekString = homeFragment.getDayOfWeekString(dayOfWeek);

        homeFragment.setSelectedDate(selectedDate);
        homeFragment.setDayOfWeekString(dayOfWeekString);
        textViewDate.setText("선택된 날짜 : " + selectedDate + "(" + dayOfWeekString + ")");
    }


    public void onRecords() {
        TextView textView = homeFragment.getView().findViewById(R.id.textOnair);
        textView.setText("");
    }
    public void noneRecords() {
        TextView text = homeFragment.getView().findViewById(R.id.textOnair);
        text.setText("영업일이 아닙니다.");
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

}
