package com.hvtechnologies.supermarketbillingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowPage extends AppCompatActivity {



    TextView supermarket , userlog ;

    String user_id;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private ProgressDialog mLoginProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_page);


        Utils.getDatabase();
        mLoginProgress = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        supermarket = (TextView)findViewById(R.id.supermarketlogin);
        userlog = (TextView)findViewById(R.id.UserLogin);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        supermarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(ShowPage.this, Email_Login.class);
                startActivity(mainIntent);



            }
        });

        userlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(ShowPage.this, Otp_Login.class);
                startActivity(mainIntent);


            }
        });



        if (currentUser != null) {

            mLoginProgress.setTitle("Logging In");
            mLoginProgress.setMessage("Please Wait While We Log Into Your Account");
            mLoginProgress.setCanceledOnTouchOutside(false);
            mLoginProgress.show();
            user_id = currentUser.getUid();


            DatabaseReference AlreadySignedIn1 = database.getReference("Users");
            AlreadySignedIn1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child("Supermarket").child(user_id).exists())
                    {

                        Intent mainIntent = new Intent(ShowPage.this, SuperMarket.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();

                    }
                    else if(dataSnapshot.child("Admin").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(ShowPage.this, Admin_Activity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();



                    }
                    else if(dataSnapshot.child("Use").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(ShowPage.this, Users.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();



                    }
                    else {

                        mAuth.signOut();
                        Toast.makeText(ShowPage.this, "User Not Found", Toast.LENGTH_SHORT).show();
                        mLoginProgress.dismiss();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            Toast.makeText(ShowPage.this, "User Already Logged In :  " + user_id, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(ShowPage.this, "No User Logged In ", Toast.LENGTH_SHORT).show();

        }









    }
}
