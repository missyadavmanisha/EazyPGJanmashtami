package net.eazypg.eazypgjanmashtami;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

public class PhoneVerifyActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText otpEditText;

    Boolean counterIsActive = false;
    CountDownTimer countDownTimer;

    Context context;

    TextView timeTextView, changeNumber, autoVerify, phoneNumber;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;


    TextView otpMessageTextView;

    String name, contact, organisation, folk, round;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        autoVerify = findViewById(R.id.autoVerify);
        phoneNumber = findViewById(R.id.tenantPhoneNumber);

        otpEditText = findViewById(R.id.otpEditText);
        timeTextView = findViewById(R.id.timeTextView);

        context = PhoneVerifyActivity.this;

        otpMessageTextView = findViewById(R.id.otpMessageTextView);
        changeNumber = findViewById(R.id.textView30);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        contact = intent.getStringExtra("contact");
        organisation = intent.getStringExtra("organisation");
        folk = intent.getStringExtra("folk");
        round = intent.getStringExtra("round");

        phoneNumber.setText(contact+"");

        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PhoneVerifyActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        if (!counterIsActive) {

            counterIsActive = true;

            countDownTimer = new CountDownTimer(60 * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer((int) millisUntilFinished / 1000);

                }

                @Override
                public void onFinish() {

                    if (context == PhoneVerifyActivity.this) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(PhoneVerifyActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("OTP is not retrieved. Please check the number or Mobile Network.");
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(PhoneVerifyActivity.this, HomePageActivity.class));

                            }
                        });
                        builder.setCancelable(false);
                        builder.show();

                    }


                }
            }.start();

        }

        String message;
        message = "OTP is sent to " + contact + ".\n Wait for 60 seconds.";
        otpMessageTextView.setText(message);


        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();

                otpEditText.setText(code);
                autoVerify.setText("All Done");

                firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(PhoneVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(PhoneVerifyActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String refreshedToken = instanceIdResult.getToken();


                                }
                            });

                            countDownTimer = null;
                            counterIsActive = false;

                            context = null;


                           Intent intent = new Intent(PhoneVerifyActivity.this, HomePageActivity.class);
                           intent.putExtra("name", name);
                            intent.putExtra("contact", contact);
                            intent.putExtra("organisation", organisation);
                            intent.putExtra("folk",folk);
                            intent.putExtra("round", round);
                            startActivity(intent);
                            finish();

                        } else {

                            //verification unsuccessful.. display an error message

                            Toast.makeText(PhoneVerifyActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(PhoneVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + contact, 60, TimeUnit.SECONDS, this, mCallBacks);

    }


    public void updateTimer(int secondsLeft) {

        String timeLeft = Integer.toString(secondsLeft);

        timeTextView.setText("Try Again in " + timeLeft + " seconds");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}



