package ir.rastanco.mobilemarket.utility;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ir.rastanco.mobilemarket.R;

/**
 * Created by ParisaRashidhi on 27/04/2016.
 */
public class ToastUtility {


    private final static ToastUtility config = new ToastUtility();

    public static ToastUtility getConfig() {
        if (config != null) {
            return config;
        } else return new ToastUtility();
    }

    public void displayToast(String textToShowInToast , Activity toastUtilityActivity){

        LayoutInflater inflater = toastUtilityActivity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) toastUtilityActivity.findViewById(R.id.custom_toast_layout));
        TextView text = (TextView) layout.findViewById(R.id.mobilemarket_toast);
        text.setText(textToShowInToast);
        Toast toast = new Toast(toastUtilityActivity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
