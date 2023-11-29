package study.customer.gui;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mysecondproject.R;

import java.util.ArrayList;

import customfonts.MyTextView_Poppins_Medium;
import study.customer.handler.ReserveCancelHandler;
import study.customer.main.NetworkManager;
import study.customer.service.ReserveCancelService;

public class ReservationRecord extends Fragment
{
    private ReserveCancelHandler reserveCancelHandler;
    private int fragmentId;
    private int reserveId;
    private int seatId;
    private int timeBegin;
    private int timeEnd;
    private String reservationDate;

    private View recordView;
    private TextView numTextView;
    private TextView seatNumTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView dayTextView;
    private TextView btnOpen;
    private TextView btnDelete;
    private TextView reserveIdView;

    public ReservationRecord(ReserveCancelHandler _handler, ArrayList<String> _lines, int _startIndex)
    {
        reserveCancelHandler = _handler;

        fragmentId = 1 + _startIndex / 5;

        reserveId = Integer.parseInt(_lines.get(_startIndex));

        seatId = Integer.parseInt(_lines.get(_startIndex + 1));

        String[] timeBeginTokens = _lines.get(_startIndex + 2).split(":");
        timeBegin = Integer.parseInt(timeBeginTokens[0]);

        String[] timeEndTokens = _lines.get(_startIndex + 3).split(":");
        timeEnd = Integer.parseInt(timeEndTokens[0]);

        reservationDate = _lines.get(_startIndex + 4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        recordView = inflater.inflate(R.layout.record_layout_first, container, false);
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
        seatNumTextView.setText(String.format("좌석 : ", seatId));
        startTimeTextView.setText(String.format("시작 시간 : %d시", timeBegin));
        endTimeTextView.setText(String.format("종료 시간 : %d시", timeEnd));
        dayTextView.setText(String.format("등록한 시간\n%s", reservationDate));

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

        reserveCancelHandler = new ReserveCancelHandler(this, containerLayout, recordView);

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
                        reserveCancelService.bindNetworkModule(NetworkManager.getManager().getNetworkModule());
                        NetworkManager.getManager().requestService(reserveCancelService);

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
        c++;

        return recordView;
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
