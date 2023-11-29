package study.customer.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mysecondproject.R;

import study.customer.main.CustomerManager;

public class InfoFragment extends Fragment {
    private String userNickname = "사용자 닉네임";
    private String userAccount = "사용자 아이디";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);


        TextView nicknameTextView = view.findViewById(R.id.nicknameTextView);
        TextView accountTextView = view.findViewById(R.id.idTextView);


        userNickname = CustomerManager.getManager().getLoginData().getNickname();
        userAccount = CustomerManager.getManager().getLoginData().getId();
        nicknameTextView.setText("닉네임 : " + userNickname);
        accountTextView.setText("아이디 : " + userAccount);

        return view;
    }
}
