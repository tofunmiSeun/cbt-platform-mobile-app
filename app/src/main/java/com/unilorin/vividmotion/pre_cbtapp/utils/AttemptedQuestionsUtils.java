package com.unilorin.vividmotion.pre_cbtapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tofunmi on 07/01/2017.
 */

public class AttemptedQuestionsUtils {

    private static String ATTEMPTED_QUESTIONS_KEY = "ATTEMPTED_QUESTIONS_KEY";
    private static String delimiter = "#";

    private Context myContext;

    public AttemptedQuestionsUtils(Context c){
        myContext = c;
    }

    public List<Long> getIdsOfAttemptedQuestions(){
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
        if (!sharedPreferences.contains(ATTEMPTED_QUESTIONS_KEY)){
            return new ArrayList<>();
        }

        String idListString = sharedPreferences.getString(ATTEMPTED_QUESTIONS_KEY, null);
        return getList(idListString);
    }

    public void addIds(List<Long> newList){
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!sharedPreferences.contains(ATTEMPTED_QUESTIONS_KEY)){
            editor.putString(ATTEMPTED_QUESTIONS_KEY, getStringFormat(newList));
            editor.apply();
        }

        List<Long> savedIds = getList(sharedPreferences.getString(ATTEMPTED_QUESTIONS_KEY, null));
        for (Long id : newList){
            if (!savedIds.contains(id)){
                savedIds.add(id);
            }
        }

        editor.putString(ATTEMPTED_QUESTIONS_KEY, getStringFormat(savedIds));
    }

    public void deleteAttemptedQuestionsList(){
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains(ATTEMPTED_QUESTIONS_KEY)) {
            editor.remove(ATTEMPTED_QUESTIONS_KEY);
        }
        editor.apply();
    }

    private static String getStringFormat(List<Long> list){
        String text = "";
        for (int i = 0; i < list.size() - 1; i++){
            text += list.get(i) + delimiter;
        }

        text += list.get(list.size()-1);
        return text;
    }

    private static List<Long> getList(String text){
        if (text.equals("")){
            return new ArrayList<>();
        }
        List<Long> longList = new ArrayList<>();
        for (String s : text.split(delimiter)){
            longList.add(Long.parseLong(s));
        }
        return longList;
    }

    public static String getInClauseStringFormat(List<Long> listOfIds){

        if (listOfIds.isEmpty()) {
            return "(-1)";
        }

        String text = "";
        text += "( ";
        for (int i = 0; i < listOfIds.size() - 1; i++){
            text += String.valueOf(listOfIds.get(i)) + ",";
        }

        text += listOfIds.get(listOfIds.size() - 1) + " ) ";
        return text;
    }
}
