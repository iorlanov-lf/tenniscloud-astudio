package com.logiforge.tenniscloud.activities.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.model.util.Phone;
import com.logiforge.tenniscloud.model.util.PhoneType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 5/9/17.
 */

public class PhoneListView extends LinearLayout
            implements View.OnClickListener {

    private static final String STATE_SUPER_CLASS = "SuperClassState";
    private static final String STATE_PHONES = "Phones";
    private static final String BLANK_PLACEHOLDER = "BLANK_PLACEHOLDER";

    LayoutInflater inflater;

    Button addBtn;

    public PhoneListView(Context context) {
        super(context);
        initViews(context);
    }

    public PhoneListView(Context context, AttributeSet atts) {
        super(context, atts);
        initViews(context);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState());

        StringBuilder serializedPhones = new StringBuilder();
        List<Phone> phones = getPhones();
        for(Phone phone : phones) {
            if(phone.number.length() == 0) {
                serializedPhones.append(BLANK_PLACEHOLDER);
            } else {
                serializedPhones.append(phone.number);
            }
            serializedPhones.append("|");
            serializedPhones.append(phone.type.toString());
            serializedPhones.append(';');
        }

        bundle.putString(STATE_PHONES, serializedPhones.toString());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER_CLASS));

            String serializedPhoneList = bundle.getString(STATE_PHONES);
            if(serializedPhoneList.length() > 0) {
                String[] serializedPhones = serializedPhoneList.split(";");
                if(serializedPhones.length != 0) {
                    ArrayList<Phone> phones = new ArrayList<Phone>();
                    for(String serializedPhone : serializedPhones) {
                        String[] serializedPhoneAttributes = serializedPhone.split("\\|");
                        phones.add(new Phone(
                                serializedPhoneAttributes[0],
                                Integer.parseInt(serializedPhoneAttributes[1])));
                    }
                    initItems(phones);
                }
            }

            setEnabled(isEnabled());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);

        if(isEnabled) {
            addBtn.setVisibility(View.VISIBLE);
        } else {
            addBtn.setVisibility(View.GONE);
        }

        for(int i=1; i<getChildCount(); i++) {
            View itemView = getChildAt(i);
            EditText editText = (EditText)itemView.findViewById(R.id.edit_phone);
            editText.setEnabled(isEnabled);
            Spinner spinner = (Spinner)itemView.findViewById(R.id.spnr_phone_type);
            spinner.setEnabled(isEnabled);
            Button removeBtn = (Button)itemView.findViewById(R.id.btn_remove);
            if(isEnabled) {
                removeBtn.setVisibility(View.VISIBLE);
            } else {
                removeBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add) {
            LinearLayout phoneView = (LinearLayout) inflater.inflate(R.layout.item_editable_phone, null);
            Spinner spinner = (Spinner)phoneView.findViewById(R.id.spnr_phone_type);
            ArrayAdapter<PhoneType> phoneTypeAdapter =
                    new ArrayAdapter<PhoneType>(getContext(), android.R.layout.simple_spinner_dropdown_item, PhoneType.values());
            spinner.setAdapter(phoneTypeAdapter);
            Button removeBtn = (Button)phoneView.findViewById(R.id.btn_remove);
            removeBtn.setOnClickListener(this);
            this.addView(phoneView);
            EditText phoneEditText = (EditText)phoneView.findViewById(R.id.edit_phone);
            phoneEditText.requestFocus();
        } else if(v.getId() == R.id.btn_remove) {
            ViewGroup parent = (ViewGroup)v.getParent();
            this.removeView(parent);
        }
    }

    private void initViews(Context context) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_phone_list, this);

        addBtn = (Button)findViewById(R.id.btn_add);
        addBtn.setOnClickListener(this);
    }

    public void initItems(List<Phone> phones) {
        int childCount = getChildCount();
        for(int i=1; i<childCount; i++) {
            this.removeView(getChildAt(1));
        }

        for(Phone phone : phones) {
            LinearLayout phoneView = (LinearLayout) inflater.inflate(R.layout.item_editable_phone, null);
            this.addView(phoneView);

            if(!phone.number.equals(BLANK_PLACEHOLDER)) {
                EditText editTextPhone = (EditText) phoneView.findViewById(R.id.edit_phone);
                editTextPhone.setText(phone.number);
            }

            Spinner spinner = (Spinner)phoneView.findViewById(R.id.spnr_phone_type);
            ArrayAdapter<PhoneType> phoneTypeAdapter =
                    new ArrayAdapter<PhoneType>(getContext(), android.R.layout.simple_spinner_dropdown_item, PhoneType.values());
            spinner.setAdapter(phoneTypeAdapter);
            PhoneType type = PhoneType.getById(phone.type);
            spinner.setSelection(type.ordinal());

            Button removeBtn = (Button)phoneView.findViewById(R.id.btn_remove);
            removeBtn.setOnClickListener(this);
        }
    }

    public List<Phone> getPhones() {
        List<Phone> phones = new ArrayList<Phone>();
        for(int i=1; i<getChildCount(); i++) {
            View itemView = getChildAt(i);
            EditText editText = (EditText)itemView.findViewById(R.id.edit_phone);
            Spinner spinner = (Spinner)itemView.findViewById(R.id.spnr_phone_type);
            PhoneType type = (PhoneType)spinner.getSelectedItem();
            phones.add(new Phone(editText.getText().toString(), type.getId()));
        }

        return phones;
    }

    public void setError(String msg) {
        TextView phoneLbl = (TextView)findViewById(R.id.txt_phone);
        phoneLbl.setError(msg);
    }
}
