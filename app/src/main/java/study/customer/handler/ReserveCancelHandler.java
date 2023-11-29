package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import study.customer.gui.ReservationFragment;
import study.customer.gui.ReservationRecord;

public class ReserveCancelHandler extends Handler {
    private ReservationFragment reservationFragment;
    private ReservationRecord reservationRecord;
    private LinearLayout linearLayout;
    private View recordView;


    public ReserveCancelHandler(ReservationFragment reservationFragment, ReservationRecord record, LinearLayout linearLayout, View recordView) {
        super();
        this.reservationFragment = reservationFragment;
        this.reservationRecord = record;
        this.linearLayout = linearLayout;
        this.recordView = recordView;
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");


        if (response.equals("<SUCCESS>")) {
            System.out.println("통신성공");
            linearLayout.removeView(recordView);
        } else if (response.equals("<FAILURE>")) {
            System.out.println("없음");
            reservationFragment.updateFail();
        } else if (response.equals("<ERROR>")) {
            System.out.println("에러");
        } else {
            System.out.println("그 외 처리");
        }
    }
}
