package com.v5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PayPalDetails extends AppCompatActivity {

    TextView txtId, txtAmount, txtStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pay_pal_details );
        txtId = findViewById( R.id.txtId );
        txtAmount = findViewById( R.id.txtAmount);
        txtStatus = findViewById( R.id.txtStatus);

        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject( intent.getStringExtra( "PaymentDetails" ) );
            showDetails( jsonObject.getJSONObject( "response" ), intent.getStringExtra( "PaymentAmount" ) );
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {

            txtAmount.setText( "You payed "+ paymentAmount+"Kr."+ " to Ny Liv Spa");
            txtId.setText( response.getString( "id" ) );
            txtStatus.setText( response.getString( "state" ) );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
