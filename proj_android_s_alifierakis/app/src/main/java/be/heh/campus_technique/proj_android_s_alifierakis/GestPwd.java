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
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

import be.heh.campus_technique.proj_android_s_alifierakis.BDD.User;
import be.heh.campus_technique.proj_android_s_alifierakis.BDD.UserAccessBDD;

public class GestPwd extends Activity {

    TextView tv_GestUser_login;

    EditText et_GestUser_ancientMDP;
    EditText et_GestUser_nouveauMDP;

    Button bt_GestUser_comeBack;
    Button bt_GestUser_changerMDP;

    SharedPreferences pref_data;

    String login;
    String droit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gest_pwd);

        tv_GestUser_login=(TextView)findViewById(R.id.tv_GestUser_login);

        et_GestUser_ancientMDP=(EditText)findViewById(R.id.et_GestUser_ancientMDP);
        et_GestUser_nouveauMDP=(EditText)findViewById(R.id.et_GestUser_nouveauMDP);

        bt_GestUser_comeBack=(Button)findViewById(R.id.bt_GestUser_comeBack);
        bt_GestUser_changerMDP=(Button)findViewById(R.id.bt_GestUser_changerMDP);

        pref_data = PreferenceManager.getDefaultSharedPreferences(getApplication());

        login=pref_data.getString("login","NULL");
        droit=pref_data.getString("droit","NULL");

        tv_GestUser_login.setText(login);

    }

    public void onGestPwdClickManager(View v){

        switch(v.getId()){
            case R.id.bt_GestUser_comeBack :
                Intent intent = new Intent(this,Connection.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_GestUser_changerMDP :
                if(et_GestUser_ancientMDP.getText().length()>0){

                    Hash h=new Hash();

                    String hashCdc=h.hashage(et_GestUser_ancientMDP.getText().toString(),"SHA-1");

                    /*String hashCdc="";
                    try{
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        md.update(et_GestUser_ancientMDP.getText().toString().getBytes());
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

                    try{

                        UserAccessBDD userBD =new UserAccessBDD(this);

                        userBD.openForRead();
                        User user1 =userBD.getUser(login);
                        userBD.close();

                        Log.i("mdp",String.valueOf(user1.getPassword().compareTo(hashCdc)));

                        if(user1.getPassword().compareTo(hashCdc)==0){

                            if(et_GestUser_nouveauMDP.getText().length()>=4){

                                String newHash=h.hashage(et_GestUser_nouveauMDP.getText().toString(),"SHA-1");

                                Log.i("testBd",user1.getPassword() + hashCdc);
                                user1.setPassword(newHash);

                                userBD.openForWrite();
                                userBD.updateUser(user1.getId(),user1);
                                userBD.close();

                                Toast.makeText(this,"Le mot de passe est changé",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(this,"Le nouveau mot de passe est incorrect",Toast.LENGTH_LONG).show();
                            }

                        }
                        else{
                            Toast.makeText(this,"L'ancien mot de passe est incorrect",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(this,"L'ancien mot de passe est incorrect",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}
