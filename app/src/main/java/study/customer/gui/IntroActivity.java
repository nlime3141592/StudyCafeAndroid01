package study.customer.gui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysecondproject.R;

import study.customer.handler.IntroHandler;
import study.customer.main.CustomerManager;
import study.customer.main.NetworkManager;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                IntroHandler handler = new IntroHandler(IntroActivity.this);
                CustomerManager.getManager().setIntroHandler(handler);
                CustomerManager.getManager().start();
            }
        },1000);  //1000으로 변경
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void onServiceEnterAccepted()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onServiceEnterDenied()
    {
        System.out.println("서버에 연결할 수 없습니다.");
    }
}