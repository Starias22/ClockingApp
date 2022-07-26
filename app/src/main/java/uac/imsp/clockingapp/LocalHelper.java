package uac.imsp.clockingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LocalHelper {

    //the metthod is used to set the language at runtime
    @SuppressLint("ObsoleteSdkInt")
    public static void setLocale(Context context, String language){
        //updating language for devices above nougat
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            updateResources(context, language);
            return;
        }
        //for devices having lower version of android os
        updateResourceLegacy(context, language);
    }

    @SuppressLint("ObsoleteSdkInt")
    private static void updateResourceLegacy(@NonNull Context context, String language) {
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Resources resources=context.getResources();
        Configuration configuration=resources.getConfiguration();
        configuration.locale=locale;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }

    private static void updateResources(@NonNull Context context, String language) {
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        //context.getApplicationContext().getResources().updateConfiguration(config, null);
        context.createConfigurationContext(configuration);
    }


}
