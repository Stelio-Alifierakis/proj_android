package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import be.heh.campus_technique.proj_android_s_alifierakis.ecritureAutomate.WriteS7Conditionnement;
import be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate.ReadS7Conditionnement;

public class Automate_Comprime extends Activity {

    ImageView img_AutoCompr_arriveeFlacon;
    ImageView img_AutoCompr_moteur;
    Button bt_autoCond_arriveeFlacon;
    Button bt_autoCond_ro;
    Button bt_autoCond_selecteur;
    RadioButton rb_AutoCompr_5compr;
    RadioButton rb_AutoCompr_10compr;
    RadioButton rb_AutoCompr_15compr;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    private ReadS7Conditionnement readS7;
    private WriteS7Conditionnement writeS7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate__comprime);

        img_AutoCompr_arriveeFlacon = (ImageView) findViewById(R.id.img_AutoCompr_arriveeFlacon);
        img_AutoCompr_moteur = (ImageView) findViewById(R.id.img_AutoCompr_moteur);
        bt_autoCond_ro = (Button) findViewById(R.id.bt_autoCond_ro);
        bt_autoCond_arriveeFlacon = (Button) findViewById(R.id.bt_autoCond_arriveeFlacon);
        bt_autoCond_selecteur = (Button) findViewById(R.id.bt_autoCond_selecteur);
        rb_AutoCompr_5compr = (RadioButton) findViewById(R.id.rb_AutoCompr_5compr);
        rb_AutoCompr_10compr = (RadioButton) findViewById(R.id.rb_AutoCompr_10compr);
        rb_AutoCompr_15compr = (RadioButton) findViewById(R.id.rb_AutoCompr_15compr);

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
                        readS7=new ReadS7Conditionnement(this,v);
                        //readS7.Start("192.168.10.220","0","2");
                        readS7.Start("192.168.0.220","0","2");

                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        //writeS7=new WriteS7Conditionnement();
                        //writeS7.Start("192.168.10.220","0","2");
                        //writeS7.Start("192.168.0.220","0","2");
                    }
                    else{
                        readS7.Stop();
                        //writeS7.Stop();
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


        Log.i("Automate_Comprime ","1 :" +String.valueOf(d2) + " valeur binaire :" +String.valueOf(0x0200 & d2));

        bt_autoCond_selecteur.setText((0x0100 & d1) ==256 ? "Désactiver sélecteur" : "Activer sélecteur");
        bt_autoCond_arriveeFlacon.setText((0x0008 & d1) ==8 ? "Désactiver arrivée des flacons" : "Activer arrivée des flacons");

        String uri = ((0x0008 & d1) ==8 ? "@android:drawable/presence_online" : "@android:drawable/presence_offline");
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_arriveeFlacon.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        rb_AutoCompr_5compr.setChecked( (0x0800 & d2)== 2048? true : false);
        rb_AutoCompr_10compr.setChecked( (0x1000 & d2)== 4096? true : false);
        rb_AutoCompr_15compr.setChecked( (0x2000 & d2)== 8192? true : false);

        uri = ((0x0200 & d2) ==512 ? "@android:drawable/presence_online" : "@android:drawable/presence_offline");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_moteur.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));
    }
}
