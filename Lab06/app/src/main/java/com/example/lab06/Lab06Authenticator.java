package com.example.lab06;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

public class Lab06Authenticator extends AbstractAccountAuthenticator {
    public Lab06Authenticator(Context c) {
        super(c);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse resp, String s) {
        return null;
    }

    // Can be used to validate password complexity, etc
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse resp, String s, String s1, String[] strings, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse resp, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    // Get auth token to avoid having to grab plain text password from the SQLite file
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }


}
