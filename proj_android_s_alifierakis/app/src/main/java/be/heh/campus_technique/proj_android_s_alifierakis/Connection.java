package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Connection extends Activity {

    private Button bt_connection_text_co1;
    private Button bt_connection_text_lancerAutomate1;
    private Button bt_connection_text_co2;
    private Button bt_connection_text_lancerAutomate2;

    private LinearLayout ll_connection_coAutomate1;
    private LinearLayout ll_connection_coAutomate2;

    private EditText et_connection_ipAUtomate1;
    private EditText et_connection_rackAUtomate1;
    private EditText et_connection_slotAUtomate1;
    private EditText et_connection_ipAUtomate2;
    private EditText et_connection_rackAUtomate2;
    private EditText et_connection_slotAUtomate2;

    //SharedPreferences pref_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        bt_connection_text_co1=(Button) findViewById(R.id.bt_connection_text_co1);
        bt_connection_text_lancerAutomate1=(Button) findViewById(R.id.bt_connection_text_lancerAutomate1);
        bt_connection_text_co2=(Button) findViewById(R.id.bt_connection_text_co2);
        bt_connection_text_lancerAutomate2=(Button) findViewById(R.id.bt_connection_text_lancerAutomate2);

        ll_connection_coAutomate1=(LinearLayout) findViewById(R.id.ll_connection_coAutomate1);
        ll_connection_coAutomate2=(LinearLayout) findViewById(R.id.ll_connection_coAutomate2);

        et_connection_ipAUtomate1=(EditText) findViewById(R.id.et_connection_ipAUtomate1);
        et_connection_rackAUtomate1=(EditText) findViewById(R.id.et_connection_rackAUtomate1);
        et_connection_slotAUtomate1=(EditText) findViewById(R.id.et_connection_slotAUtomate1);
        et_connection_ipAUtomate2=(EditText) findViewById(R.id.et_connection_ipAUtomate2);
        et_connection_rackAUtomate2=(EditText) findViewById(R.id.et_connection_rackAUtomate2);
        et_connection_slotAUtomate2=(EditText) findViewById(R.id.et_connection_slotAUtomate2);

        //pref_data = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void onConnexionClickManager(View v){
        switch(v.getId()){
            case R.id.bt_connection_text_co1 :

                /*if(!pref_data.getAll().isEmpty()){
                    et_connection_ipAUtomate1.setText(pref_data.getString("ipAutom1","NULL"));
                    et_connection_rackAUtomate1.setText(pref_data.getString("rackAutom1","NULL"));
                    et_connection_slotAUtomate1.setText(pref_data.getString("slotAutom1","NULL"));
                }*/

                try{
                    FileInputStream ins=openFileInput("autom1.txt");
                    BufferedReader reader=new BufferedReader(new InputStreamReader(ins));
                    StringBuilder out=new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null){
                        out.append(line);
                    }

                    reader.close();
                    ins.close();

                    String[] confs=out.toString().split("#");
                    et_connection_ipAUtomate1.setText(confs[0]);
                    et_connection_rackAUtomate1.setText(confs[1]);
                    et_connection_slotAUtomate1.setText(confs[2]);

                }
                catch (FileNotFoundException ex){
                    ex.printStackTrace();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }

                ll_connection_coAutomate1.setVisibility(View.VISIBLE);
                ll_connection_coAutomate2.setVisibility(View.GONE);
                break;
            case R.id.bt_connection_text_co2 :
                ll_connection_coAutomate2.setVisibility(View.VISIBLE);
                ll_connection_coAutomate1.setVisibility(View.GONE);
                break;
            case R.id.bt_connection_text_lancerAutomate1 :
                String str=et_connection_ipAUtomate1.getText().toString()+"#"+et_connection_rackAUtomate1.getText().toString()+"#"+et_connection_slotAUtomate1.getText().toString();
                try{
                    FileOutputStream ous=openFileOutput("autom1.txt",MODE_APPEND);
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

                /*SharedPreferences.Editor edit_data=pref_data.edit();

                edit_data.putString("ipAutom1",et_connection_ipAUtomate1.getText().toString());
                edit_data.putString("rackAutom1",et_connection_rackAUtomate1.getText().toString());
                edit_data.putString("slotAutom1",et_connection_slotAUtomate1.getText().toString());
                edit_data.commit();*/

                Intent intent = new Intent(this,Automate_Comprime.class);
                startActivity(intent);
                finish();

                break;
        }
    }
}
