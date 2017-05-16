package com.logiforge.tenniscloud.activities.leagueregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.model.LeagueProfileEmail;

import java.util.List;

/**
 * Created by iorlanov on 5/8/17.
 */

public class EmailListAdapter extends BaseAdapter {
    List<LeagueProfileEmail> emails;
    LayoutInflater inflater;

    public EmailListAdapter(Context context, List<LeagueProfileEmail> emails) {
        this.emails = emails;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return emails.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = inflater.inflate(R.layout.item_editable_email, null);
        }

        EditText emailEditText = (EditText)rowView.findViewById(R.id.edit_email);
        LeagueProfileEmail email = emails.get(position);
        emailEditText.setText(email.getEmail());

        return rowView;
    }
}
