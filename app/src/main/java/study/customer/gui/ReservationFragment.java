package study.customer.gui;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import customfonts.MyTextView_Poppins_Medium;
import study.customer.handler.ReserveCancelHandler;
import study.customer.handler.ReserveSelectHandler;

import com.example.mysecondproject.R;

import study.customer.main.CustomerManager;
import study.customer.main.NetworkManager;
import study.customer.service.ReserveCancelService;
import study.customer.service.ReserveSelectService;

import java.io.IOError;
import java.util.ArrayList;

public class ReservationFragment extends Fragment {
    private ArrayList<String> lines;
    private View view;
    private ArrayList<ReservationRecord> m_records;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_reservation, container, false);
        m_records = new ArrayList<ReservationRecord>();

        ReserveSelectHandler reserveSelectHandler;
        reserveSelectHandler = new ReserveSelectHandler(this);
        ReserveSelectService reserveSelectService = new ReserveSelectService(reserveSelectHandler);
        CustomerManager.getManager().requestService(reserveSelectService);

        return view;
    }

    public void noneRecords() {
        TextView text = view.findViewById(R.id.text);
        text.setText("등록된 예약내역이 없습니다.");
    }

    public void updateRecords(ArrayList<String> lines)
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        for (int i = 0; i < lines.size(); i += 5)
        {
            ReservationRecord record = new ReservationRecord(this, lines, i);
            m_records.add(record);
            transaction.add(record, "ReservationRecord");
        }

        transaction.commit();
    }

    public void removeRecord(ReservationRecord _record)
    {
        if(!m_records.remove(_record))
            return;

        _record.removeRecordFromView();

        for(int i = 0; i < m_records.size(); ++i)
            m_records.get(i).setFragmentId(i + 1);
    }

    public void updateFail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View dialogView = getLayoutInflater().inflate(R.layout.fail_dialog, null);
        builder.setView(dialogView);

        customfonts.MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
        MyTextView_Poppins_Medium confirmButton = dialogView.findViewById(R.id.confirm_button);

        dialogTitle.setText("예약시간이 지나 취소할 수 없습니다.");
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

    public void setLines(ArrayList<String> lines)
    {
        this.lines = lines;
    }
}