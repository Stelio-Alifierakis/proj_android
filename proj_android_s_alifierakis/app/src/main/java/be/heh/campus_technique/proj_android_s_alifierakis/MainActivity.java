package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
//import java.util.ArrayList;

import be.heh.campus_technique.proj_android_s_alifierakis.BDD.User;
import be.heh.campus_technique.proj_android_s_alifierakis.BDD.UserAccessBDD;

public class MainActivity extends Activity {

    EditText et_main_pwd;
    EditText et_main_login;

    Button bt_main_text_co;

    SharedPreferences pref_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_main_pwd=(EditText)findViewById(R.id.et_main_pwd);
        et_main_login=(EditText)findViewById(R.id.et_main_login);

        bt_main_text_co=(Button)findViewById(R.id.bt_main_text_co);

        pref_data = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Bundle extraTxt=this.getIntent().getExtras();
        if(extraTxt != null){
            et_main_login.setText(extraTxt.getString("login"));
        }


        /*String str="5#0#1#2#3#0#0#1#3#3#20";
        try{
            //FileOutputStream ous=openFileOutput("autom1.txt",MODE_APPEND);
            FileOutputStream ous=openFileOutput("RWautom1.txt",MODE_PRIVATE);
            byte[] buff;
            buff=str.toString().getBytes();
            ous.write(buff);
            ous.close();
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }*/

    }

    public void onMainClickManager(View v){
        switch(v.getId()){
            case R.id.bt_main_text_co :

                try{
                    UserAccessBDD userBD =new UserAccessBDD(this);

                    userBD.openForRead();

                    int nbAdmin=userBD.countAdmin("FC");
                    Log.i("------->Essai ", String.valueOf(nbAdmin));

                    if(nbAdmin==0){
                        Hash h=new Hash();
                        String hashCdc=h.hashage("android3","SHA-1");

                        User user1=new User("android",hashCdc,"FC");

                        userBD.openForWrite();
                        userBD.insertUser(user1);

                        String str="5#0#1#2#3#0#0#1#3#3#20";
                        try{
                            //FileOutputStream ous=openFileOutput("autom1.txt",MODE_APPEND);
                            FileOutputStream ous=openFileOutput("RWautom1.txt",MODE_PRIVATE);
                            byte[] buff;
                            buff=str.toString().getBytes();
                            ous.write(buff);
                            ous.close();
                        }
                        catch (FileNotFoundException ex){
                            ex.printStackTrace();
                        }
                        catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }

                    userBD.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                if(et_main_login.getText().length()>0 && et_main_pwd.getText().length()>0){

                    Hash h=new Hash();

                    String hashCdc=h.hashage(et_main_pwd.getText().toString(),"SHA-1");

                    /*String hashCdc="";
                    try{
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        md.update(et_main_pwd.getText().toString().getBytes());
                        byte[] hashByte=md.digest();

                        char[] hexArray = "0123456789ABCDEF".toCharArray();

                        StringBuffer buffer = new StringBuffer();
                        for (int j = 0; j < hashByte.length; j++) {
                            buffer.append(hexArray[(hashByte[j] >> 4) & 0x0f]);
                            buffer.append(hexArray[hashByte[j] & 0x0f]);
                        }

                        hashCdc=buffer.toString();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }*/
                    // User user1 =
                    try{

                        UserAccessBDD userBD =new UserAccessBDD(this);

                        userBD.openForRead();
                        //ArrayList<User> tabUser=userBD.getAllUsers();
                        User user1 =userBD.getUser(et_main_login.getText().toString());
                        userBD.close();

                        Log.i("user",user1.getLogin());
                        Log.i("mdp",String.valueOf(user1.getPassword().compareTo(hashCdc)));

                        if(user1.getPassword().compareTo(hashCdc)==0){

                            SharedPreferences.Editor edit_data=pref_data.edit();

                            edit_data.putString("login",et_main_login.getText().toString());
                            edit_data.putString("droit",user1.getDroit());
                            edit_data.commit();

                            Intent intent = new Intent(this,Connection.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(this,"Connexion impossible",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(this,"Connexion impossible",Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}
