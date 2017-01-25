package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import be.heh.campus_technique.proj_android_s_alifierakis.ecritureAutomate.WriteS7Regulation;
import be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate.ReadS7Regulation;

public class Automate_Regulation extends Activity {

    private ProgressBar pb_AutoReg_nivLiq;

    Button bt_autoReg_retourAccueil;
    Button bt_autoReg_retourChxAuto;
    Button bt_autoReg_ro;
    Button bt_autoReg_v2;
    Button bt_autoReg_v3;
    Button bt_autoReg_v4;
    Button bt_autoReg_v1;
    Button bt_autoReg_valEcriture;

    TextView tv_AutoReg_txtIp;
    TextView tv_AutoReg_txtRack;
    TextView tv_AutoReg_txtSlot;
    TextView tv_AutoReg_SP;
    TextView tv_AutoReg_manuel;
    TextView tv_AutoReg_mpv;
    TextView tv_AutoReg_txt_service;
    TextView tv_AutoReg_progress;

    ImageView img_AutoReg_v2;
    ImageView img_AutoReg_v3;
    ImageView img_AutoReg_v4;
    ImageView img_AutoReg_v1;
    ImageView img_AutoReg_service;

    LinearLayout ll_AutoReg_auto;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    private String ipAdr;
    private String rack;
    private String slot;

    private ReadS7Regulation readS7;
    private WriteS7Regulation writeS7;

    private Vibrator vib;

    private Vibration vibb;

    private int nivLiq;

    private String login;
    private String droit;

