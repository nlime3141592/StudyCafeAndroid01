package study.customer.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import study.customer.handler.LoginHandler;
import com.example.mysecondproject.R;

import study.customer.main.CustomerManager;
import study.customer.main.NetworkManager;
import study.customer.service.LoginService;

import customfonts.MyTextView_Poppins_Medium;

public class LoginActivity extends AppCompatActivity {
    private EditText etxtAccount;
    private EditText etxtPassword;
    private TextView errorTextView;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etxtAccount = ((EditText)super.findViewById((R.id.editTextAccount)));
        etxtPassword = ((EditText)super.findViewById(R.id.editTextPassword));
        errorTextView = findViewById(R.id.Error);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        MyTextView_Poppins_Medium buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        MyTextView_Poppins_Medium buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etxtAccount.getText().toString().trim();
                String password = etxtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    // 키패드 내리기
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // 아이디와 비밀번호 입력 칸 비우기
                    etxtAccount.setText("");
                    etxtPassword.setText("");

                    errorTextView.setText("아이디와 비밀번호를 모두 입력하세요.");
                    errorTextView.setTextColor(Color.RED);
                    return;
                }

                LoginHandler loginHandler = new LoginHandler(LoginActivity.this, v);
                LoginService loginService = new LoginService(loginHandler, account, password);
                CustomerManager.getManager().requestService(loginService);
            }
        });
    }

    public void onResponseSuccess()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onResponseBlackedAccount()
    {
        this.showfailDialog();
    }

    public void onResponseFailure(View view)
    {
        errorTextView.setTextColor(Color.RED);
        errorTextView.setText("아이디 또는 비밀번호가 올바르지 않습니다.");

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        etxtAccount.setText("");
        etxtPassword.setText("");
    }

    private void showfailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.fail_dialog, null);
        builder.setView(dialogView);

        customfonts.MyTextView_Poppins_Medium dialogTitle = dialogView.findViewById(R.id.dialog_title);
        MyTextView_Poppins_Medium confirmButton = dialogView.findViewById(R.id.confirm_button);

        dialogTitle.setText("이용이 정지된 계정입니다.");
        confirmButton.setText("확인");

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }
}