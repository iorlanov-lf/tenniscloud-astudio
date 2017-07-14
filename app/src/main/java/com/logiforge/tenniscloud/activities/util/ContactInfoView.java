package com.logiforge.tenniscloud.activities.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.model.util.Phone;

import java.util.List;

/**
 * Created by iorlanov on 6/7/17.
 */

public class ContactInfoView extends LinearLayout {
    LayoutInflater inflater;

    TextView nameLabelTextView;
    EditText nameEditText;
    EmailListView emailView;
    PhoneListView phoneView;

    public ContactInfoView(Context context) {
        super(context);
        initViews(context, null);
    }

    public ContactInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        CustomViewState customViewState = new CustomViewState(superState);
        customViewState.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(customViewState.childrenStates);
        }
        return customViewState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        CustomViewState customViewState = (CustomViewState) state;
        super.onRestoreInstanceState(customViewState.getSuperState());
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).restoreHierarchyState(customViewState.childrenStates);
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

        nameEditText.setEnabled(isEnabled);
        emailView.setEnabled(isEnabled);
        phoneView.setEnabled(isEnabled);
    }

    public void initName(String name) {
        nameEditText.setText(name);
    }

    public void initEmails(List<String> emails) {
        emailView.initItems(emails);
    }

    public void initPhones(List<Phone> phones) {
        phoneView.initItems(phones);
    }

    private void initViews(Context context, AttributeSet attrs) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_contact_info, this);

        String label = "Name";
        String hint = "Enter Name";

        if(attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.ContactInfoView, 0, 0);

            if (a.hasValue(R.styleable.ContactInfoView_nameLabel)) {
                label = a.getString(R.styleable.ContactInfoView_nameLabel);
            }

            if (a.hasValue(R.styleable.ContactInfoView_nameHint)) {
                hint = a.getString(R.styleable.ContactInfoView_nameHint);
            }
        }

        nameLabelTextView = (TextView)findViewById(R.id.txt_displayName);
        nameLabelTextView.setText(label);
        nameEditText = (EditText)findViewById(R.id.edit_displayName);
        nameEditText.setText(hint);

        emailView = (EmailListView)findViewById(R.id.view_emails);
        phoneView = (PhoneListView)findViewById(R.id.view_phones);
    }

    public String getName() {
        return nameEditText.getText().toString();
    }

    public List<String> getEmails() {
        return emailView.getEmails();
    }

    public List<Phone> getPhones() {
        return phoneView.getPhones();
    }

    public void setNameError(String msg) {
        nameEditText.setError(msg);
        nameEditText.requestFocus();
    }

    public void setEmailError(String msg) {
        emailView.setError(msg);
    }

    public void setPhoneError(String msg) {
        phoneView.setError(msg);
    }
}
