package com.example.appbank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListTransactions extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    RecyclerView listtransactions;
    ArrayList<Transaction> transactionList;
    RequestQueue rq;
    JsonRequest jrq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transactions);
        listtransactions = findViewById(R.id.rvlisttransactions);
        transactionList = new ArrayList<>();
        listtransactions.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        listtransactions.setHasFixedSize(true);
        rq = Volley.newRequestQueue(getApplicationContext());

        loadWebService();
    }

    private void loadWebService() {
        String url = "http://192.168.1.9/webServiceBank/listTransfer.php?account=" + getIntent().getStringExtra("accountClient");
        jrq = new JsonObjectRequest(Request.Method.POST,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onResponse(JSONObject response) {
        Transaction transaction = null;
        JSONArray json = response.optJSONArray("transaction");
        try {

            for (int i = 0; i < json.length(); i++){
                transaction = new Transaction();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                transaction.setTransactionNumber("Número de transacción: " + jsonObject.optString("transaction_number"));
                transaction.setTargetAccount("Cuenta destino: " + jsonObject.optString("target_account_number"));
                transaction.setOriginAccount("Cuenta origen: " + jsonObject.optString("origin_account_number"));
                transaction.setDate(jsonObject.optString("date"));
                transaction.setAmount("Valor: " + jsonObject.optString("amount"));
                transactionList.add(transaction);
            }
            TransactionAdapter adapter = new TransactionAdapter(transactionList);
            listtransactions.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
    }
}