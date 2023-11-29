package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import study.customer.gui.need_home_view.TimePickerDialogFragment;

import java.util.ArrayList;

public class TimetableSelectHandler extends Handler {
    TimePickerDialogFragment timePickerDialogFragment;

    public TimetableSelectHandler(TimePickerDialogFragment timePickerDialogFragment) {
        super();
        this.timePickerDialogFragment = timePickerDialogFragment;
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");


        ArrayList<String> onair = bundle.getStringArrayList("lines");


        if (response.equals("<SUCCESS>")) {
            System.out.println("통신성공");
            timePickerDialogFragment.setOnair(onair);
            timePickerDialogFragment.update();
        } else if (response.equals("<FAILURE>")) {
            System.out.println("없음");
        } else if (response.equals("<ERROR>")) {
            System.out.println("에러");
        } else {
            System.out.println("그 외 처리");
        }
    }
}
