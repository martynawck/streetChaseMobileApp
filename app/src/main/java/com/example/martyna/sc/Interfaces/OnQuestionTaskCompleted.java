package com.example.martyna.sc.Interfaces;

import com.example.martyna.sc.Models.Question;
import com.example.martyna.sc.Models.StreetGame;

import java.util.ArrayList;

/**
 * Created by Martyna on 2016-01-12.
 */
public interface OnQuestionTaskCompleted {
    //void onTaskCompleted(ArrayList<StreetGame> strings);
    void onQuestionReturned(Question question);
   // void onControlPointReturned(ControlPoint controlPoint);
}
