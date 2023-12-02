package study.customer.main;

import java.util.Calendar;
import java.util.Locale;

public class LocaleManager
{
    private LocaleManager()
    {

    }

    public static void setLocale(String languageCode)
    {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
    }

    public static Calendar getCalendar()
    {
        return Calendar.getInstance(Locale.getDefault());
    }

    public static String getCurrentDateString()
    {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", y, m, d);
    }
}
