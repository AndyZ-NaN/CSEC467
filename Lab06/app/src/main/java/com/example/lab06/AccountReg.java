package com.example.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AccountReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_reg);
    }

    public void register(View v) {
        // Add User Account based on user-input username & password
        EditText username = (EditText)findViewById(R.id.regEditUsername);
        EditText password = (EditText)findViewById(R.id.regEditPass);

        // Username DNE, accept registration
        if (!userExists(username.getText().toString())) {
            AccountManager am = (AccountManager)getSystemService(ACCOUNT_SERVICE);
            Account toBeAdded = new Account(username.getEditableText().toString(), "com.example.lab06.useracc");
            am.addAccountExplicitly(toBeAdded, password.getEditableText().toString(), null);

            // Create encryption key for the user
            generateKey(username.getText().toString());
        } else { // Username exists, reject registration
            Toast.makeText(getApplicationContext(), "Username already exists.", Toast.LENGTH_LONG).show();
        }
    }

    // Checks if the username already exists within the set of accounts
    private boolean userExists(String username) {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.example.lab06.useracc");
        for (Account account : accounts) {
            if (username.equals(account.name)) {
                return true;
            }
        }
        return false;
    }

    private void generateKey(String alias) {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();
            Log.d("key", aliases.toString());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(128)
                .build();
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            kg.init(keySpec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        kg.generateKey();

        try {
            SecretKey k = ((KeyStore.SecretKeyEntry) ks.getEntry(alias, null)).getSecretKey();
            Log.d("key", k.toString());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }

    }
}