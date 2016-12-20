package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate.ReadS7Conditionnement;

public class Automate_Comprime extends Activity {

    Button bt_autoCond_ro;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    private ReadS7Conditionnement readS7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate__comprime);

        bt_autoCond_ro = (Button) findViewById(R.id.bt_autoCond_ro);
    }

    public void onAutoCondClickManager(View v){
        switch(v.getId()){
            case R.id.bt_autoCond_ro:
                if(network !=null && network.isConnectedOrConnecting()){
                    if(bt_autoCond_ro.getText().equals("Se connecter")){
                        Toast.makeText(this,network.getTypeName(),Toast.LENGTH_LONG).show();
                        bt_autoCond_ro.setText("Se déconnecter");
                        readS7=new ReadS7Conditionnement(v);
                        readS7.Start("192.168.10.220","0","2");
                    }
                    else{

                    }
                }
                else{
                    Toast.makeText(this,"Connexion au réseau impossible",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
