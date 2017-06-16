package com.logiforge.tenniscloud.activities.facility;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.model.Facility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by iorlanov on 6/13/17.
 */

public class FacilityLookupFragment extends Fragment {
    public interface OnLookupFacilitySelectedListener {
        void onLookupFacilitySelected(Facility facility);
    }

    FacilityListAdapter listAdapter;
    EditText nameEditText;

    public FacilityLookupFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dlg_matchfacility_frag_lookupfacilities, container, false);

        listAdapter = new FacilityListAdapter(getActivity());
        ListView listView = (ListView) rootView.findViewById(R.id.lookup_facility_list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Facility facility = (Facility) listAdapter.getItem(position);
                OnLookupFacilitySelectedListener listener =
                        (OnLookupFacilitySelectedListener)getParentFragment();
                listener.onLookupFacilitySelected(facility);
            }
        });

        nameEditText = (EditText) rootView.findViewById(R.id.edit_name);
        Button lookupBtn = (Button)rootView.findViewById(R.id.btn_lookup);
        lookupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.clear();
                String substring = nameEditText.getText().toString();
                if(substring.length() > 0) {
                    FacilityFacade facilityFacade = new FacilityFacade();
                    List<Facility> foundFacilities =
                            facilityFacade.getFacilitiesByLikeName(substring);
                    listAdapter.addAll(foundFacilities);
                } else {
                    listAdapter.clear();
                }
                listAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    public static class FacilityListAdapter extends ArrayAdapter<Facility> {

        public FacilityListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1, new ArrayList<Facility>());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Facility facility = (Facility) this.getItem(position);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dlg_matchfacility_frag_playerfacilities_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(facility.getName());

            return convertView;
        }


    }
}
