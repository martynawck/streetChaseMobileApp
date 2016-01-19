package com.example.martyna.sc.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.martyna.sc.R;

/**
 * Created by Martyna on 2016-01-18.
 */
public class FinishedGameDialog extends Dialog {

    public Button button;
    public TextView result;
    public FinishedGameDialog(final Context context) {
        // Set your theme here
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);

        // This is the layout XML file that describes your Dialog layout
        this.setContentView(R.layout.finished_game);

        this.setTitle("Koniec gry!");
        this.setCancelable(false);

        result = (TextView) this.findViewById(R.id.result);
        button = (Button) this.findViewById(R.id.button);

    }
}