package com.hvtechnologies.supermarketbillingsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;

public class Email_Login extends AppCompatActivity {




    Button login ;
    EditText Nametxt, Passtxt;

    TextView forgotPass ;
    String user_id;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog mLoginProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__login);



        login = (Button) findViewById(R.id.LoginActBtn);
        forgotPass = (TextView)findViewById(R.id.ForgotPasswordTextView);


        mLoginProgress = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Nametxt = (EditText) findViewById(R.id.NameText);
        Passtxt = (EditText) findViewById(R.id.PassText);




        //FORGOT PASSWORD -----------------------------------------------------------------------

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Email_Login.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_forgot_password, null);
                final EditText EditTextForgotPass = (EditText) mView.findViewById(R.id.EditTextForgotPassword);

                AlertDialog.Builder builder = mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        if (!EditTextForgotPass.getText().toString().isEmpty()) {

                            mLoginProgress.setMessage("Sending Mail");
                            mLoginProgress.setCanceledOnTouchOutside(false);
                            mLoginProgress.show();
                            mAuth.sendPasswordResetEmail(EditTextForgotPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(Email_Login.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();
                                        dialog.dismiss();
                                    } else {

                                        Toast.makeText(Email_Login.this, "Email Does Not Exist", Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();
                                        dialog.dismiss();
                                    }

                                }
                            });


                        }


                    }
                });

                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = Nametxt.getText().toString();
                final String Pass = Passtxt.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(Pass)) {

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();




                    mAuth.signInWithEmailAndPassword(email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {


                                Toast.makeText(Email_Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = task.getResult().getUser();
                                user_id = user.getUid();

                                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPrefs.edit();
                                edit.putString("USERID" , user_id );
                                edit.putString("EMAIL" , Nametxt.getText().toString().trim());
                                edit.putString("PASSWORD" , Passtxt.getText().toString().trim() );
                                edit.apply();

                                //DatabaseReference myRef = database.getReference("Users/" + user_id).child("Type");

                                DatabaseReference AlreadySignedIn1 = database.getReference("Users");
                                AlreadySignedIn1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.child("Supermarket").child(user_id).exists())
                                        {


                                            Intent mainIntent = new Intent(Email_Login.this, SuperMarket.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                            mLoginProgress.dismiss();
                                        }
                                        else if(dataSnapshot.child("Admin").child(user_id).exists())
                                        {

                                            Intent mainIntent = new Intent(Email_Login.this, Admin_Activity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                            mLoginProgress.dismiss();



                                        }
                                        else {
                                            mAuth.signOut();
                                            Toast.makeText(Email_Login.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                            mLoginProgress.dismiss();


                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else {

                                try
                                {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    mLoginProgress.hide();
                                    Toast.makeText(Email_Login.this, "Invalid Email" , Toast.LENGTH_SHORT).show();


                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                {
                                    mLoginProgress.hide();
                                    Toast.makeText(Email_Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(Email_Login.this,  e.getMessage() ,
                                            Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();

                                }


                            }


                        }

                    });

                } else {
                    Toast.makeText(Email_Login.this, "FIELDS CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();

                }
            }
        });










    }
}
