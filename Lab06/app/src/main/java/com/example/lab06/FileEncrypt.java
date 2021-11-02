package com.example.lab06;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;

import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class FileEncrypt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_encrypt);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void encrypt(View v){
        try {
            EditText edtFilename = (EditText)findViewById(R.id.edtFile);
            EditText edtData = (EditText)findViewById(R.id.edtData);
            EditText edtAlias = (EditText)findViewById(R.id.edtAlias);

            String fileName = edtFilename.getText().toString();
            String plainText = edtData.getText().toString();
            String keyAlias = edtAlias.getText().toString();

            String path = getFilesDir().toString(); //data/data/com.example.lab06/../files

            byte[] iv;
            byte[] cipherText;


            // Fetch keystore service
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            // Check to see if alias is in the keystore

            if(!ks.containsAlias(keyAlias)) {
                generateKey(keyAlias);
            }

            SecretKey k = ((KeyStore.SecretKeyEntry) ks.getEntry(keyAlias, null)).getSecretKey();
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, k);
            iv = c.getIV();
            cipherText = c.doFinal(plainText.getBytes());

            //Write data to file
            FileOutputStream ctOut = new FileOutputStream((path + File.separator + fileName));
            for (Byte b : cipherText){
                ctOut.write(b);
            }
            ctOut.close();

            //Write iv to new file
            FileOutputStream ivOut = new FileOutputStream((path + File.separator + fileName + "_iv"));
            for (Byte b : iv){
                ivOut.write(b);
            }
            ivOut.close();

        } catch (UserNotAuthenticatedException e) {
            authenticate(v);
            encrypt(null);
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableEntryException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public void decrypt(View v) {
        try {
            EditText edtFilename = (EditText) findViewById(R.id.edtFile);
            EditText edtData = (EditText) findViewById(R.id.edtData);
            EditText edtAlias = (EditText) findViewById(R.id.edtAlias);

            String fileName = edtFilename.getText().toString();
            String keyAlias = edtAlias.getText().toString();

            String path = getFilesDir().toString(); //data/data/com.example.lab06/../files

            byte[] iv;
            byte[] cipherText;

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            if (!ks.containsAlias(keyAlias)) {
                Toast.makeText(this, "Key not found", Toast.LENGTH_LONG).show();
                return;
            }

            SecretKey k = ((KeyStore.SecretKeyEntry) ks.getEntry(keyAlias, null)).getSecretKey();

            File ivFile = new File(path + File.separator + fileName + "_iv");
            if (ivFile.exists()) {
                Path p = Paths.get(path + File.separator + fileName + "_iv");
                iv = Files.readAllBytes(p);
            } else {
                Toast.makeText(this, "IV missing, cannot decrypt", Toast.LENGTH_LONG).show();
                return;
            }

            GCMParameterSpec params = new GCMParameterSpec(128, iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, k, params);

            Path p = Paths.get(path + File.separator + fileName);
            cipherText = Files.readAllBytes(p);
            byte[] plainText = c.doFinal(cipherText);
            String readablePT = new String(plainText, StandardCharsets.UTF_8);
            edtData.setText(readablePT);
            Toast.makeText(this, readablePT, Toast.LENGTH_SHORT).show();

        } catch (UserNotAuthenticatedException e) {
            authenticate(v);
            decrypt(null);
        } catch (InvalidKeyException | UnrecoverableEntryException | KeyStoreException | NoSuchAlgorithmException | CertificateException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | IOException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void newKey(View v) {
        EditText edtAlias = (EditText) findViewById(R.id.edtAlias);
        try {
            generateKey(edtAlias.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void authenticate(View v){
        // Allows the OS to return to the app from the auth prompt
        Executor ex = ContextCompat.getMainExecutor(this);
        PromptInfo details = new PromptInfo.Builder()
                .setTitle("Lab06 Authenticate to App")
                .setSubtitle("Please provide your pin or password")
                //.setNegativeButtonText("Neg")
                .setAllowedAuthenticators(
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
                ).build();
        BiometricPrompt prompt = new BiometricPrompt(this, ex, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Auth Error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Auth Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Auth Failed", Toast.LENGTH_LONG).show();
            }
        });
        prompt.authenticate(details);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void generateKey(String alias) throws NoSuchAlgorithmException {
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
        } catch (CertificateException | IOException | KeyStoreException e) {
            e.printStackTrace();
        }
        KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationParameters(120, KeyProperties.AUTH_DEVICE_CREDENTIAL)
                .setKeySize(128)
                .build();
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
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
            Toast.makeText(getApplicationContext(), "Key found for alias: " + alias + " - " + k.toString(), Toast.LENGTH_LONG).show();
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