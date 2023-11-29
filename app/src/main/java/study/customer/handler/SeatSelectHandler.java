package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import study.customer.gui.need_home_view.ShowSeatFragment;

import java.util.ArrayList;

public class SeatSelectHandler extends Handler {
    ShowSeatFragment showSeatFragment;

    public SeatSelectHandler(ShowSeatFragment showSeatFragment) {
        super();
        this.showSeatFragment = showSeatFragment;
    }


    @Override
    public void handleMessage(@NonNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");


        ArrayList<String> lines = bundle.getStringArrayList("lines");

        if (response.equals("<SUCCESS>")) {
            System.out.println("좌석 예약내역 통신성공");
            showSeatFragment.setLines(lines);
            showSeatFragment.updateRecords(lines);
        } else if (response.equals("<FAILURE>")) {
            System.out.println("좌석 예약내역 없음");
            showSeatFragment.noneRecords();
        } else if (response.equals("<ERROR>")) {
            System.out.println("에러");
        } else {
            System.out.println("그 외 처리");
        }
    }

}
