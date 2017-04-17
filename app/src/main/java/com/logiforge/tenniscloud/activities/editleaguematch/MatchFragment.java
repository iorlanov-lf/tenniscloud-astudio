package com.logiforge.tenniscloud.activities.editleaguematch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class MatchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_editleaguematch_frag_match, container, false);
        return rootView;
    }
}
