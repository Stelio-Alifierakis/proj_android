package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import be.heh.campus_technique.proj_android_s_alifierakis.ecritureAutomate.WriteS7Conditionnement;
import be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate.ReadS7Conditionnement;

public class Automate_Comprime extends Activity {

    Button bt_autoCond_ro;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    private ReadS7Conditionnement readS7;
    private WriteS7Conditionnement writeS7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate__comprime);

        bt_autoCond_ro = (Button) findViewById(R.id.bt_autoCond_ro);

        connexStatus=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connexStatus.getActiveNetworkInfo();
    }

    public void onAutoCondClickManager(View v){
        switch(v.getId()){
            case R.id.bt_autoCond_ro:
                if(network !=null && network.isConnectedOrConnecting()){
                    if(bt_autoCond_ro.getText().equals("Lire automate")){
                        Toast.makeText(this,network.getTypeName(),Toast.LENGTH_LONG).show();
                        bt_autoCond_ro.setText("Se déconnecter");
                        readS7=new ReadS7Conditionnement(v);
                        readS7.Start("192.168.10.220","0","2");
                        //readS7.Start("10.1.0.118","0","2");

                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        writeS7=new WriteS7Conditionnement();
                        writeS7.Start("192.168.10.220","0","2");

                    }
                    else{
                        readS7.Stop();
                        writeS7.Stop();
                        try{

                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            Log.i("Automate_Comprime","test");
                            e.printStackTrace();
                        }

                        bt_autoCond_ro.setText("Lire automate");

                        Toast.makeText(this,"Le traitement a été interrompu par l'utilisateur",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(this,"Connexion au réseau impossible",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void lectureAutomate(int[]donnees){
        int d1=donnees[0];
        int d2=donnees[1];
        int d3=donnees[2];
        int d4=donnees[3];
    }
}
