package study.customer.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import study.customer.handler.JoinHandler;
import com.example.mysecondproject.R;

import study.customer.main.CustomerManager;
import study.customer.service.JoinService;

import customfonts.MyTextView_Poppins_Medium;

public class JoinActivity extends AppCompatActivity {
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextNickname;
    private TextView errorTextView;
    private InputMethodManager imm;

    //가입 후 팝업
    public void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(dialogView);

        customfonts.MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
        MyTextView_Poppins_Medium confirmButton = dialogView.findViewById(R.id.confirm_button);

        dialogTitle.setText("회원가입 완료");
        confirmButton.setText("확인");

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        editTextId = findViewById(R.id.editTextAccount);
        editTextPw = findViewById(R.id.editTextPassword);
        editTextNickname = findViewById(R.id.editTextNickname);
        errorTextView = findViewById(R.id.Error);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        MyTextView_Poppins_Medium backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyTextView_Poppins_Medium buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = editTextNickname.getText().toString().trim();
                String account = editTextId.getText().toString().trim();
                String password = editTextPw.getText().toString().trim();

                if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    // 빈칸이 있을 경우, 에러 메세지 텍스트뷰에 메시지 설정
                    errorTextView.setTextColor(Color.RED);
                    errorTextView.setText("빈칸을 채워주세요");

                    // 키패드 내리기
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // 아이디와 비밀번호 입력 칸 비우기
                    editTextId.setText("");
                    editTextPw.setText("");
                    return;
                }

                //기존코드라인
                JoinHandler joinHandler = new JoinHandler(JoinActivity.this, v);
                JoinService joinService = new JoinService(joinHandler, account, password, nickname);
                CustomerManager.getManager().requestService(joinService);
            }
        });
    }
}
