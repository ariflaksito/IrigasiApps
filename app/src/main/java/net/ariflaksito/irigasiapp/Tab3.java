package net.ariflaksito.irigasiapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class Tab3 extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View rootView = inflater.inflate(R.layout.list_fragment, null);

        return rootView;
    }
}

