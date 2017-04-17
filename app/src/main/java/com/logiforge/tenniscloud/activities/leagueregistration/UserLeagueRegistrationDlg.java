package com.logiforge.tenniscloud.activities.leagueregistration;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.LeagueRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/12/2017.
 */
public class UserLeagueRegistrationDlg extends DialogFragment implements ListView.OnItemClickListener {

    public interface OnItemSelectedListener {
        void onItemSelected(LeagueRegistration registration);
    }

    public static final String DLG_TAG = "dlg_user_league_registration";

    List<LeagueRegistration> registrations;

    public UserLeagueRegistrationDlg() {

    }

    public static UserLeagueRegistrationDlg newInstance() {
        UserLeagueRegistrationDlg frag = new UserLeagueRegistrationDlg();

        /*
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args); */
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_userregistration, container);

        ListView listView = (ListView)view.findViewById(R.id.list_registrations);

        LeagueRegistrationFacade registrationFacade = new LeagueRegistrationFacade();
        registrations = registrationFacade.getRegistrations(true);
        List<String> items = new ArrayList<String>();
        for(LeagueRegistration reg : registrations) {
            items.add(reg.getProfile().getLeagueMetroArea().getProvider().getProviderName() + " - " + reg.getLeagueFlight().getLeague().getLeagueName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, items);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        Button newRegistrationBtn = (Button)view.findViewById(R.id.new_registration);
        newRegistrationBtn.setOnClickListener((View.OnClickListener)getActivity());

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = getActivity();
        if (activity instanceof OnItemSelectedListener) {
            LeagueRegistration registration = registrations.get(position);
            OnItemSelectedListener listener = (OnItemSelectedListener)activity;
            listener.onItemSelected(registration);
        }

        this.dismiss();
    }
}
