package com.example.base.base;

import android.support.v4.app.Fragment;
import android.util.Log;

public class BaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();

        Log.e("Test",this.getClass().getSimpleName());
    }
}
