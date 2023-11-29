package study.customer.gui.need_home_view;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import study.customer.handler.ReserveHandler;
import study.customer.handler.TimetableSelectHandler;
import study.customer.gui.HomeFragment;
import study.customer.gui.IntroActivity;
import com.example.mysecondproject.R;
import study.customer.service.ReserveService;
import study.customer.service.TimetableSelectService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;

public class TimePickerDialogFragment extends DialogFragment {

    private String seatNum;
    private String startTime;
    private String endTime;

    private String selectedTime;
    private String selectedDay;
    private int usingOnair;
    private int startOnair;
    private int endOnair;
    private TimePickerDialogFragment timePickerDialogFragment;
    private HomeFragment homeFragment;
    private ShowSeatFragment showSeat;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> onair = new ArrayList<>();
    List<String> timeList = new ArrayList<>();
    private View view;
    ListView timeListView;


    public void setSelectedDay(String selectedDay) {
        this.selectedDay = selectedDay;
    }

    public TimePickerDialogFragment(ShowSeatFragment showSeat, String seatNum, String startTime, String endTime, String selectedDay) {
        this.showSeat = showSeat;
        this.seatNum = seatNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.selectedDay = selectedDay;
    }

    @NonNull
    @Override
    //나중에 서버랑 이용시간 연동해야함
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        switch (selectedDay) {
            case "월":
                usingOnair = 0;
                break;
            case "화":
                usingOnair = 1;
                break;
            case "수":
                usingOnair = 2;
                break;
            case "목":
                usingOnair = 3;
                break;
            case "금":
                usingOnair = 4;
                break;
            case "토":
                usingOnair = 5;
                break;
            case "일":
                usingOnair = 6;
                break;
        }

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_time_picker, null);

        timeListView = view.findViewById(R.id.timeListView);

        TimetableSelectHandler timetableSelectHandler;
        timetableSelectHandler = new TimetableSelectHandler(this);
        TimetableSelectService timetableSelectService = new TimetableSelectService(timetableSelectHandler);
        timetableSelectService.bindNetworkModule(IntroActivity.networkModule);
        IntroActivity.networkThread.requestService(timetableSelectService);


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.TimePickerDialogTheme);
        builder.setTitle(seatNum + "번 좌석 시간 선택")
                .setView(view);
        View btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReserveHandler seatTimeHandler;
                String[] h = selectedTime.split(" ~ ");
                String startH = h[0].replace("시", "").trim();
                String endH = h[1].replace("시", "").trim();
                String day = showSeat.getSelectedDate();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    Date startDate = inputFormat.parse(day + " " + startH + ":00:00");
                    Date endDate = inputFormat.parse(day + " " + endH + ":00:00");
                        startTime = outputFormat.format(startDate);
                        endTime = outputFormat.format(endDate);
                } catch (ParseException e) {
                        e.printStackTrace();
                }

                for (int i = 0; i < lines.size(); i += 2) {
                    if (startTime.equals(lines.get(i))) {
                        showReservationErrorDialog();
                        return;
                    }
                }
                seatTimeHandler = new ReserveHandler(timePickerDialogFragment, v);
                ReserveService seatTimeService = new ReserveService(seatTimeHandler, seatNum, startTime, endTime);
                seatTimeService.bindNetworkModule(IntroActivity.networkModule);
                IntroActivity.networkThread.requestService(seatTimeService);
                showConfirmationDialog(selectedTime);
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



    public void update() {

        int j = 0;
        for (int i = 0; i < onair.size(); i += 2) {
            if (usingOnair == j) {
                startOnair = Integer.parseInt(onair.get(i));
                endOnair = Integer.parseInt(onair.get(i + 1));
            }
            j++;
        }


        for (int hour = startOnair; hour <= endOnair - 1; hour++) {
            timeList.add(hour + "시 ~ " + (hour+1)+"시");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_single_choice, timeList);
        timeListView.setAdapter(adapter);
        timeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        timeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = adapter.getItem(position);

            }
        });
    }

    //예약실패 시 뜨는 팝업창
    private void showReservationErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View dialogView = getLayoutInflater().inflate(R.layout.fail_dialog, null);
        builder.setView(dialogView);

        customfonts.MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
        MyTextView_Poppins_Medium confirmButton = dialogView.findViewById(R.id.confirm_button);

        dialogTitle.setText("이미 예약된 좌석입니다.");
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

    //예약성공 시 뜨는 팝업창
    private void showConfirmationDialog(String selectedTime) {
        String confirmationMessage = seatNum + "번 좌석, " + selectedTime + "에 예약이 완료되었습니다.";
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View dialogView = getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(dialogView);

        MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
        MyTextView_Poppins_Medium confirmButton = dialogView.findViewById(R.id.confirm_button);

        dialogTitle.setText(confirmationMessage);
        dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        confirmButton.setText("확인");


        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }


    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }
    public void setOnair(ArrayList<String> onair) {
        this.onair = onair;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
