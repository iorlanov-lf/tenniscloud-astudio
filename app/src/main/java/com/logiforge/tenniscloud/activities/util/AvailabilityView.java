package com.logiforge.tenniscloud.activities.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.LocalDateRange;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 7/7/17.
 */

public class AvailabilityView extends LinearLayout implements View.OnClickListener {
    private static final String STATE_SUPER_CLASS = "SuperClassState";
    private static final String STATE_FROM_DATE = "FromDate";
    private static final String STATE_TO_DATE = "ToDate";
    private static final String STATE_TIMES = "Times";
    private static final String BLANK_PLACEHOLDER = "BLANK_PLACEHOLDER";

    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final String TIME_FORMAT = "h:mm a";

    LayoutInflater inflater;

    EditText fromDtEditText;
    EditText toDtEditText;
    Button addTmRangeBtn;

    public AvailabilityView(Context context) {
        super(context);
        initViews(context, null);
    }

    public AvailabilityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState());

        bundle.putString(STATE_FROM_DATE, fromDtEditText.getText().toString());
        bundle.putString(STATE_TO_DATE, toDtEditText.getText().toString());

        StringBuilder times = new StringBuilder();
        List<String> timeList = getTimes();
        for(String time : timeList) {
            if(time.length() == 0) {
                times.append(BLANK_PLACEHOLDER);
            } else {
                times.append(time);
            }
            times.append(';');
        }

        bundle.putString(STATE_TIMES, times.toString());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER_CLASS));

            fromDtEditText.setText(bundle.getString(STATE_FROM_DATE));
            toDtEditText.setText(bundle.getString(STATE_TO_DATE));

            String times = bundle.getString(STATE_TIMES);
            if(times.length() > 0) {
                String[] timeArray = times.split(";");
                if(timeArray.length != 0) {
                    ArrayList<String> timeList = new ArrayList<String>();
                    for(String email : timeArray) {
                        timeList.add(email);
                    }
                    initTimes(timeList);
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
        if(v.getId() == addTmRangeBtn.getId()) {
            addTimeRange();
        } else if(v.getId() == R.id.btn_remove) {
            ViewGroup parent = (ViewGroup)v.getParent();
            this.removeView(parent);
        }
    }

    public void initFromAvailability(MatchAvailability availability) {
        fromDtEditText.setText(availability.getDateRange().getStartDt().toString(DATE_FORMAT));
        toDtEditText.setText(availability.getDateRange().getEndDt().toString(DATE_FORMAT));

        for(LocalTimeRange tmRange : availability.getTimeRanges()) {
            View tmRangeView = addTimeRange();
            EditText fromTmEditText = (EditText) tmRangeView.findViewById(R.id.edit_fromTm);
            fromTmEditText.setText(tmRange.getStartTm().toString(TIME_FORMAT));
            EditText toTmEditText = (EditText) tmRangeView.findViewById(R.id.edit_toTm);
            toTmEditText.setText(tmRange.getEndTm().toString(TIME_FORMAT));
        }
    }

    public void populateAvailability(MatchAvailability availability) {
        String fromDtAsString = fromDtEditText.getText().toString();
        String toDtAsString = toDtEditText.getText().toString();
        if(!fromDtAsString.isEmpty() && !toDtAsString.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
            LocalDate fromDt = LocalDate.parse(fromDtAsString, formatter);
            LocalDate toDt = LocalDate.parse(toDtAsString, formatter);
            availability.setDateRange(new LocalDateRange(fromDt, toDt));
        }

        List<String> timeStrings = getTimes();
        List<LocalTimeRange> timeRanges = new ArrayList<>();
        if(timeStrings.size() >= 2) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(TIME_FORMAT);
            for (int i = 0; i < timeStrings.size() - 1; i += 2) {
                String fromTmAsString = timeStrings.get(i);
                String toTmAsString = timeStrings.get(i+1);
                if(!fromTmAsString.isEmpty() && !toTmAsString.isEmpty()) {
                    LocalTime fromTm = LocalTime.parse(fromTmAsString, formatter);
                    LocalTime toTm = LocalTime.parse(toTmAsString, formatter);
                    timeRanges.add(new LocalTimeRange(fromTm, toTm));
                }
            }
        }
        availability.setTimeRanges(timeRanges);
    }

    private View addTimeRange() {
        View tmRangeView = inflater.inflate(R.layout.item_time_range, null);

        Button removeBtn = (Button)tmRangeView.findViewById(R.id.btn_remove);
        removeBtn.setOnClickListener(this);

        EditText fromTm = (EditText)tmRangeView.findViewById(R.id.edit_fromTm);
        fromTm.setOnTouchListener(new OnTimeFieldTouchListener(fromTm));

        EditText toTm = (EditText)tmRangeView.findViewById(R.id.edit_toTm);
        toTm.setOnTouchListener(new OnTimeFieldTouchListener(toTm));

        this.addView(tmRangeView);

        return tmRangeView;
    }

    private void initViews(Context context, AttributeSet attrs) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_availability, this);

        fromDtEditText = (EditText)findViewById(R.id.edit_fromDt);
        fromDtEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LocalDate now = LocalDate.now();
                    DatePickerDialog datePickerDlg =
                            new DatePickerDialog(getContext(), new OnFromDateSetListener(),
                                    now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth());
                    datePickerDlg.show();
                }
                return false;
            }
        });

        toDtEditText = (EditText)findViewById(R.id.edit_toDt);
        toDtEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LocalDate now = LocalDate.now();
                    DatePickerDialog datePickerDlg =
                            new DatePickerDialog(getContext(), new OnToDateSetListener(),
                                    now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth());
                    datePickerDlg.show();
                }
                return false;
            }
        });

        addTmRangeBtn = (Button)findViewById(R.id.btn_add_tmRange);
        addTmRangeBtn.setOnClickListener(this);
    }

    public void initTimes(List<String> times) {
        int childCount = getChildCount();
        for(int i=3; i<childCount; i++) {
            this.removeView(getChildAt(3));
        }

        for(int i=0; i<times.size()-1; i+=2) {
            String fromTm = times.get(i);
            String toTm = times.get(i+1);

            View tmRangeView = addTimeRange();

            if(!fromTm.equals(BLANK_PLACEHOLDER)) {
                EditText fromTmEditText = (EditText) tmRangeView.findViewById(R.id.edit_fromTm);
                fromTmEditText.setText(fromTm);
            }

            if(!toTm.equals(BLANK_PLACEHOLDER)) {
                EditText toTmEditText = (EditText) tmRangeView.findViewById(R.id.edit_toTm);
                toTmEditText.setText(toTm);
            }
        }
    }


    List<String> getTimes() {
        List<String> times = new ArrayList<String>();
        for(int i=3; i<getChildCount(); i++) {
            View childView = getChildAt(i);
            EditText fromTmEditText = (EditText)childView.findViewById(R.id.edit_fromTm);
            if(fromTmEditText != null) {
                times.add(fromTmEditText.getText().toString());
            }
            EditText toTmEditText = (EditText)childView.findViewById(R.id.edit_toTm);
            if(toTmEditText != null) {
                times.add(toTmEditText.getText().toString());
            }
        }

        return times;
    }

    class OnFromDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate deadline = new LocalDate(year, month+1, dayOfMonth);
            fromDtEditText.setText(deadline.toString(DATE_FORMAT));
        }
    }

    class OnToDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate deadline = new LocalDate(year, month+1, dayOfMonth);
            toDtEditText.setText(deadline.toString(DATE_FORMAT));
        }
    }

    class OnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        EditText editText;

        public OnTimeSetListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onTimeSet(TimePicker view, int hours, int minutes) {
            LocalTime schTime = new LocalTime(hours, minutes);
            editText.setText(schTime.toString(TIME_FORMAT));
            editText.setError(null);
        }
    }

    class OnTimeFieldTouchListener implements View.OnTouchListener {
        EditText editText;

        public OnTimeFieldTouchListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                LocalDate now = LocalDate.now();
                TimePickerDialog datePickerDlg =
                        new TimePickerDialog(getContext(), new OnTimeSetListener(editText),
                                19, 0, false);
                datePickerDlg.show();
            }
            return false;
        }
    }
}
