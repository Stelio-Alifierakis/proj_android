package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import be.heh.campus_technique.proj_android_s_alifierakis.ecritureAutomate.WriteS7Conditionnement;
import be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate.ReadS7Conditionnement;

public class Automate_Comprime extends Activity {

    ImageView img_AutoCompr_arriveeFlacon;
    ImageView img_AutoCompr_moteur;
    ImageView img_AutoCompr_service;

    Button bt_autoCond_arriveeFlacon;
    Button bt_autoCond_ro;
    Button bt_autoCond_changeNbCompr;
    Button bt_autoCond_selecteur;
    Button bt_autoCond_resetCompteur;
    Button bt_autoCond_retourChxAuto;

    RadioButton rb_AutoCompr_5compr;
    RadioButton rb_AutoCompr_10compr;
    RadioButton rb_AutoCompr_15compr;

    TextView tv_AutoCompr_txt_nbBoutAffich;
    TextView tv_AutoCompr_txt_nbreComprAffich;
    TextView tv_AutoCompr_txt_service;
    TextView tv_AutoCompr_txtIp;
    TextView tv_AutoCompr_txtRack;
    TextView tv_AutoCompr_txtSlot;

    LinearLayout ll_AutoCompr_service;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    private ReadS7Conditionnement readS7;
    private WriteS7Conditionnement writeS7;

    private String ipAdr;
    private String rack;
    private String slot;

    //SharedPreferences pref_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate__comprime);

        ll_AutoCompr_service = (LinearLayout) findViewById(R.id.ll_AutoCompr_service);

        img_AutoCompr_arriveeFlacon = (ImageView) findViewById(R.id.img_AutoCompr_arriveeFlacon);
        img_AutoCompr_moteur = (ImageView) findViewById(R.id.img_AutoCompr_moteur);
        img_AutoCompr_service = (ImageView) findViewById(R.id.img_AutoCompr_service);

        bt_autoCond_ro = (Button) findViewById(R.id.bt_autoCond_ro);
        bt_autoCond_arriveeFlacon = (Button) findViewById(R.id.bt_autoCond_arriveeFlacon);
        bt_autoCond_selecteur = (Button) findViewById(R.id.bt_autoCond_selecteur);
        bt_autoCond_changeNbCompr = (Button) findViewById(R.id.bt_autoCond_changeNbCompr);
        bt_autoCond_resetCompteur = (Button) findViewById(R.id.bt_autoCond_resetCompteur);
        bt_autoCond_retourChxAuto = (Button) findViewById(R.id.bt_autoCond_retourChxAuto);

        rb_AutoCompr_5compr = (RadioButton) findViewById(R.id.rb_AutoCompr_5compr);
        rb_AutoCompr_10compr = (RadioButton) findViewById(R.id.rb_AutoCompr_10compr);
        rb_AutoCompr_15compr = (RadioButton) findViewById(R.id.rb_AutoCompr_15compr);

        tv_AutoCompr_txt_nbBoutAffich = (TextView) findViewById(R.id.tv_AutoCompr_txt_nbBoutAffich);
        tv_AutoCompr_txt_nbreComprAffich = (TextView) findViewById(R.id.tv_AutoCompr_txt_nbreComprAffich);
        tv_AutoCompr_txt_service = (TextView) findViewById(R.id.tv_AutoCompr_txt_service);
        tv_AutoCompr_txtIp = (TextView) findViewById(R.id.tv_AutoCompr_txtIp);
        tv_AutoCompr_txtRack = (TextView) findViewById(R.id.tv_AutoCompr_txtRack);
        tv_AutoCompr_txtSlot = (TextView) findViewById(R.id.tv_AutoCompr_txtSlot);

        connexStatus=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connexStatus.getActiveNetworkInfo();

        ll_AutoCompr_service.setEnabled(false);

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
            ipAdr=confs[0];
            rack=confs[1];
            slot=confs[2];

        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        /*pref_data = PreferenceManager.getDefaultSharedPreferences(getApplication());
        ipAdr=pref_data.getString("ipAutom1","NULL");
        rack=pref_data.getString("rackAutom1","NULL");
        slot=pref_data.getString("slotAutom1","NULL");*/

