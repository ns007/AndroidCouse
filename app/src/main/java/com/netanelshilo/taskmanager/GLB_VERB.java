package com.netanelshilo.taskmanager;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by netanel shilo on 29/12/2015.
 */
public class GLB_VERB {

    public static List<MEMBER> MembersList = new ArrayList<MEMBER>();
    public static List<String> TeamUsers = new ArrayList<String>();
    public static String Glb_user;
    public static String managerUser;
    public static String teamName;
    public static boolean is_manager;
    public static boolean is_new;
    public static boolean is_firstSignUP = true;
    public static int UpdateTime;

}
