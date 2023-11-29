package study.customer.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import study.customer.gui.LoginActivity;

import org.jetbrains.annotations.NotNull;

public class LoginHandler extends Handler
{
    View view;
    LoginActivity loginActivity;

    public LoginHandler(LoginActivity loginActivity, View view) {
        super();
        this.loginActivity = loginActivity;
        this.view = view;
    }

    @Override
    public void handleMessage(@NotNull Message message) {
        super.handleMessage(message);
        Bundle bundle = message.getData();
        String response = bundle.getString("response");

        if (response.equals("<SUCCESS>")) {
            loginActivity.onResponseSuccess();
        } else if(response.equals("<BLACKED_ACCOUNT>")) {
            loginActivity.onResponseBlackedAccount();
        } else if (response.equals("<FAILURE>")) {
            loginActivity.onResponseFailure(view);
        } else {
            System.out.println("test");
        }
    }
}
