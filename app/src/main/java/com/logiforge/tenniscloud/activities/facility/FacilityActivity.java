package com.logiforge.tenniscloud.activities.facility;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.model.Installation;
import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.util.ItemPickerDialogFragment;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.db.FacilityTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.db.TCUserFacilityTbl;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.TCUserFacility;

import java.util.ArrayList;

public class FacilityActivity extends AppCompatActivity
        implements ItemPickerDialogFragment.OnItemSelectedListener {

    private static final String TAG = FacilityActivity.class.getSimpleName();

    public static Facility facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_facility);

        facility = null;
    }

    public void onSave(final View view) {
        boolean isValid = true;

        facility = new Facility();

        EditText nameEditText = (EditText)findViewById(R.id.edit_name);
        facility.setName(nameEditText.getText().toString());
        if(facility.getName().length() == 0) {
            nameEditText.setError("Required!");
            isValid = false;
        }

        EditText streetAddrEditText = (EditText)findViewById(R.id.edit_streetAddress);
        facility.setStreetAddress(streetAddrEditText.getText().toString());
        if(facility.getStreetAddress().length() == 0) {
            streetAddrEditText.setError("Required!");
            isValid = false;
        }

        EditText cityEditText = (EditText)findViewById(R.id.edit_city);
        facility.setCity(cityEditText.getText().toString());
        if(facility.getCity().length() == 0) {
            cityEditText.setError("Required!");
            isValid = false;
        }

        TextView stateTextView = (TextView)findViewById(R.id.txt_state);
        facility.setState(stateTextView.getText().toString());
        if(facility.getState().length() == 0 || facility.getState().equals("State")) {
            stateTextView.setError("Required!");
            isValid = false;
        }

        EditText zipEditText = (EditText)findViewById(R.id.edit_zip);
        facility.setZip(zipEditText.getText().toString());
        if(facility.getZip().length() == 0) {
            zipEditText.setError("Required!");
            isValid = false;
        }

        if(!isValid) {
            facility = null;
            return;
        } else {
            FacilityFacade facilityFacade = new FacilityFacade();
            facilityFacade.createFacility(facility, true);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    public void onCancel(final View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onSelectState(final View view) {
        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        pickerItems.add(new ItemPickerDialogFragment.Item("Alabama - AL", "AL"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Alaska - AK", "AK"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Arizona	- AZ", "AZ"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Arkansas - AR", "AR"));
        pickerItems.add(new ItemPickerDialogFragment.Item("California - CA", "CA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Colorado - CO", "CO"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Connecticut - CT", "CT"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Delaware - DE", "DE"));
        pickerItems.add(new ItemPickerDialogFragment.Item("District of Columbia - DC", "DC"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Florida - FL", "FL"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Georgia - GA", "GA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Hawaii - HI", "HI"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Idaho - ID", "ID"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Illinois - IL", "IL"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Indiana - IN", "IN"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Iowa - IA", "IA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Kansas - KS", "KS"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Kentucky - KY", "KY"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Louisiana - LA", "LA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Maine - ME", "ME"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Maryland - MD", "MD"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Massachusetts - MA", "MA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Michigan - MI", "MI"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Minnesota - MN", "MN"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Mississippi - MS", "MS"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Missouri - MO", "MO"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Montana - MT", "MT"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Nebraska - NE", "NE"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Nevada - NV", "NV"));
        pickerItems.add(new ItemPickerDialogFragment.Item("New Hampshire - NH", "NH"));
        pickerItems.add(new ItemPickerDialogFragment.Item("New Jersey - NJ", "NJ"));
        pickerItems.add(new ItemPickerDialogFragment.Item("New Mexico - NM", "NM"));
        pickerItems.add(new ItemPickerDialogFragment.Item("New York - NY", "NY"));
        pickerItems.add(new ItemPickerDialogFragment.Item("North Carolina - NC", "NC"));
        pickerItems.add(new ItemPickerDialogFragment.Item("North Dakota - ND", "ND"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Ohio - OH", "OH"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Oklahoma - OK", "OK"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Oregon - OR", "OR"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Pennsylvania - PA", "PA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Rhode Island - RI", "RI"));
        pickerItems.add(new ItemPickerDialogFragment.Item("South Carolina - SC", "SC"));
        pickerItems.add(new ItemPickerDialogFragment.Item("South Dakota - SD", "SD"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Tennessee - TN", "TN"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Texas - TX", "TX"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Utah - UT", "UT"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Vermont - VT", "VT"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Virginia - VA", "VA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Washington - WA", "WA"));
        pickerItems.add(new ItemPickerDialogFragment.Item("West Virginia - WV", "WV"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Wisconsin - WI", "WI"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Wyoming - WY", "WY"));

        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "States",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "StatePicker");
    }

    @Override
    public void onItemSelected(ItemPickerDialogFragment fragment, ItemPickerDialogFragment.Item item, int index) {
        if (fragment.getTag().equals("StatePicker")) {
            String selectedValue = item.getStringValue();
            TextView stateTextView = (TextView)findViewById(R.id.txt_state);
            stateTextView.setText(selectedValue);
            stateTextView.setError(null);
        }
    }
}
