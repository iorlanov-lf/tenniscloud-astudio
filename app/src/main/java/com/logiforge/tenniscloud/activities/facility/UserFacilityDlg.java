package com.logiforge.tenniscloud.activities.facility;

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
import com.logiforge.tenniscloud.db.TCUserFacilityTbl;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.model.Facility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/12/2017.
 */
public class UserFacilityDlg extends DialogFragment implements ListView.OnItemClickListener {

    public interface OnItemSelectedListener {
        void onItemSelected(Facility facility);
    }

    public static final String DLG_TAG = "dlg_userfacility";

    List<Facility> facilities;

    public UserFacilityDlg() {

    }

    public static UserFacilityDlg newInstance() {
        UserFacilityDlg frag = new UserFacilityDlg();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_userfacility, container);

        ListView listView = (ListView)view.findViewById(R.id.list_facilities);

        FacilityFacade facilityFacade = new FacilityFacade();
        facilities = facilityFacade.getFacilities();
        List<String> items = new ArrayList<String>();
        for(Facility facility : facilities) {
            items.add(facility.getName() + ", " + facility.getCity() + " " + facility.getZip());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, items);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        Button newFacilityBtn = (Button)view.findViewById(R.id.new_facility);
        newFacilityBtn.setOnClickListener((View.OnClickListener)getActivity());

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
            Facility facility = facilities.get(position);
            OnItemSelectedListener listener = (OnItemSelectedListener)activity;
            listener.onItemSelected(facility);
        }

        this.dismiss();
    }
}
