package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.util.AvailabilityView;
import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade;
import com.logiforge.tenniscloud.model.MatchAvailability;

/**
 * Created by iorlanov on 7/6/17.
 */

public class AvailabilityDlg extends DialogFragment implements View.OnClickListener {
    public interface OnAvailabilityActionListener {
        void onDeleteAvailability(String availabilityId);
        void onAddAvailability(MatchAvailability availability);
        void onUpdateAvailability(MatchAvailability availability);
    }

    public static final String DLG_TAG = "dlg_availability";
    static final String ARG_EDITED_AVAILABILITY_ID = "ARG_EDITED_AVAILABILITY_ID";

    public static AvailabilityDlg newInstance(MatchAvailability editedAvailability) {
        AvailabilityDlg dlg = new AvailabilityDlg();

        Bundle args = new Bundle();
        args.putString(ARG_EDITED_AVAILABILITY_ID, editedAvailability==null?null:editedAvailability.id);
        dlg.setArguments(args);
        dlg.editedAvailability = editedAvailability;

        return dlg;
    }

    String editedAvailabilityId;
    MatchAvailability editedAvailability;
    AvailabilityView availabilityView;

    public AvailabilityDlg() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_availability, container);

        availabilityView = (AvailabilityView)view.findViewById(R.id.view_availability);
        if(editedAvailability != null) {
            availabilityView.initFromAvailability(editedAvailability);
        }

        Button deleteBtn = (Button)view.findViewById(R.id.btn_delete);
        if(editedAvailabilityId == null) {
            deleteBtn.setVisibility(View.GONE);
        } else {
            deleteBtn.setOnClickListener(this);
        }

        Button cancelBtn = (Button)view.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        Button saveBtn = (Button)view.findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(this);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editedAvailabilityId = getArguments().getString(ARG_EDITED_AVAILABILITY_ID);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_cancel) {
            dismiss();
        } else if(v.getId() == R.id.btn_delete) {
            Activity activity = getActivity();
            if(activity instanceof OnAvailabilityActionListener) {
                ((OnAvailabilityActionListener)activity).onDeleteAvailability(editedAvailabilityId);
                dismiss();
            } else {
                Toast.makeText(activity, "Not implemented", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == R.id.btn_save) {
            if(availabilityView.validateForm()) {
                Activity activity = getActivity();
                if (activity instanceof OnAvailabilityActionListener) {
                    MatchAvailabilityFacade availabilityFacade = new MatchAvailabilityFacade();
                    MatchAvailability availability = availabilityFacade.createMatchAvailablity(editedAvailabilityId);

                    availabilityView.populateAvailability(availability);

                    if (availability.syncState == DbDynamicTable.SYNC_STATE_TRANSIENT) {
                        ((OnAvailabilityActionListener) activity).onAddAvailability(availability);
                    } else {
                        ((OnAvailabilityActionListener) activity).onUpdateAvailability(availability);
                    }

                    dismiss();
                } else {
                    Toast.makeText(activity, "Not implemented", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
