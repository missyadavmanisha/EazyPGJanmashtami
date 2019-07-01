package net.eazypg.eazypgjanmashtami;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LoginActivity  extends AppCompatActivity {

    EditText nameEditText, contactEditText, organisationEditText, folkGuideEditText, roundChantingEditText;
    TextView dateOfBirthEditText;
    Button signUpButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameEditText = findViewById(R.id.ownerNameEditText);
        contactEditText = findViewById(R.id.contactEditText);
        organisationEditText = findViewById(R.id.organisationEditText);
        folkGuideEditText = findViewById(R.id.folkGuideNameEditText);
        roundChantingEditText = findViewById(R.id.roundsChanting);
        signUpButton = findViewById(R.id.signUpButton);
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                dateOfBirthEditText.setText(sdf.format(calendar.getTime()));
            }
        };

        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LoginActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameEditText.getText().toString().isEmpty()) {
                    nameEditText.setError("Required");
                }
               else if(contactEditText.getText().toString().length()!=10) {
                    contactEditText.setError("Required");
                }
                else {

                    Intent intent = new Intent(LoginActivity.this, PhoneVerifyActivity.class);
                    intent.putExtra("name", nameEditText.getText().toString());
                    intent.putExtra("contact", contactEditText.getText().toString());
                    intent.putExtra("organisation", organisationEditText.getText().toString());
                    intent.putExtra("folk", folkGuideEditText.getText().toString());
                    intent.putExtra("round", roundChantingEditText.getText().toString());
                    startActivity(intent);

                }
            }
        });



    }
}
