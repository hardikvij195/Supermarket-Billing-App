package com.hvtechnologies.supermarketbillingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import android.os.Bundle;

public class Otp_Login extends AppCompatActivity {


    Button askotp , ok ;
    EditText ph , otp ;
    String user_id , CodeSent ;
    private FirebaseAuth mAuth;
    private DatabaseReference ClassRef ;

    private ProgressDialog mLoginProgress;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__login);

        ClassRef = FirebaseDatabase.getInstance().getReference("Users/") ;


        askotp = (Button)findViewById(R.id.button2);
        ok = (Button)findViewById(R.id.button3);

        ph = (EditText)findViewById(R.id.editText);
        otp = (EditText)findViewById(R.id.editText2);


        mLoginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();



        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable())) {

            Toast.makeText(getApplicationContext(), "Internet Not Available", Toast.LENGTH_SHORT).show();

        }


        askotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String num = ph.getText().toString().trim();
                if (num.isEmpty()) {
                    Toast.makeText(Otp_Login.this, "Number Field Cannot Be Empty", Toast.LENGTH_SHORT).show();
                }else if (num.length() < 10) {
                    Toast.makeText(Otp_Login.this, "Number Cannot Be Less Than 10 Digits", Toast.LENGTH_SHORT).show();

                }else {

                    sendVerificationCode(num);

                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLoginProgress.setTitle("Logging In");
                mLoginProgress.setMessage("Please Wait While We Log Into Your Account");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();

                VerifyCode();


            }
        });






    }




    private void VerifyCode() {


        String code = otp.getText().toString() ;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent , code);
        signInWithPhoneAuthCredential(credential);

    }
    private void sendVerificationCode(String phoneNumber) {



        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);



    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            Toast.makeText(Otp_Login.this , "CODE - " +  phoneAuthCredential.getSmsCode() , Toast.LENGTH_SHORT ).show();

        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(Otp_Login.this , "CODE - " +  e.getMessage() , Toast.LENGTH_SHORT ).show();


        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            CodeSent = s ;
            Toast.makeText(Otp_Login.this , "CODE SENT" , Toast.LENGTH_SHORT ).show();


        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            Toast.makeText(Otp_Login.this , "Login Successful" , Toast.LENGTH_SHORT).show();
                            final String user_id = task.getResult().getUser().getUid();

                            SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPrefs.edit();
                            edit.putString("Uid" , user_id );
                            edit.putString("Ph" , ph.getText().toString() );
                            edit.apply();

                            ClassRef = FirebaseDatabase.getInstance().getReference("Users/Use/"+ user_id+ "/Name/") ;
                            ClassRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){



                                        Intent m2 = new Intent(Otp_Login.this , UserFormActivity.class);
                                        startActivity(m2);
                                        finish();
                                        mLoginProgress.dismiss();


                                    }else {

                                        ClassRef = FirebaseDatabase.getInstance().getReference("Users/Use/"+ user_id) ;
                                        HashMap<String,String> dataMap2 = new HashMap<String, String>();
                                        dataMap2.put("Phone Number" , ph.getText().toString());
                                        dataMap2.put("Uid" , user_id);
                                        ClassRef.setValue(dataMap2);

                                        Intent m2 = new Intent(Otp_Login.this , UserFormActivity.class);
                                        startActivity(m2);
                                        finish();
                                        mLoginProgress.dismiss();


                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Otp_Login.this , "Wrong Code" , Toast.LENGTH_SHORT).show();

                                mLoginProgress.dismiss();

                            }
                        }
                    }
                });
    }


}
