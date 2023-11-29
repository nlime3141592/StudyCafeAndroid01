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

import study.customer.gui.IntroActivity;
import com.example.mysecondproject.R;
import study.customer.service.ReservableWeekdaySelectService;
import study.customer.service.SeatSelectService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;


public class ShowSeatFragment extends DialogFragment {
    private String seatNum;
    private String startTime;
    private String endTime;

    private String selectedTime;
    private ShowSeatFragment showSeatFragment;
    private HomeFragment homeFragment;
    private String selectedDate;
    private ArrayList<String> lines = new ArrayList<>();
    private View view;
    private String usingForTimePickerday;
    private String serviceEnable;
    public ShowSeatFragment(HomeFragment homeFragment, String seatNum, String startTime, String endTime, String selectedDate, String usingForTimePickerday) {
        this.homeFragment = homeFragment;
        this.seatNum = seatNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.selectedDate = selectedDate;
        this.usingForTimePickerday = usingForTimePickerday;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SeatSelectHandler seatSelectHandler;
        seatSelectHandler = new SeatSelectHandler(this);
        SeatSelectService seatSelectService = new SeatSelectService(seatSelectHandler, seatNum);
        seatSelectService.bindNetworkModule(IntroActivity.networkModule);
        IntroActivity.networkThread.requestService(seatSelectService);

        ReservableWeekdaySelectHandler reservableWeekdaySelectHandler;
        reservableWeekdaySelectHandler = new ReservableWeekdaySelectHandler(this);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.seat_select_first, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.TimePickerDialogTheme);
        builder.setTitle(seatNum + "번 좌석 예약정보")
                .setView(view);
        View btnOk = view.findViewById(R.id.btnOk);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservableWeekdaySelectService reservableWeekdaySelectService =
                        new ReservableWeekdaySelectService(reservableWeekdaySelectHandler, selectedDate);
                reservableWeekdaySelectService.bindNetworkModule(IntroActivity.networkModule);
                IntroActivity.networkThread.requestService(reservableWeekdaySelectService);

                dismiss();
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
    public void noneRecords() {
        TextView text = view.findViewById(R.id.text);
        text.setText("등록된 예약내역이 없습니다.");
    }
    public void updateFail() {
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
    }
    public void updateRecords(ArrayList<String> lines) {
        lines = this.lines;
        LinearLayout containerLayout = view.findViewById(R.id.recordsContainer1);

        for (int i = 0; i < lines.size(); i += 2) {
            View recordView = getLayoutInflater().inflate(R.layout.seat_record_layout, containerLayout, false);
            TextView text1 = recordView.findViewById(R.id.text1);

            String startTime = lines.get(i);
            String endTime = lines.get(i + 1);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);

                String year = String.valueOf(calendar.get(Calendar.YEAR));
                String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                String startHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

                calendar.setTime(endDate);
                String endHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

                String combinedText = String.format("%s년 %s월 %s일 %s시 ~ %s시", year, month, day, startHour, endHour);

                text1.setText(combinedText);

                containerLayout.addView(recordView);
            } catch (ParseException e) {
                //e.printStackTrace();
            }
        }
    }
    public void showTimePickerDialog(String seatNum) {
        TimePickerDialogFragment dialogFragment = new TimePickerDialogFragment(this, seatNum, startTime, endTime, usingForTimePickerday);
        dialogFragment.show(getParentFragmentManager(), "time_picker");
        dialogFragment.setLines(lines);
    }
    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSeatNum() {
        return seatNum;
    }
}
