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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    TextView signup;
    EditText password, user;
    Button login;
    RequestQueue rq;
    JsonRequest jrq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup = findViewById(R.id.tvsignup);
        password = findViewById(R.id.etpassword);
        user = findViewById(R.id.etuser);
        login = findViewById(R.id.btnlogin);

        rq = Volley.newRequestQueue(getApplicationContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin();
            }
        });
    }

    private void validateLogin() {
        String url = "http://192.168.1.9/webServiceBank/search.php?user="+ user.getText().toString() + "&password=" + password.getText().toString();
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Client currentClient = new Client();
        Account currentAccount = new Account();

        JSONArray arrayAccount = response.optJSONArray("account");
        JSONObject jsonObjectAccount = null;
        JSONArray arrayClient = response.optJSONArray("client");
        JSONObject jsonObject = null;
        try {
            jsonObject = arrayClient.getJSONObject(0);
            currentClient.setUser(jsonObject.optString("user"));
            currentClient.setName(jsonObject.optString("name"));
            jsonObjectAccount = arrayAccount.getJSONObject(0);
            currentAccount.setAccountNumber(jsonObjectAccount.optString("accountnumber"));
            currentAccount.setBalance(jsonObjectAccount.optString("balance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent logToTransaction = new Intent(getApplicationContext(), TransactionPage.class);
        logToTransaction.putExtra("user", currentClient.getUser());
        logToTransaction.putExtra("name", currentClient.getName());
        logToTransaction.putExtra("accountnumber", currentAccount.getAccountNumber());
        logToTransaction.putExtra("balance", currentAccount.getBalance());
        startActivity(logToTransaction);
    }
}