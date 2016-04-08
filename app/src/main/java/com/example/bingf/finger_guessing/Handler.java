package com.example.bingf.finger_guessing;

import java.util.List;

/**
 * Created by karthur on 2016/3/31.
 */
public interface Handler {
    void onResult(boolean result);
    void onReceive(String userName,String msg);
    void onAllUserName(List<String> userNames);
}
