package study.customer.gui;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mysecondproject.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import customfonts.MyTextView_Poppins_Medium;
import study.customer.handler.ReserveCancelHandler;
import study.customer.main.CustomerManager;
import study.customer.main.NetworkManager;
import study.customer.service.ReserveCancelService;

public class ReservationRecord extends Fragment
{
    private ReservationFragment reservationFragment;
    private LinearLayout containerLayout;
    private int fragmentId;
    private int reserveId;
    private int seatId;
    private LocalDateTime timeBegin;
    private LocalDateTime timeEnd;
    private LocalDateTime reservationDate;

    private View recordView;
    private TextView numTextView;
    private TextView seatNumTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView dayTextView;
    private TextView btnOpen;
    private TextView btnDelete;
    private TextView reserveIdView;

    public ReservationRecord(ReservationFragment _reservationFragment, ArrayList<String> _lines, int _startIndex)
    {
        reservationFragment = _reservationFragment;

        fragmentId = 1 + _startIndex / 5;

        reserveId = Integer.parseInt(_lines.get(_startIndex));

        seatId = Integer.parseInt(_lines.get(_startIndex + 1));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timeBegin = LocalDateTime.parse(_lines.get(_startIndex + 2), dateTimeFormatter);
        timeEnd = LocalDateTime.parse(_lines.get(_startIndex + 3), dateTimeFormatter);
        reservationDate = LocalDateTime.parse(_lines.get(_startIndex + 4), dateTimeFormatter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        containerLayout = reservationFragment.getView().findViewById(R.id.recordsContainer);
        recordView = inflater.inflate(R.layout.record_layout_first, containerLayout, false);
        numTextView = recordView.findViewById(R.id.num);
        seatNumTextView = recordView.findViewById(R.id.seatNum);
        startTimeTextView = recordView.findViewById(R.id.startTime);
        endTimeTextView = recordView.findViewById(R.id.endTime);
        dayTextView = recordView.findViewById(R.id.day);
        btnOpen = recordView.findViewById(R.id.btnOk);
        btnDelete = recordView.findViewById(R.id.btnDelete);
        reserveIdView = recordView.findViewById(R.id.reserveId1);

        numTextView.setText(String.valueOf(fragmentId));
        reserveIdView.setText(String.format("%d", reserveId));
        seatNumTextView.setText(String.format("좌석 : %d", seatId));
        startTimeTextView.setText(String.format("시작 시간 : %d시", timeBegin.getHour()));
        endTimeTextView.setText(String.format("종료 시간 : %d시", timeEnd.getHour()));
        dayTextView.setText(String.format("등록한 시간\n%s", reservationDate.toString()));

        // 현재 layoutParams == null임.
        ViewGroup.LayoutParams layoutParams = recordView.getLayoutParams();

        layoutParams.height = 263;
        recordView.setLayoutParams(layoutParams);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int targetHeight = 508;
                if (recordView.getHeight() == 263) {
                    btnOpen.setText("닫기");
                    expandView(recordView, targetHeight);
                } else {
                    btnOpen.setText("열기");
                    collapseView(recordView);
                }
            }
        });

        ReserveCancelHandler reserveCancelHandler = new ReserveCancelHandler(this.reservationFragment, this, this.recordView);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View dialogView = getLayoutInflater().inflate(R.layout.question_mark_dialog, null);
                builder.setView(dialogView);

                customfonts.MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
                MyTextView_Poppins_Medium btnNo = dialogView.findViewById(R.id.btnNo);
                MyTextView_Poppins_Medium btnYes = dialogView.findViewById(R.id.btnYes);

                dialogTitle.setText("정말로 삭제하시겠습니까?");
                btnNo.setText("아니요");
                btnYes.setText("네");

                AlertDialog dialog = builder.create();

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReserveCancelService reserveCancelService = new ReserveCancelService(reserveCancelHandler, Integer.toString(reserveId));
                        CustomerManager.getManager().requestService(reserveCancelService);

                        dialog.dismiss();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        containerLayout.addView(recordView);
        return recordView;
    }

    public void removeRecordFromView()
    {
        containerLayout.removeView(recordView);
    }

    public void setFragmentId(int _id)
    {
        fragmentId = _id;
        numTextView.setText(String.valueOf(fragmentId));
    }

    private void expandView(final View view, int targetHeight) {
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(view.getHeight(), targetHeight)
                .setDuration(300);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });

        slideAnimator.start();
    }

    private void collapseView(final View view) {
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(view.getHeight(), 263)
                .setDuration(300);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });

        slideAnimator.start();
    }
}
