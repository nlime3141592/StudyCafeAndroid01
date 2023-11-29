package study.customer.handler;

import android.os.Handler;
import android.os.Message;

import org.jetbrains.annotations.NotNull;

import study.customer.gui.IntroActivity;

public class IntroHandler extends Handler
{
    private IntroActivity introActivity;

    public IntroHandler(IntroActivity introActivity)
    {
        this.introActivity = introActivity;
    }

    public void handleMessage(@NotNull Message message)
    {
        String introResponse = message.getData().getString("intro-response");

        switch(introResponse)
        {
            case "ACCEPTED":
                introActivity.onServiceEnterAccepted();
                break;
            case "DENIED":
                introActivity.onServiceEnterDenied();
                break;
            default:
                break;
        }
    }
}
