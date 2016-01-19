package com.example.martyna.sc.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import com.example.martyna.sc.R;

/**
 * Created by Martyna on 2016-01-18.
 */
public class ResultGameDialog extends Dialog {

    public Button button;
    public TextView result;
    public ResultGameDialog(final Context context) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        this.setContentView(R.layout.result_game);
        this.setTitle("Twój wynik");
        this.setCancelable(false);
        result = (TextView) this.findViewById(R.id.result);
        button = (Button) this.findViewById(R.id.button);

    }
}