package com.jawad.driverapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Login extends AppCompatActivity {

    CardView Loginbtn;
    TextView username;
    TextView pass;
    DatabaseReference databaseReference;
    public static String user;
    public static String password;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            Loginbtn=(CardView) findViewById(R.id.loginbtn);
            username=(TextView)findViewById(R.id.usernameET);
            pass=(TextView)findViewById(R.id.PasswordET);
            user =username.getText().toString();


        Toolbar toolbar=(Toolbar)findViewById(R.id.map_Toolbar);
        setSupportActionBar(toolbar);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String us = username.getText().toString().trim();


                final String _password = pass.getText().toString().trim();
              //  Toast.makeText(Login.this,_password,Toast.LENGTH_LONG).show();
                String _user= "User/".concat(us);
                databaseReference= FirebaseDatabase.getInstance().getReference("User");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(_user);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username = "";
                        String password="";

                        username=(String) dataSnapshot.child("username").getValue();
                        password =(String) dataSnapshot.child("password").getValue();

                       // Toast.makeText(Login.this,_password,Toast.LENGTH_LONG).show();
                        if (_password.equals(password)){
                            SharedPreferences sharedpref=getSharedPreferences("LoginPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedpref.edit();
                            editor.putString("username",username);
                            editor.putBoolean("isLogin",true);
                            Intent intent=new Intent(Login.this,MainActivity.class);
                            intent.putExtra("username",username);

                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Login.this,"username and password is wrong",Toast.LENGTH_LONG).show();

                           }
                     // String username=dataSnapshot.toString();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(Login.this, (CharSequence) databaseError,Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }//end of onCreate function

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }//end of backpressed fucntion

//    public void progress(View view)
//    {
//
//        progress= new ProgressDialog(this);
//        progress.setMessage("processing");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setProgress(0);
//        progress.show();
//         final int totalProgressTime=100;
//        final Thread t =new Thread()
//        {
//          int jumpTime=0;
//          while(jumpTime < totalProgressTime)
//            {
//
//            }
//        };
//    }
}