    SharedPreferences pref_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate__regulation);

        nivLiq=0;

        ll_AutoReg_auto = (LinearLayout) findViewById(R.id.ll_AutoReg_auto);

        pb_AutoReg_nivLiq = (ProgressBar) findViewById(R.id.pb_AutoReg_nivLiq);

        bt_autoReg_retourAccueil = (Button) findViewById(R.id.bt_autoReg_retourAccueil);
        bt_autoReg_retourChxAuto = (Button) findViewById(R.id.bt_autoReg_retourChxAuto);
        bt_autoReg_ro = (Button) findViewById(R.id.bt_autoReg_ro);
        bt_autoReg_v2 = (Button) findViewById(R.id.bt_autoReg_v2);
        bt_autoReg_v3 = (Button) findViewById(R.id.bt_autoReg_v3);
        bt_autoReg_v4 = (Button) findViewById(R.id.bt_autoReg_v4);
        bt_autoReg_v1 = (Button) findViewById(R.id.bt_autoReg_v1);
        bt_autoReg_valEcriture = (Button) findViewById(R.id.bt_autoReg_valEcriture);

        tv_AutoReg_txtIp = (TextView) findViewById(R.id.tv_AutoReg_txtIp);
        tv_AutoReg_txtRack = (TextView) findViewById(R.id.tv_AutoReg_txtRack);
        tv_AutoReg_txtSlot = (TextView) findViewById(R.id.tv_AutoReg_txtSlot);
        tv_AutoReg_SP = (TextView) findViewById(R.id.tv_AutoReg_SP);
        tv_AutoReg_manuel = (TextView) findViewById(R.id.tv_AutoReg_manuel);
        tv_AutoReg_mpv = (TextView) findViewById(R.id.tv_AutoReg_mpv);
        tv_AutoReg_txt_service = (TextView) findViewById(R.id.tv_AutoReg_txt_service);
        tv_AutoReg_progress = (TextView) findViewById(R.id.tv_AutoReg_progress);

        img_AutoReg_v2 = (ImageView) findViewById(R.id.img_AutoReg_v2);
        img_AutoReg_v3 = (ImageView) findViewById(R.id.img_AutoReg_v3);
        img_AutoReg_v4 = (ImageView) findViewById(R.id.img_AutoReg_v4);
        img_AutoReg_v1 = (ImageView) findViewById(R.id.img_AutoReg_v1);
        img_AutoReg_service = (ImageView) findViewById(R.id.img_AutoReg_service);

        connexStatus=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        network = connexStatus.getActiveNetworkInfo();

        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        vib= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibb=new Vibration(vib, builder);

        try{
            FileInputStream ins=openFileInput("autom2.txt");
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

        tv_AutoReg_txtIp.setText(tv_AutoReg_txtIp.getText() + " " + ipAdr);
        tv_AutoReg_txtRack.setText(tv_AutoReg_txtRack.getText() + " " + rack);
        tv_AutoReg_txtSlot.setText(tv_AutoReg_txtSlot.getText() + " " + slot);

        pref_data = PreferenceManager.getDefaultSharedPreferences(getApplication());
        login=pref_data.getString("login","NULL");
        droit=pref_data.getString("droit","NULL");

        Log.i("-------------> test","test"+ droit);
    }

    public void onAutoRegClickManager(View v){
        switch(v.getId()){
            case R.id.bt_autoReg_retourChxAuto:

                if(bt_autoReg_ro.getText().equals("Se déconnecter")){

                    readS7.Stop();
                    vibb.stop();
                    writeS7.Stop();
                    try{
                        Thread.sleep(500);
                    }
                    catch(Exception e){
                        Log.i("Automate_Comprime","test");
                        e.printStackTrace();
                    }

                }
                Intent intent = new Intent(this,Connection.class);
                startActivity(intent);
                break;
            case R.id.bt_autoReg_ro:
                Log.i("test co",ipAdr + " "+ rack + " "+ slot);
                if(network !=null && network.isConnectedOrConnecting()){
                    if(bt_autoReg_ro.getText().equals("Lire automate")){

                        ll_AutoReg_auto.setVisibility(View.VISIBLE);

                        if(droit.equals("RO")){
                            bt_autoReg_v1.setVisibility(View.GONE);
                            bt_autoReg_v2.setVisibility(View.GONE);
                            bt_autoReg_v3.setVisibility(View.GONE);
                            bt_autoReg_v4.setVisibility(View.GONE);
                            bt_autoReg_valEcriture.setVisibility(View.GONE);
                        }
                        else{
                            bt_autoReg_v1.setVisibility(View.VISIBLE);
                            bt_autoReg_v2.setVisibility(View.VISIBLE);
                            bt_autoReg_v3.setVisibility(View.VISIBLE);
                            bt_autoReg_v4.setVisibility(View.VISIBLE);
                            bt_autoReg_valEcriture.setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(this,network.getTypeName(),Toast.LENGTH_LONG).show();
                        bt_autoReg_ro.setText("Se déconnecter");

                        readS7=new ReadS7Regulation(this,v);

                        readS7.Start(ipAdr,rack,slot);

                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        writeS7=new WriteS7Regulation();
                        writeS7.Start(ipAdr,rack,slot);
                    }
                    else{

                        readS7.Stop();
                        vibb.stop();

                        if(!droit.equals("RO")){
                            writeS7.Stop();
                        }
                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){
                            Log.i("Automate_Comprime","test");
                            e.printStackTrace();
                        }

                        bt_autoReg_ro.setText("Lire automate");

                        String uri = "@android:drawable/presence_offline";
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        img_AutoReg_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

                        tv_AutoReg_txt_service.setText("Application non connectée à l'automate");

                        Toast.makeText(this,"Le traitement a été interrompu par l'utilisateur",Toast.LENGTH_LONG).show();
                        ll_AutoReg_auto.setVisibility(View.GONE);

                    }
                }
                else{
                    Toast.makeText(this,"Connexion au réseau impossible",Toast.LENGTH_LONG).show();
                    problemeCo();
                    ll_AutoReg_auto.setVisibility(View.GONE);
                }
                break;
            case R.id.bt_autoReg_valEcriture:
                //writeS7.setWriteInt(pb_AutoReg_nivLiq.getProgress(),0); nivLiq
                if(!droit.equals("RO")){
                    writeS7.setWriteInt(nivLiq,0);
                    writeS7.setWriteInt(Integer.parseInt(tv_AutoReg_SP.getText().toString()),2);
                    writeS7.setWriteInt(Integer.parseInt(tv_AutoReg_manuel.getText().toString()),4);
                    writeS7.setWriteInt(Integer.parseInt(tv_AutoReg_mpv.getText().toString()),6);
                }

                break;
            case R.id.bt_autoReg_v1:
                if(!droit.equals("RO")){
                    writeS7.setWriteBool(0,2, bt_autoReg_v1.getText().toString()=="Ouvrir la valve 1" ? 0 : 1);
                }

                break;
            case R.id.bt_autoReg_v2:
                if(!droit.equals("RO")){
                    writeS7.setWriteBool(0,4, bt_autoReg_v2.getText().toString()=="Ouvrir la valve 2" ? 0 : 1);
                }

                break;
            case R.id.bt_autoReg_v3:
                if(!droit.equals("RO")){
                    writeS7.setWriteBool(0,8, bt_autoReg_v3.getText().toString()=="Ouvrir la valve 3" ? 0 : 1);
                }

                break;
            case R.id.bt_autoReg_v4:
                if(!droit.equals("RO")){
                    writeS7.setWriteBool(0,16, bt_autoReg_v4.getText().toString()=="Ouvrir la valve 4" ? 0 : 1);
                }

                break;
        }
    }

    public void problemeCo(){
        String uri = "@android:drawable/presence_busy";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoReg_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        tv_AutoReg_txt_service.setText("Problème de connexion");

        readS7.Stop();

        if(!droit.equals("RO")){
            writeS7.Stop();
        }


        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            Log.i("Automate_Comprime","test");
            e.printStackTrace();
        }

        bt_autoReg_ro.setText("Lire automate");

        Toast.makeText(this,"Le traitement a été interrompu car l'application n'a pas pu se connecter à l'automate",Toast.LENGTH_LONG).show();
    }

    public void lectureAutomate(int[]donnees){

        int d1=donnees[0];
        int d2=donnees[1];
        int d3=donnees[2];
        int d4=donnees[3];
        int d5=donnees[4];

        //Log.i("Automate_Comprime ","1 :" +String.valueOf(d1));

        String uri = "@android:drawable/presence_online";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoReg_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        tv_AutoReg_txt_service.setText("Automate en service");

        uri = ((0x0200 & d1) ==512 ? "@android:drawable/presence_offline" : "@android:drawable/presence_online");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoReg_v1.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        uri = ((0x0400 & d1) ==1024 ? "@android:drawable/presence_offline" : "@android:drawable/presence_online");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoReg_v2.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        uri = ((0x0800 & d1) ==2048 ? "@android:drawable/presence_offline" : "@android:drawable/presence_online");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoReg_v3.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        uri = ((0x1000 & d1) ==4096 ? "@android:drawable/presence_offline" : "@android:drawable/presence_online");
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoReg_v4.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        bt_autoReg_v1.setText((0x0200 & d1) ==512 ? "Ouvrir la valve 1" : "Fermer la valve 1");
        bt_autoReg_v2.setText((0x0400 & d1) ==1024 ? "Ouvrir la valve 2" : "Fermer la valve 2");
        bt_autoReg_v3.setText((0x0800 & d1) ==2048 ? "Ouvrir la valve 3" : "Fermer la valve 3");
        bt_autoReg_v4.setText((0x1000 & d1) ==4096 ? "Ouvrir la valve 4" : "Fermer la valve 4");

        double litreCuve= (double)d2/1000*30;

        //tv_AutoReg_progress.setText("Niveau du liquide : " + String.valueOf(((d2/10)/100)*30) + "L");

        tv_AutoReg_progress.setText("Niveau du liquide : " + String.valueOf(litreCuve) + "L");

        Log.i("verif math", String.valueOf(d2)+ " et " + String.valueOf(litreCuve));

        pb_AutoReg_nivLiq.setProgress(d2/10);
        nivLiq=d2;

        if(d2/10>=100){
            vibb.Start();
        }
        else if (d2/10<100){
            vibb.stop();
        }

        tv_AutoReg_SP.setText(String.valueOf(d3));

        tv_AutoReg_manuel.setText(String.valueOf(d4));

        tv_AutoReg_mpv.setText(String.valueOf(d5));

    }
}
