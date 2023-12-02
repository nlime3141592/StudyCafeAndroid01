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

import com.example.mysecondproject.R;

import study.customer.main.CustomerManager;
import study.customer.main.IResponsable;
import study.customer.service.ReserveService;
import study.customer.service.TimetableSelectService;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import customfonts.MyTextView_Poppins_Medium;

public class TimePickerDialogFragment extends DialogFragment {

    private String seatNum;
    private String pickedDate;


    private String startTime;
    private String endTime;

    private String selectedTime;

    List<String> timeList = new ArrayList<>();
    private View view;
    ListView timeListView;

    public TimePickerDialogFragment(String seatNum, String pickedDate)
    {
        this.seatNum = seatNum;
        this.pickedDate = pickedDate;
    }

    @NonNull
    @Override
    //나중에 서버랑 이용시간 연동해야함
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // NOTE: Dialog 생성
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_time_picker, null);

        timeListView = view.findViewById(R.id.timeListView);

        TimetableSelectHandler tsHandler = new TimetableSelectHandler();
        tsHandler.setOnServiceSuccess(new onTimetableSelectServiceSuccess());
        TimetableSelectService tsService = new TimetableSelectService(tsHandler, pickedDate);
        CustomerManager.getManager().requestService(tsService);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.TimePickerDialogTheme);
        builder.setTitle(seatNum + "번 좌석 시간 선택").setView(view);

        // NOTE: 확인 버튼
        View btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] h = selectedTime.split(" ~ ");
                String startH = h[0].replace("시", "").trim();
                String endH = h[1].replace("시", "").trim();

                startTime = String.format("%s %s:00:00", pickedDate, startH);
                endTime = String.format("%s %s:00:00", pickedDate, endH);

                ReserveHandler rHandler = new ReserveHandler();
                rHandler.setOnServiceSuccess(new onReserveSuccess());
                rHandler.setOnServiceFailure(new onReserveFailure());
                ReserveService rService = new ReserveService(rHandler, seatNum, startTime, endTime);
                CustomerManager.getManager().requestService(rService);
            }
        });

        // NOTE: 취소 버튼
        View btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
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

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    private class onTimetableSelectServiceSuccess implements IResponsable<ArrayList<String>>
    {
        @Override
        public void onResponse(ArrayList<String> _lines)
        {
            int startOnair = Integer.parseInt(_lines.get(0));
            int endOnair = Integer.parseInt(_lines.get(1));

            System.out.println("line count: " + _lines.size());

            for(String str : _lines)
                System.out.println("tttttt: " + str);

            for (int beg = startOnair; beg <= endOnair - 1; beg++) {
                timeList.add(String.format("%d시 ~ %d시", beg, beg + 1));
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
    }

    private class onReserveSuccess implements IResponsable
    {
        @Override
        public void onResponse(Object _nullArg)
        {
            showConfirmationDialog(selectedTime);
            dismiss();
        }
    }

    private class onReserveFailure implements IResponsable
    {
        @Override
        public void onResponse(Object _nullArg)
        {
            showReservationErrorDialog();
            dismiss();
        }
    }
}
