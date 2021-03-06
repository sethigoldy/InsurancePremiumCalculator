package com.avidprogrammers.insurancepremiumcalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//import static com.avidprogrammers.pp_goodsauto_private.R.id.pp_goodsauto_private_lpgtype_value;

/**
 * Created by Abhishek on 26-Mar-17.
 */

public class pp_goodsauto_private extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener {

    ConnectivityReceiver conn;

    CheckingStatus checkingStatus;

    private static final String TAG = "pp_goodsauto_private";
    private AdView mAdView;

    private TextView mDateDisplay;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    long diffInDays,diff_days;
    private int mDay;

    Button pp_goodsauto_private_btn;

    EditText pp_goodsauto_private_idv_value;
    EditText pp_goodsauto_private_date_value;
    EditText pp_goodsauto_private_cc_value;
    EditText pp_goodsauto_private_nd_value;
    EditText pp_goodsauto_private_uwd_value;
    EditText pp_goodsauto_private_nfpp;
    EditText pp_goodsauto_private_coolie;
    EditText pp_goodsauto_private_gvw_value;
    EditText pp_goodsauto_private_lpgtype_value;
    EditText pp_goodsauto_private_paod_value;

    RadioGroup pp_goodsauto_private_zone;
    RadioGroup pp_goodsauto_private_lpg;
    RadioGroup pp_goodsauto_private_lpgtype;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(conn);
    }


    static final int DATE_DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkingStatus=new CheckingStatus();
        conn=new ConnectivityReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(conn, intentFilter);
        checkfunction(pp_goodsauto_private.this);

        setContentView(R.layout.pp_goodsauto_private);
        getSupportActionBar().setTitle("Private Goods Auto Package Policy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        RadioButton pa_no =  findViewById(R.id.pp_goodsauto_private_paod_value_no);
        pa_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pp_goodsauto_private_paod_value.setText("0");
                pp_goodsauto_private_paod_value.setEnabled(false);
            }
        });
        RadioButton pa_yes = findViewById(R.id.pp_goodsauto_private_paod_value_yes);
        pa_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pp_goodsauto_private_paod_value.setText("320");
                pp_goodsauto_private_paod_value.setEnabled(true);
            }
        });


        //Date-start
        mDateDisplay = (TextView) findViewById(R.id.pp_goodsauto_private_date_value);
        mPickDate = (Button) findViewById(R.id.pp_goodsauto_private_date_btn);


        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();
        //Date-end


        //spinner-start
        Spinner spin = (Spinner) findViewById(R.id.pp_goodsauto_private_ncb_value);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,ncb);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        //spinner-end

        //to check if CNG is yes or no
        RadioButton cng_no = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpg_value_no);
        cng_no.setChecked(true);
        cng_no.setOnClickListener(no_cng);


        RadioButton cng_yes = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpg_value_yes);
        cng_yes.setChecked(false);
        cng_yes.setOnClickListener(yes_cng);


        //setting default value of cngtype to fixed
        RadioButton cng_inbuilt = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpgtype_value_inbuilt);
        cng_inbuilt.setChecked(false);

        //For N/d
        RadioButton nd_yes = (RadioButton) findViewById(R.id.pp_goodsauto_private_nd_value_yes);
        nd_yes.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText ed1=(EditText)findViewById(R.id.pp_goodsauto_private_nd_value);
                long diffInDays=CalculateDifferenceInDays();
                double nd_value1=0.00;
                if (diffInDays < 365) {
                    nd_value1=15;
                }else if (diffInDays >= 365 ) {
                    if(diffInDays < 1825){
                        nd_value1 = 25;
                    }
                    else if (diffInDays >= 1825 ){
                        nd_value1 = 0;
                    }
                }
                int  nd_value1_int =(int) nd_value1;
                ed1.setText(String.valueOf(nd_value1_int));

                ed1.setEnabled(false);
                ed1.setVisibility(View.VISIBLE);
                TextView nd_sym= (TextView)findViewById(R.id.percent_sym);
                nd_sym.setVisibility(View.VISIBLE);
            }
        });
        RadioButton nd_no = (RadioButton) findViewById(R.id.pp_goodsauto_private_nd_value_no);
        nd_no.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText ed1 = (EditText) findViewById(R.id.pp_goodsauto_private_nd_value);
                ed1.setEnabled(false);
                ed1.setText("0");
            }
        });

        //Find diff in days which will be used to calculate Idv using DOP in display
        diff_days = CalculateDifferenceInDays();

        findViewById(R.id.pp_goodsauto_private_btn).setOnClickListener(listener_pp_goodsauto_private_btn);


        //start-passthevalues
        //Get the ids of view objects
        findAllViewsId();

        pp_goodsauto_private_btn.setOnClickListener(this);
        //stop-passthevalues
    };


    View.OnClickListener listener_pp_goodsauto_private_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(pp_goodsauto_private.this, ppdisplay_goodsauto_private.class);
            startActivity(intent);
        }
    };


    //listener if cng is yes
    View.OnClickListener yes_cng = new View.OnClickListener(){
        public void onClick(View v){
            RadioButton rb1 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpgtype_value_fixed);
            RadioButton rb2 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpgtype_value_inbuilt);
            rb1.setEnabled(true);
            rb2.setEnabled(true);
            EditText ed1 = (EditText) findViewById(R.id.pp_goodsauto_private_lpgtype_value);
            ed1.setVisibility(View.VISIBLE);
            TextView lpg_sym= (TextView)findViewById(R.id.r_sym);
            lpg_sym.setVisibility(View.VISIBLE);
            ed1.setEnabled(true);
            rb1.setChecked(true);
            rb2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    EditText ed1 = (EditText) findViewById(R.id.pp_goodsauto_private_lpgtype_value);
                    ed1.setVisibility(View.INVISIBLE);
                    TextView lpg_sym= (TextView)findViewById(R.id.r_sym);
                    lpg_sym.setVisibility(View.INVISIBLE);
                }});
            rb1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    EditText ed1 = (EditText) findViewById(R.id.pp_goodsauto_private_lpgtype_value);
                    ed1.setVisibility(View.VISIBLE);
                    TextView lpg_sym= (TextView)findViewById(R.id.r_sym);
                    lpg_sym.setVisibility(View.VISIBLE);
                    ed1.setEnabled(true);

                }});

        }
    };

    //listener if cng is no
    View.OnClickListener no_cng = new View.OnClickListener(){
        public void onClick(View v) {

            RadioButton rb1 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpgtype_value_fixed);
            RadioButton rb2 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpgtype_value_inbuilt);
            rb1.setEnabled(false);
            rb2.setEnabled(false);
            EditText ed1=(EditText)findViewById(R.id.pp_goodsauto_private_lpgtype_value);
            ed1.setEnabled(false);
            ed1.setVisibility(View.INVISIBLE);
            TextView lpg_sym= (TextView)findViewById(R.id.r_sym);
            lpg_sym.setVisibility(View.INVISIBLE);

        }
    };
    void nd_calculate_diff(){
        EditText ed1=(EditText)findViewById(R.id.pp_goodsauto_private_nd_value);
        long diffInDays=CalculateDifferenceInDays();
        double nd_value1=0.00;
        if (diffInDays < 365) {
            nd_value1=15;
        }else if (diffInDays >= 365 ) {
            if(diffInDays < 1825){
                nd_value1 = 25;
            }
            else if (diffInDays >= 1825 ){
                nd_value1 = 0;
            }
        }
        int  nd_value1_int =(int) nd_value1;
        ed1.setText(String.valueOf(nd_value1_int));

        ed1.setEnabled(false);
        ed1.setVisibility(View.VISIBLE);
        TextView nd_sym= (TextView)findViewById(R.id.percent_sym);
        nd_sym.setVisibility(View.VISIBLE);
    }

    //start-passthevalues
    private void findAllViewsId() {
        pp_goodsauto_private_btn = (Button) findViewById(R.id.pp_goodsauto_private_btn);

        pp_goodsauto_private_idv_value = (EditText) findViewById(R.id.pp_goodsauto_private_idv_value);
        pp_goodsauto_private_date_value = (EditText) findViewById(R.id.pp_goodsauto_private_date_value);
        pp_goodsauto_private_cc_value = (EditText) findViewById(R.id.pp_goodsauto_private_cc_value);
        pp_goodsauto_private_nd_value = (EditText) findViewById(R.id.pp_goodsauto_private_nd_value);
        pp_goodsauto_private_uwd_value = (EditText) findViewById(R.id.pp_goodsauto_private_uwd_value);
        pp_goodsauto_private_nfpp = (EditText) findViewById(R.id.pp_goodsauto_private_nfpp);
        pp_goodsauto_private_coolie = (EditText) findViewById(R.id.pp_goodsauto_private_coolie);
        pp_goodsauto_private_paod_value = findViewById(R.id.pp_goodsauto_private_paod);

        final ScrollView scrollview = ((ScrollView) findViewById(R.id.pp_goodsauto_private_sv));

        pp_goodsauto_private_uwd_value.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if(action == EditorInfo.IME_ACTION_DONE)
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.scrollTo(0, scrollview.getBottom());
                            pp_goodsauto_private_nfpp.requestFocus();
                        }
                    });
                return false;
            }
        });

        pp_goodsauto_private_coolie.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if(action == EditorInfo.IME_ACTION_DONE)
                    scrollview.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollview.scrollTo(0, scrollview.getBottom());
                            pp_goodsauto_private_btn.requestFocus();
                        }
                    });
                return false;
            }
        });

        pp_goodsauto_private_gvw_value = (EditText) findViewById(R.id.pp_goodsauto_private_gvw_value);
        pp_goodsauto_private_lpgtype_value = (EditText)findViewById(R.id.pp_goodsauto_private_lpgtype_value);
        pp_goodsauto_private_zone = (RadioGroup) findViewById(R.id.pp_goodsauto_private_zone);
        pp_goodsauto_private_lpg = (RadioGroup) findViewById(R.id.pp_goodsauto_private_lpg);
        pp_goodsauto_private_lpgtype = (RadioGroup) findViewById(R.id.pp_goodsauto_private_lpgtype);


    }

    public long  CalculateDifferenceInDays(){

        int mYear_now,mMonth_now,mDay_now;
        // Create Calendar instance
        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        java.util.Calendar calendar2 = java.util.Calendar.getInstance();
        mYear_now = calendar1.get(java.util.Calendar.YEAR);
        mMonth_now = calendar1.get(java.util.Calendar.MONTH);
        mDay_now = calendar1.get(java.util.Calendar.DAY_OF_MONTH);

        // Set the values for the calendar fields YEAR, MONTH, and DAY_OF_MONTH.
        calendar2.set(mYear, mMonth, mDay);
        calendar1.set(mYear_now, mMonth_now, mDay_now);

            /*
            * Use getTimeInMillis() method to get the Calendar's time value in
            * milliseconds. This method returns the current time as UTC
            * milliseconds from the epoch
            */
        long miliSecondForDate1 = calendar1.getTimeInMillis();
        long miliSecondForDate2 = calendar2.getTimeInMillis();

        // Calculate the difference in millisecond between two dates
        long diffInMilis = miliSecondForDate1 - miliSecondForDate2;

             /*
              * Now we have difference between two date in form of millsecond we can
              * easily convert it Minute / Hour / Days by dividing the difference
              * with appropriate value. 1 Second : 1000 milisecond 1 Hour : 60 * 1000
              * millisecond 1 Day : 24 * 60 * 1000 milisecond
              */

        long diffInSecond = diffInMilis / 1000;
        long diffInMinute = diffInMilis / (60 * 1000);
        long diffInHour = diffInMilis / (60 * 60 * 1000);
        diffInDays = diffInMilis / ( 24 * 60 * 60 * 1000);

        return diffInDays;


    }

    //Validation function
    int validation(){
        if(pp_goodsauto_private_idv_value.getText().toString().isEmpty() || pp_goodsauto_private_nd_value.getText().toString().isEmpty()
                || pp_goodsauto_private_uwd_value.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter All Fields First",Toast.LENGTH_SHORT).show();
            return 0;
        }
        RadioButton radioButton1 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpg_value_yes);
        RadioButton radioButton2 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpgtype_value_fixed);
        if(radioButton1.isChecked() && radioButton2.isChecked()){
            if(pp_goodsauto_private_lpgtype_value.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Enter The Fixed Value Of CNG First",Toast.LENGTH_SHORT).show();
                return 0;
            }

        }
        if(pp_goodsauto_private_nfpp.getText().toString().isEmpty() || pp_goodsauto_private_coolie.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter NFPP or Coolie First ",Toast.LENGTH_SHORT).show();
            return 0;
        }


        return 1;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ppdisplay_goodsauto_private.class);
        //Create a bundle object
        int valid = validation();
        if(valid==1) {
            Bundle b = new Bundle();

            //Inserts a String value into the mapping of this Bundle
            b.putString("pp_goodsauto_private_idv_value", pp_goodsauto_private_idv_value.getText().toString());
            b.putString("pp_goodsauto_private_date_value", pp_goodsauto_private_date_value.getText().toString());
            b.putString("pp_goodsauto_private_cc_value", pp_goodsauto_private_cc_value.getText().toString());
            b.putString("pp_goodsauto_private_nd_value", pp_goodsauto_private_nd_value.getText().toString());
            b.putString("pp_goodsauto_private_uwd_value", pp_goodsauto_private_uwd_value.getText().toString());
            b.putString("pp_goodsauto_private_nfpp", pp_goodsauto_private_nfpp.getText().toString());
            b.putString("pp_goodsauto_private_coolie", pp_goodsauto_private_coolie.getText().toString());
            b.putString("pp_goodsauto_private_paod_value", pp_goodsauto_private_paod_value.getText().toString());
            b.putString("pp_goodsauto_private_gvw_value", pp_goodsauto_private_gvw_value.getText().toString());


            //For changing the default value of cng type if NO is selected
            RadioButton r1 = (RadioButton) findViewById(R.id.pp_goodsauto_private_lpg_value_no);
            if (r1.isChecked()) {
                pp_goodsauto_private_lpgtype_value.setText("-");
            }
            b.putString("lpgtype_value", pp_goodsauto_private_lpgtype_value.getText().toString());

            //For NCB spinner
            Spinner spin = (Spinner) findViewById(R.id.pp_goodsauto_private_ncb_value);
            String spin_val = spin.getSelectedItem().toString();
            b.putString("pp_goodsauto_private_ncb_value", spin_val);

            //put diff in days
            b.putLong("diff_in_days", diff_days);

            int id1 = pp_goodsauto_private_zone.getCheckedRadioButtonId();
            RadioButton radioButton1 = (RadioButton) findViewById(id1);
            b.putString("pp_goodsauto_private_zone", radioButton1.getText().toString());


            int id2 = pp_goodsauto_private_lpg.getCheckedRadioButtonId();
            RadioButton radioButton2 = (RadioButton) findViewById(id2);
            b.putString("pp_goodsauto_private_lpg", radioButton2.getText().toString());


            int id3 = pp_goodsauto_private_lpgtype.getCheckedRadioButtonId();
            RadioButton radioButton3 = (RadioButton) findViewById(id3);
            b.putString("pp_goodsauto_private_lpgtype", radioButton3.getText().toString());

            //refresh();
            //Add the bundle to the intent.
            intent.putExtras(b);

            //start the DisplayActivity
            startActivity(intent);
        }
    }
    //stop-passthevalues


    //refresh function to reset all the fields once calculate button is clicked

    void refresh()
    {
        pp_goodsauto_private_idv_value.setText("");

        final Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH);
        mDay = c1.get(Calendar.DAY_OF_MONTH);

        updateDisplay();
        RadioButton r1 = (RadioButton)findViewById(R.id.pp_goodsauto_private_idv_value_C);
        r1.setChecked(true);

        pp_goodsauto_private_lpgtype_value.setText("");
        pp_goodsauto_private_uwd_value.setText("");
        pp_goodsauto_private_nfpp.setText("");
        pp_goodsauto_private_coolie.setText("");

    }

    //Date-start
    private void updateDisplay() {
        mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(" "));
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                    //to auto update Nil Depreciation when date changes(only when yes was selected in n/d)
                    RadioButton r1 = (RadioButton)findViewById(R.id.pp_goodsauto_private_nd_value_yes);
                    if(r1.isChecked())
                        nd_calculate_diff();
                }
            };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
    //Date-end


    //BackButton in title bar
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }




    //Spinner
    String[] ncb={"0","20","25","35","45","50"};

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), ncb[position], Toast.LENGTH_LONG);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub

    }

    public void checkfunction(Context context){
        boolean isConnected=ConnectivityReceiver.isConnected();
        checkingStatus.notification(isConnected,context);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkingStatus.notification(isConnected,this);
    }

}