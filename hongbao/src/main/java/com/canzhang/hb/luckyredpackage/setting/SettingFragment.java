package com.canzhang.hb.luckyredpackage.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.canzhang.hb.R;
import com.canzhang.hb.luckyredpackage.config.ConfigManger;
import com.canzhang.hb.luckyredpackage.utils.Logg;



public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private String childTag = SettingFragment.class.getSimpleName();
    private SharedPreferences preferences;
    private EditTextPreference timeEditTextPreference;
    private EditTextPreference timeClickEditTextPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings, rootKey);
        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

            timeEditTextPreference = findPreference("edit_time_preference");
            if (timeEditTextPreference != null) {
                timeEditTextPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                });

                timeEditTextPreference.setOnPreferenceChangeListener(this);
                timeEditTextPreference.setSummary(ConfigManger.getInstance().getOpenDelayTime() + " ms");
            }

            timeClickEditTextPreference = findPreference("edit_click_time_preference");
            if (timeClickEditTextPreference != null) {
                timeClickEditTextPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                });

                timeClickEditTextPreference.setOnPreferenceChangeListener(this);
                timeClickEditTextPreference.setSummary(ConfigManger.getInstance().getClickDelayTime() + " ms");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        Logg.i(childTag, "change key is " + key + " value " + newValue);
        if ("edit_time_preference".equals(key)) {

            if (timeEditTextPreference != null) {
                timeEditTextPreference.setSummary(newValue + " ms");
            }
            return true;
        } else if ("edit_click_time_preference".equals(key)) {
            if (timeEditTextPreference != null) {
                timeClickEditTextPreference.setSummary(newValue + " ms");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (timeEditTextPreference != null) {
            timeEditTextPreference.setSummary(ConfigManger.getInstance().getOpenDelayTime() + " ms");
        }

        if (timeClickEditTextPreference != null) {
            timeClickEditTextPreference.setSummary(ConfigManger.getInstance().getClickDelayTime() + " ms");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
