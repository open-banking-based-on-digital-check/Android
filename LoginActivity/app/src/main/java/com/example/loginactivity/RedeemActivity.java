package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemActivity extends AppCompatActivity {

    public static String SERVER_ADRESS = "http://141.223.83.40:8080";

    String bank_tran_id = "djiflsfe"; //은행거래고유번호
    int cntr_account_num = 1234; //약정 계좌 번호(이용기관 소유의 계좌번호를 의미)
    int fintech_use_num = 1234; //출금계좌핀테크이용번호
    int tran_amt = 1234; //거래 금액
    // int tran_dtime = 1234; //거래일시
    // String req_client_name = "홍길동"; //요청고객성명

    //핀테크이용번호, Access token은 어떻게 전달?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        final Spinner spinner = (Spinner) findViewById(R.id.bankNameForRedeem);
        //final EditText bankNameText = (EditText) findViewById(R.id.bankName);
        final EditText accountNumberText = (EditText) findViewById(R.id.accountNumberForRedeem);
        final EditText balanceText = (EditText) findViewById(R.id.balanceText);

        Button btnRedeem = (Button) findViewById(R.id.btnRedeem);

        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        final String userId = intent.getExtras().getString("userId");

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Bank, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spinner.setAdapter(adapter);


        btnRedeem.setOnClickListener(new View.OnClickListener() {
            final Intent mainIntetent = new Intent(RedeemActivity.this, MenuActivity.class);


            @Override
            public void onClick(View v) {

                //final String cgha = chargeText.getText().toString();
                // final String bankName = bankNameText.getText().toString();
                final String bankName = spinner.getSelectedItem().toString();
                final String accountNumber = accountNumberText.getText().toString();
                final String amounts = balanceText.getText().toString();

                //final String account = accountNumber.getText().toString();

                EndorsementDB endorsementDB = realm.where(EndorsementDB.class).equalTo("userId", userId).findFirst();
                Log.d("Enrollment >>>>>>>> ", endorsementDB.getStrEndorsement() + " ################ " + bankName);
                Call<ResponseBody> m = MainActivity.RetrofitServiceImplFactory.serverPost().redeemToken(userId, endorsementDB.getStrEndorsement(), bankName, accountNumber, amounts);
                m.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "디지털 수표 출금 성공 ", Toast.LENGTH_SHORT).show();
                        }

                        mainIntetent.putExtra("userId", userId);
                        //setContentView(R.layout.activity_menu);
                        startActivity(mainIntetent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            ;
        });
    }
}
