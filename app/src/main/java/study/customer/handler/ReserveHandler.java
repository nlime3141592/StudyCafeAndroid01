package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import study.customer.gui.need_home_view.TimePickerDialogFragment;

public class ReserveHandler extends Handler {
    View view;
    TimePickerDialogFragment timePickerDialogFragment;

    public ReserveHandler(TimePickerDialogFragment timePickerDialogFragment, View view) {
        super();
        this.view = view;
        this.timePickerDialogFragment = timePickerDialogFragment;
    }
    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");

        if (response.equals("<SUCCESS>")) {
            System.out.println("좌석예약성공");
        } else if (response.equals("<FAILURE>")) {
            System.out.println("N == 0");
        } else if (response.equals("<ERROR>")) {
            System.out.println("에러");
        }else{
            System.out.println("그외처리");
        }
    }
}
