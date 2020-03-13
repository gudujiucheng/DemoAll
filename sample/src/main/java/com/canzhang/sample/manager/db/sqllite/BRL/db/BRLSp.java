package com.canzhang.sample.manager.db.sqllite.BRL.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.base.base.AppProxy;

public class BRLSp {
    private static final String SP_KEY = "CRL_LX_REPORT";
    private static SharedPreferences sSp;
    static final String ID = "id";

    public static SharedPreferences get() {
        if (sSp == null) {
            synchronized (BRLSp.class) {
                if (sSp == null) {
                    sSp = AppProxy.getInstance().getApplication().getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
                }
            }
        }
        return sSp;
    }

}
