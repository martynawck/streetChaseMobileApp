package com.example.martyna.sc;

import com.example.martyna.sc.Models.ControlPoint;
import com.example.martyna.sc.Models.StreetGame;

import java.util.ArrayList;

/**
 * Created by Martyna on 2016-01-12.
 */
public interface OnControlPointTaskCompleted {
   // void onTaskCompleted(ArrayList<StreetGame> strings);
   // void onQuestionReturned(Question question);
    void onControlPointReturned(ControlPoint controlPoint);
}
