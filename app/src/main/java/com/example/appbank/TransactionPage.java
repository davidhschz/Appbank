package com.example.appbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionPage extends AppCompatActivity {
    TextView balance, hour;
    EditText targetAccount, amount;
    Button logout, transfer, cancel, list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_page);
        balance = findViewById(R.id.tvbalance);
        hour = findViewById(R.id.tvhour);
        targetAccount = findViewById(R.id.ettargetaccount);
        amount = findViewById(R.id.etamount);
        logout = findViewById(R.id.btnlogout);
        transfer = findViewById(R.id.btntransfer);
        cancel = findViewById(R.id.btncancel);
        list = findViewById(R.id.btnlisttransactions);
        balance.setText(getIntent().getStringExtra("balance"));

        Date date = new Date();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        hour.setText(dateFormat.format(date));

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListTransactions.class);
                i.putExtra("accountClient", getIntent().getStringExtra("accountnumber"));
                startActivity(i);
            }
        });
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyTransfer();
            }
        });
    }

    private void verifyTransfer() {
        String url = "http://192.168.1.9/webServiceBank/transfer.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("0")) {
                            Toast.makeText(TransactionPage.this, "Verifique la cuenta destino", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("1")) {
                            Toast.makeText(TransactionPage.this, "Rectifique valor a enviar", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")){
                            Toast.makeText(TransactionPage.this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                        } else {
                            balance.setText(response);
                            Toast.makeText(TransactionPage.this, "Transacción realizada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TransactionPage.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("amount",amount.getText().toString().trim());
                        params.put("origin_account_number", getIntent().getStringExtra("accountnumber"));
                        params.put("target_account_number", targetAccount.getText().toString().trim());
                        return params;
                    }
                };
                RequestQueue requestQueue =  Volley.newRequestQueue(this);
                requestQueue.add(postRequest);
    }
}