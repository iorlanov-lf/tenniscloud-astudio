package com.logiforge.tenniscloud.activities.dashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.logiforge.tenniscloud.R;

/**
 * Created by iorlanov on 2/27/2017.
 */
public class MatchTypeDlg extends DialogFragment {
    public static final String DLG_TAG = "act_dashboard_dlg_add";

    public static MatchTypeDlg newInstance() {
        MatchTypeDlg frag = new MatchTypeDlg();
        return frag;
    }

    public MatchTypeDlg() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_dashboard_dlg_add, container);

        Button newLeagueMatchBtn = (Button)view.findViewById(R.id.new_league_match);
        newLeagueMatchBtn.setOnClickListener((View.OnClickListener)getActivity());

        Button newFriendlyMatchBtn = (Button)view.findViewById(R.id.new_friendly_match);
        newFriendlyMatchBtn.setOnClickListener((View.OnClickListener)getActivity());

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
