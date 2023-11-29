package study.customer.handler;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import study.customer.gui.JoinActivity;
import com.example.mysecondproject.R;

public class JoinHandler extends Handler {
    View view;
    JoinActivity joinActivity;
    public JoinHandler(JoinActivity joinActivity, View view) {
        super();
        this.joinActivity = joinActivity;
        this.view = view;
    }

    @Override
    public void handleMessage(@NonNull Message message){
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");


        if (response.equals("<DUPLICATED_ID>")) {
            // 중복된 아이디 처리
            TextView errorTextView = joinActivity.findViewById(R.id.Error);
            errorTextView.setText("사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
            errorTextView.setTextColor(Color.RED);
            // 키패드 내리기
            InputMethodManager imm = (InputMethodManager) joinActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //칸 비우기
            ((EditText)joinActivity.findViewById(R.id.editTextAccount)).setText("");

        } else if (response.equals("<DUPLICATED_NICKNAME>")) {
            // 중복된 닉네임 처리
            TextView errorTextView = joinActivity.findViewById(R.id.Error);
            errorTextView.setText("사용할 수 없는 닉네임입니다. 다른 닉네임을 입력해 주세요.");
            errorTextView.setTextColor(Color.RED);
            // 키패드 내리기
            InputMethodManager imm = (InputMethodManager)joinActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //칸비우기
            ((EditText)joinActivity.findViewById(R.id.editTextNickname)).setText("");
        } else if(response.equals("<SUCCESS>")){
            // 정상적으로 처리된 경우
            // 회원가입 성공 팝업창 띄우기
            joinActivity.showSuccessDialog();
        }
        else{
            System.out.println("회원가입 문제1");
        }
    }
}
