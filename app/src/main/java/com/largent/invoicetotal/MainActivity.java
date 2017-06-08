package com.largent.invoicetotal;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnEditorActionListener{

    private static final String TAG = "MainActivity";

    //define our widget variables
    private EditText subTotalET;
    private TextView percentTV;
    private TextView amountTV;
    private TextView totalTV;
    private Button resetButton;

    //comment

    //define instance variable
    private String subtotalString = "";

    private SharedPreferences savedValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //get reference to the widgets
        subTotalET = (EditText) findViewById(R.id.subTotalET);
        percentTV = (TextView) findViewById(R.id.percentTV);
        amountTV = (TextView) findViewById(R.id.amountTV);
        totalTV = (TextView) findViewById(R.id.totalTV);
        resetButton = (Button) findViewById(R.id.resetButton);

        //set the listener for the event
        subTotalET.setOnEditorActionListener(this);
        resetButton.setOnClickListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    public void onClick(View view){

        switch(view.getId()) {
            case R.id.resetButton:
                subTotalET.setText("");
                percentTV.setText("00%");
                amountTV.setText("$0.00");
                totalTV.setText("$0.00");

                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }

        Toast.makeText(getApplicationContext(), "Action ID:" + actionId, Toast.LENGTH_LONG).show();

        //hide soft keyboard
        return false;
    }

    private void calculateAndDisplay() {

        //get subtotal from user
        subtotalString = subTotalET.getText().toString();
        float subtotal;
        if(subtotalString.equals("")){
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalString);
        }

        //get discount percent
        float discountPercent = 0;
        if(subtotal >= 200){
            discountPercent = .2f;
        }
        else if (subtotal >= 100){
            discountPercent = .1f;
        }
        else
        {
            discountPercent = 0;
        }

        //calculate discount

        float discountAmount = subtotal * discountPercent;
        float total = subtotal-discountAmount;

        // format and display

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTV.setText(percent.format(discountPercent));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        amountTV.setText(currency.format(discountAmount));
        totalTV.setText(currency.format(total));

    }

/*    protected void onPause(){

        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.commit();

        super.onPause();
    }

    protected void onResume() {
        super.onResume();

        subtotalString = savedValues.getString("subtotalString", "");
        subTotalET.setText(subtotalString);
        calculateAndDisplay();
    }*/
}
