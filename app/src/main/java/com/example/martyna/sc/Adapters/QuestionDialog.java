package com.example.martyna.sc.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.martyna.sc.R;

/**
 * Created by Martyna on 2016-01-18.
 */
public class QuestionDialog extends Dialog {

    public EditText answer;
    public TextView question;
    public Button okButton;

    public QuestionDialog(final Context context) {
        // Set your theme here
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);

        // This is the layout XML file that describes your Dialog layout
        this.setContentView(R.layout.question);

        this.setContentView(R.layout.question);
        this.setTitle("Pytanie");
        this.setCancelable(false);

        question = (TextView) this.findViewById(R.id.question_text);
       // question.setText(q.getQuestion());

        answer = (EditText) this.findViewById(R.id.answer_text);

        // co sie stanie jak chcemy sprawdzic odpowiedz
        okButton = (Button) this.findViewById(R.id.button);
    }
}