package com.ccs.fnae;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConfig {
    private static final String PREF_NAME = "com.ccs.fnae";
    private static final String KEY_BYTE_VALUE = "numberOfNight";

    // Сохранение значения типа byte
    public static void saveByte(Context context, byte value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_BYTE_VALUE, value);
        editor.apply();
    }

    // Получение значения типа byte
    public static byte getByte(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int intValue = sharedPreferences.getInt(KEY_BYTE_VALUE, 0); // Значение по умолчанию можно изменить по вашему усмотрению
        return (byte) intValue;
    }
}
