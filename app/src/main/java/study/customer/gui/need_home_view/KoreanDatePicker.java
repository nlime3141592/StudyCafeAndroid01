package study.customer.gui.need_home_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class KoreanDatePicker extends DatePicker {

    private Paint textPaint;

    public KoreanDatePicker(Context context) {
        super(context);
        init();
    }

    public KoreanDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KoreanDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KoreanDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN);
        String formattedDate = dateFormat.format(getCalendarView().getDate());

        canvas.drawText(formattedDate, 20, 40, textPaint);
    }
}
