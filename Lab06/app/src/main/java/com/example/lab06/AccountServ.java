package com.example.lab06;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountServ extends Service {
    public AccountServ() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Lab06Authenticator auth = new Lab06Authenticator(this);
        return auth.getIBinder();
    }
}