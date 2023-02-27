package ru.nekitgam.githubusersapi.dynamic;

import android.util.Base64;

public class MetaDataClass {
    public String APP_PREFERENCES = "log_settings";
    /*
     * Функция шифрования строки в base64
     */
    public String encodeB64(String text) {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    /*
     * Функция дешифрования строки из base64
     */
    public String decodeB64(String code) {
        return new String(Base64.decode(code,Base64.DEFAULT));
    }
}
