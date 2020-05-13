package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;

public class AccountBalanceActivity extends AppCompatActivity {

    public static String SERVER_ADRESS = "http://141.223.83.40:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);

        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();
        final String userId = getIntent().getStringExtra("userId");

        EndorsementDB endorsementDB = realm.where(EndorsementDB.class).equalTo("userId", userId).findFirst();
        Call<BankAccount> m = MainActivity.RetrofitServiceImplFactory.serverPost().queryMyDigitalCheck(userId, endorsementDB.getStrEndorsement());


        Button btnSendToken = (Button) findViewById(R.id.btnSendToken);
        Button btnWithdraw = (Button) findViewById(R.id.btnWithdraw);
        Button btnCharge = (Button) findViewById(R.id.btnCharge);
        final EditText totalBalance = (EditText) findViewById(R.id.totalBalance);

        m.enqueue(new Callback<BankAccount>() {

            @Override
            public void onResponse(Call<BankAccount> call, Response<BankAccount> response) {

                if (response.isSuccessful()) {
                    Log.d("query my digital checks", "success");
                    final BankAccount body = response.body();
                    totalBalance.setText(valueOf(body.getDigitalCheck()));
                }
            }

            @Override
            public void onFailure(Call<BankAccount> call, Throwable t) {

            }
        });

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent issueuIntetent = new Intent(AccountBalanceActivity.this, IssueActivity.class);
                //final Intent mainIntetent = new Intent(LoginActivity.this, MainActivity.class);

                issueuIntetent.putExtra("userId", userId);
                //setContentView(R.layout.activity_issue);
                AccountBalanceActivity.this.startActivity(issueuIntetent);

            }
        });

        btnSendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent sendIntetent = new Intent(AccountBalanceActivity.this, SendActivity.class);
                //final Intent mainIntetent = new Intent(LoginActivity.this, MainActivity.class);

                sendIntetent.putExtra("userId", userId);
                //setContentView(R.layout.activity_send);
                startActivity(sendIntetent);

            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent redeemIntetent = new Intent(AccountBalanceActivity.this, RedeemActivity.class);
                //final Intent mainIntetent = new Intent(LoginActivity.this, MainActivity.class);

                redeemIntetent.putExtra("userId", userId);
                //setContentView(R.layout.activity_redeem);
                AccountBalanceActivity.this.startActivity(redeemIntetent);

            }
        });

    };

}
