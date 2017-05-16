package com.logiforge.tenniscloud.activities.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.logiforge.tenniscloud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 5/9/17.
 */

public class EmailListView extends LinearLayout
            implements View.OnClickListener {

    private static final String STATE_SUPER_CLASS = "SuperClassState";
    private static final String STATE_EMAILS = "Emails";
    private static final String BLANK_PLACEHOLDER = "BLANK_PLACEHOLDER";

    LayoutInflater inflater;

    public EmailListView(Context context) {
        super(context);
        initViews(context);
    }

    public EmailListView(Context context, AttributeSet atts) {
        super(context, atts);
        initViews(context);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState());

        StringBuilder emails = new StringBuilder();
        List<String> emailList = getEmails();
        for(String email : emailList) {
            if(email.length() == 0) {
                emails.append(BLANK_PLACEHOLDER);
            } else {
                emails.append(email);
            }
            emails.append(';');
        }

        bundle.putString(STATE_EMAILS, emails.toString());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER_CLASS));

            String emails = bundle.getString(STATE_EMAILS);
            if(emails.length() > 0) {
                String[] emailArray = emails.split(";");
                for(String email : emailArray) {
                    LinearLayout emailView = (LinearLayout) inflater.inflate(R.layout.item_editable_email, null);
                    this.addView(emailView);

                    if(!email.equals(BLANK_PLACEHOLDER)) {
                        EditText editTextEmail = (EditText) emailView.findViewById(R.id.edit_email);
                        editTextEmail.setText(email);
                    }
                }
            }
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
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add) {
            LinearLayout emailView = (LinearLayout) inflater.inflate(R.layout.item_editable_email, null);
            this.addView(emailView);
        } else if(v.getId() == R.id.btn_remove) {
            ViewGroup parent = (ViewGroup)v.getParent();
            this.removeView(parent);
        }
    }

    private void initViews(Context context) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_email_list, this);

        Button addBtn = (Button)findViewById(R.id.btn_add);
        addBtn.setOnClickListener(this);
    }

    public void initItems(List<String> emails) {
        int childCount = getChildCount();
        for(int i=1; i<childCount; i++) {
            this.removeView(getChildAt(1));
        }

        for(String email : emails) {
            LinearLayout emailView = (LinearLayout) inflater.inflate(R.layout.item_editable_email, null);
            this.addView(emailView);

            EditText editTextEmail = (EditText)emailView.findViewById(R.id.edit_email);
            editTextEmail.setText(email);

            Button removeBtn = (Button)emailView.findViewById(R.id.btn_remove);
            removeBtn.setOnClickListener(this);
        }
    }

    public List<String> getEmails() {
        List<String> emails = new ArrayList<String>();
        for(int i=1; i<getChildCount(); i++) {
            View itemView = getChildAt(i);
            EditText editText = (EditText)itemView.findViewById(R.id.edit_email);
            if(editText != null) {
                emails.add(editText.getText().toString());
            }
        }

        return emails;
    }
}