        tv_AutoCompr_txtIp.setText(tv_AutoCompr_txtIp.getText() + " " + ipAdr);
        tv_AutoCompr_txtRack.setText(tv_AutoCompr_txtRack.getText() + " " + rack);
        tv_AutoCompr_txtSlot.setText(tv_AutoCompr_txtSlot.getText() + " " + slot);
    }

    public void onAutoCondClickManager(View v){
        switch(v.getId()){
            case R.id.bt_autoCond_retourChxAuto:

                if(bt_autoCond_ro.getText().equals("Se déconnecter")){
                    readS7.Stop();
                    writeS7.Stop();
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){
                        Log.i("Automate_Comprime","test");
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(this,Connection.class);
                startActivity(intent);
                break;
            case R.id.bt_autoCond_ro:
                Log.i("test co",ipAdr + " "+ rack + " "+ slot);
                if(network !=null && network.isConnectedOrConnecting()){
                    if(bt_autoCond_ro.getText().equals("Lire automate")){
                        ll_AutoCompr_service.setVisibility(View.VISIBLE);
                        Toast.makeText(this,network.getTypeName(),Toast.LENGTH_LONG).show();
                        bt_autoCond_ro.setText("Se déconnecter");
                        readS7=new ReadS7Conditionnement(this,v);
                        //readS7.Start("192.168.10.220","0","2");
                        //readS7.Start("192.168.0.220","0","2");
                        readS7.Start(ipAdr,rack,slot);
                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        writeS7=new WriteS7Conditionnement();
                        //writeS7.Start("192.168.10.220","0","2");
                        //writeS7.Start("192.168.0.220","0","2");
                        writeS7.Start(ipAdr,rack,slot);
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

                        String uri = "@android:drawable/presence_offline";
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        img_AutoCompr_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

                        tv_AutoCompr_txt_service.setText("Application non connectée à l'automate");

                        Toast.makeText(this,"Le traitement a été interrompu par l'utilisateur",Toast.LENGTH_LONG).show();
                        ll_AutoCompr_service.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(this,"Connexion au réseau impossible",Toast.LENGTH_LONG).show();
                    problemeCo();
                    ll_AutoCompr_service.setVisibility(View.GONE);
                }
                break;
            case R.id.rb_AutoCompr_5compr:

                writeS7.setWriteBool(0,2, rb_AutoCompr_5compr.isChecked() ? 1 : 0);
                writeS7.setWriteBool(0,4, rb_AutoCompr_10compr.isChecked() ? 1 : 0);
                writeS7.setWriteBool(0,8, rb_AutoCompr_15compr.isChecked() ? 1 : 0);
                break;
            case R.id.rb_AutoCompr_10compr:
                writeS7.setWriteBool(0,2, rb_AutoCompr_5compr.isChecked() ? 1 : 0);
                writeS7.setWriteBool(0,4, rb_AutoCompr_10compr.isChecked() ? 1 : 0);
                writeS7.setWriteBool(0,8, rb_AutoCompr_15compr.isChecked() ? 1 : 0);
                break;
            case R.id.rb_AutoCompr_15compr:
                writeS7.setWriteBool(0,2, rb_AutoCompr_5compr.isChecked() ? 1 : 0);
                writeS7.setWriteBool(0,4, rb_AutoCompr_10compr.isChecked() ? 1 : 0);
                writeS7.setWriteBool(0,8, rb_AutoCompr_15compr.isChecked() ? 1 : 0);
                break;
            case R.id.bt_autoCond_selecteur:
                writeS7.setWriteBool(0,1, bt_autoCond_selecteur.getText().toString()=="Activer sélecteur" ? 1 : 0);
                break;
            case R.id.bt_autoCond_arriveeFlacon:
                writeS7.setWriteBool(1,8, bt_autoCond_arriveeFlacon.getText()=="Activer arrivée des flacons" ? 1 : 0);
                break;
            case R.id.bt_autoCond_changeNbCompr:
                writeS7.setWriteByte(3,Integer.parseInt(tv_AutoCompr_txt_nbreComprAffich.getText().toString()));
                break;
            case R.id.bt_autoCond_resetCompteur:

                writeS7.setWriteInt(0);
                break;
        }
    }

    public void problemeCo(){
        String uri = "@android:drawable/presence_busy";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        tv_AutoCompr_txt_service.setText("Problème de connexion");

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
        ll_AutoCompr_service.setEnabled(false);

        Toast.makeText(this,"Le traitement a été interrompu car l'application n'a pas pu se connecter à l'automate",Toast.LENGTH_LONG).show();
    }

    public void lectureAutomate(int[]donnees){
        int d1=donnees[0];
        int d2=donnees[1];
        int d3=donnees[2];
        int d4=donnees[3];

        String uri = "@android:drawable/presence_online";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        tv_AutoCompr_txt_service.setText("Automate en service");

        //Log.i("Automate_Comprime ","1 :" +String.valueOf(d4));

        bt_autoCond_selecteur.setText((0x0100 & d1) ==256 ? "Désactiver sélecteur" : "Activer sélecteur");
        bt_autoCond_arriveeFlacon.setText((0x0008 & d1) ==8 ? "Désactiver arrivée des flacons" : "Activer arrivée des flacons");

        uri = ((0x0008 & d1) ==8 ? "@android:drawable/presence_online" : "@android:drawable/presence_offline");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_arriveeFlacon.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        rb_AutoCompr_5compr.setChecked( (0x0800 & d2)== 2048? true : false);
        rb_AutoCompr_10compr.setChecked( (0x1000 & d2)== 4096? true : false);
        rb_AutoCompr_15compr.setChecked( (0x2000 & d2)== 8192? true : false);

        uri = ((0x0200 & d2) ==512 ? "@android:drawable/presence_online" : "@android:drawable/presence_offline");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_moteur.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        tv_AutoCompr_txt_nbBoutAffich.setText(String.valueOf(d4));
        tv_AutoCompr_txt_nbreComprAffich.setText(rb_AutoCompr_5compr.isChecked()? "5" : ( rb_AutoCompr_10compr.isChecked()? "10" : (rb_AutoCompr_15compr.isChecked()? "15" : "0")));
    }
}
