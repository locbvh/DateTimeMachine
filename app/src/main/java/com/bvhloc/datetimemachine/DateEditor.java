package com.bvhloc.datetimemachine;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateEditor extends EditText implements View.OnClickListener {
    private String dateFormat;
    private String saveButton;
    private String cancelButton;
    private OnSaveListener onSaveListener;
    private OnCancelListener onCancelListener;

    private long maxDate;

    private Context mContext;
    private SimpleDateFormat simpleDateFormat;

    public DateEditor(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("SimpleDateFormat")
    public DateEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        String hint = null;
        if (attrs != null) {

            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DateEditor);

            dateFormat = array.getString(R.styleable.DateEditor_dateFormat);
            saveButton = array.getString(R.styleable.DateEditor_saveButton);
            cancelButton = array.getString(R.styleable.DateEditor_cancelButton);
            hint = array.getString(R.styleable.DateEditor_android_hint);

            array.recycle();
        }
        if (hint == null) {
            this.setText("Date");
        }

        if (dateFormat != null) {
            simpleDateFormat = new SimpleDateFormat(dateFormat);
        } else {
            simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        }

        this.setOnClickListener(this);
        this.setFocusable(false);
        this.setCursorVisible(false);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {

        final DatePicker datePicker = new DatePicker(mContext);
        datePicker.setCalendarViewShown(false);
        if (maxDate != 0) {
            datePicker.setMaxDate(maxDate);
        }

        if (!this.getText().toString().equals("")) {

            try {
                Date date = simpleDateFormat.parse(this.getText().toString());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                datePicker.updateDate(year, month, day);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            datePicker.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        String saveText = saveButton != null ? saveButton : "Save";
        String cancelText = cancelButton != null ? cancelButton : "Cancel";
        new AlertDialog.Builder(this.getContext())
                .setView(datePicker)
                .setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onCancelListener != null) {
                            onCancelListener.onCancel(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        }
                    }
                })
                .setPositiveButton(saveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        Date date = cal.getTime();
                        DateEditor.this.setText(simpleDateFormat.format(date));

                        if (onSaveListener != null) {
                            onSaveListener.onSave(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        }
                    }
                })
                .show();
    }

    public void setDate(Calendar date){
        String text = new DatetimeHelper().getString(date, simpleDateFormat);
        this.setText(text);
    }

    public Calendar getDate(){
        String text = this.getText().toString();
        return new DatetimeHelper().getCalendar(text, simpleDateFormat);
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public String getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(String saveButton) {
        this.saveButton = saveButton;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
    }


    public interface OnCancelListener{
        void onCancel(int year, int month, int day);
    }
    public interface OnSaveListener{
        void onSave(int year, int month, int day);
    }
    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
}
