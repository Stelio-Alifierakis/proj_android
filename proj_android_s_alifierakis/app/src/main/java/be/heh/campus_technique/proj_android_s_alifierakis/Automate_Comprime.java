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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    Button bt_autoCond_envoiRWDB;
    Button bt_autoCond_rw;

    RadioButton rb_AutoCompr_5compr;
    RadioButton rb_AutoCompr_10compr;
    RadioButton rb_AutoCompr_15compr;

    TextView tv_AutoCompr_txt_nbBoutAffich;
    TextView tv_AutoCompr_txt_nbreComprAffich;
    TextView tv_AutoCompr_txt_service;
    TextView tv_AutoCompr_txtIp;
    TextView tv_AutoCompr_txtRack;
    TextView tv_AutoCompr_txtSlot;

    EditText et_AutoCompr_numDB;
    EditText et_AutoCompr_numDBCompr;
    EditText et_AutoCompr_numDB5Compr;
    EditText et_AutoCompr_numDB10Compr;
    EditText et_AutoCompr_numDB15Compr;
    EditText et_AutoCompr_numDBArriveeFlacon;
    EditText et_AutoCompr_numDBArriveeFlaconbit;
    EditText et_AutoCompr_numDBnbreComprAffich;
    EditText et_AutoCompr_numDBResetCompt;
    EditText et_AutoCompr_numDBnbreComprAffichbit;
    EditText et_AutoCompr_numComprVraiByte;

    LinearLayout ll_AutoCompr_service;

    RelativeLayout rl_ecritBits;

    private NetworkInfo network;
    private ConnectivityManager connexStatus;

    private ReadS7Conditionnement readS7;
    private WriteS7Conditionnement writeS7;

    private String ipAdr;
    private String rack;
    private String slot;

    SharedPreferences pref_data;

    private String login;
    private String droit;

    private ArrayList<Integer> tabBit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate__comprime);

        ll_AutoCompr_service = (LinearLayout) findViewById(R.id.ll_AutoCompr_service);

        rl_ecritBits = (RelativeLayout) findViewById(R.id.rl_ecritBits);

        img_AutoCompr_arriveeFlacon = (ImageView) findViewById(R.id.img_AutoCompr_arriveeFlacon);
        img_AutoCompr_moteur = (ImageView) findViewById(R.id.img_AutoCompr_moteur);
        img_AutoCompr_service = (ImageView) findViewById(R.id.img_AutoCompr_service);

        bt_autoCond_ro = (Button) findViewById(R.id.bt_autoCond_ro);
        bt_autoCond_arriveeFlacon = (Button) findViewById(R.id.bt_autoCond_arriveeFlacon);
        bt_autoCond_selecteur = (Button) findViewById(R.id.bt_autoCond_selecteur);
        bt_autoCond_changeNbCompr = (Button) findViewById(R.id.bt_autoCond_changeNbCompr);
        bt_autoCond_resetCompteur = (Button) findViewById(R.id.bt_autoCond_resetCompteur);
        bt_autoCond_retourChxAuto = (Button) findViewById(R.id.bt_autoCond_retourChxAuto);
        bt_autoCond_envoiRWDB = (Button) findViewById(R.id.bt_autoCond_envoiRWDB);
        bt_autoCond_rw = (Button) findViewById(R.id.bt_autoCond_rw);

        et_AutoCompr_numDB=(EditText) findViewById(R.id.et_AutoCompr_numDB);
        et_AutoCompr_numDBCompr=(EditText) findViewById(R.id.et_AutoCompr_numDBCompr);
        et_AutoCompr_numDB5Compr=(EditText) findViewById(R.id.et_AutoCompr_numDB5Compr);
        et_AutoCompr_numDB10Compr=(EditText) findViewById(R.id.et_AutoCompr_numDB10Compr);
        et_AutoCompr_numDB15Compr=(EditText) findViewById(R.id.et_AutoCompr_numDB15Compr);
        et_AutoCompr_numDBArriveeFlacon=(EditText) findViewById(R.id.et_AutoCompr_numDBArriveeFlacon);
        et_AutoCompr_numDBArriveeFlaconbit=(EditText) findViewById(R.id.et_AutoCompr_numDBArriveeFlaconbit);
        et_AutoCompr_numDBnbreComprAffich=(EditText) findViewById(R.id.et_AutoCompr_numDBnbreComprAffich);
        et_AutoCompr_numDBResetCompt=(EditText) findViewById(R.id.et_AutoCompr_numDBResetCompt);
        et_AutoCompr_numDBnbreComprAffichbit=(EditText) findViewById(R.id.et_AutoCompr_numDBnbreComprAffichbit);
        et_AutoCompr_numComprVraiByte=(EditText) findViewById(R.id.et_AutoCompr_numComprVraiByte);

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

        pref_data = PreferenceManager.getDefaultSharedPreferences(getApplication());
        login=pref_data.getString("login","NULL");
        droit=pref_data.getString("droit","NULL");

        tv_AutoCompr_txtIp.setText(tv_AutoCompr_txtIp.getText() + " " + ipAdr);
        tv_AutoCompr_txtRack.setText(tv_AutoCompr_txtRack.getText() + " " + rack);
        tv_AutoCompr_txtSlot.setText(tv_AutoCompr_txtSlot.getText() + " " + slot);

        btVisibility();

        try{
            FileInputStream ins=openFileInput("RWautom1.txt");
            BufferedReader reader=new BufferedReader(new InputStreamReader(ins));
            StringBuilder out=new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null){
                out.append(line);
            }

            reader.close();
            ins.close();

            String[] confs=out.toString().split("#");
            int i=0;
            for(String a : confs){

                //tabBit[i]=Integer.parseInt(a);

                tabBit.add(Integer.parseInt(a));
                Log.i("Valeur " + String.valueOf(i),String.valueOf(tabBit.get(i)));
                i++;
            }

        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        et_AutoCompr_numDB.setText(String.valueOf(tabBit.get(0)));
        et_AutoCompr_numDBCompr.setText(String.valueOf(tabBit.get(1)));
        et_AutoCompr_numDB5Compr.setText(String.valueOf(tabBit.get(2)));
        et_AutoCompr_numDB10Compr.setText(String.valueOf(tabBit.get(3)));
        et_AutoCompr_numDB15Compr.setText(String.valueOf(tabBit.get(4)));
        et_AutoCompr_numDBArriveeFlacon.setText(String.valueOf(tabBit.get(5)));
        et_AutoCompr_numDBArriveeFlaconbit.setText(String.valueOf(tabBit.get(6)));
        et_AutoCompr_numDBnbreComprAffich.setText(String.valueOf(tabBit.get(7)));
        et_AutoCompr_numDBnbreComprAffichbit.setText(String.valueOf(tabBit.get(8)));
        et_AutoCompr_numComprVraiByte.setText(String.valueOf(tabBit.get(9)));
        et_AutoCompr_numDBResetCompt.setText(String.valueOf(tabBit.get(10)));
    }

    public void onAutoCondClickManager(View v){
        switch(v.getId()){
            case R.id.bt_autoCond_retourChxAuto:

                if(bt_autoCond_ro.getText().equals("Se déconnecter")){
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
                }

                Intent intent = new Intent(this,Connection.class);
                startActivity(intent);
                break;
            case R.id.bt_autoCond_rw:
                if(bt_autoCond_ro.getText().equals("Se déconnecter")){
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

                    bt_autoCond_ro.setText("Lire automate");
                }
                ll_AutoCompr_service.setVisibility(View.GONE);
                rl_ecritBits.setVisibility(View.VISIBLE);

                break;
            case R.id.bt_autoCond_envoiRWDB:

                if(et_AutoCompr_numDB.getText().toString().equals("") || et_AutoCompr_numComprVraiByte.getText().toString().equals("") || et_AutoCompr_numDBnbreComprAffichbit.getText().toString().equals("") || et_AutoCompr_numDBCompr.getText().toString().equals("") || et_AutoCompr_numDB5Compr.getText().toString().equals("") || et_AutoCompr_numDB10Compr.getText().toString().equals("") || et_AutoCompr_numDB15Compr.getText().toString().equals("") || et_AutoCompr_numDBArriveeFlacon.getText().toString().equals("") || et_AutoCompr_numDBArriveeFlaconbit.getText().toString().equals("") || et_AutoCompr_numDBnbreComprAffich.getText().toString().equals("") || et_AutoCompr_numDBResetCompt.getText().toString().equals("")){
                    Toast.makeText(this,"Tous les champs doivent être remplis",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDB.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDB.getText().toString())>50){
                    Toast.makeText(this,"Le champs numéro DB n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDBCompr.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDBCompr.getText().toString())>36){
                    Toast.makeText(this,"Le champs numéro de byte des comprimés n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDB5Compr.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDB5Compr.getText().toString())>8){
                    Toast.makeText(this,"Le champs numéro de bit pour 5 comprimés n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDB10Compr.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDB10Compr.getText().toString())>8){
                    Toast.makeText(this,"Le champs numéro de bit pour 10 comprimés n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDB15Compr.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDB15Compr.getText().toString())>8){
                    Toast.makeText(this,"Le champs numéro de bit pour 15 comprimés n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDBArriveeFlacon.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDBArriveeFlacon.getText().toString())>36 || Integer.parseInt(et_AutoCompr_numDBArriveeFlaconbit.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDBArriveeFlaconbit.getText().toString())>8){
                    Toast.makeText(this,"Le champs de byte ou de bit d'arrivée des flacons ne sont pas bons",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDBnbreComprAffich.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDBnbreComprAffich.getText().toString())>36 || Integer.parseInt(et_AutoCompr_numDBnbreComprAffichbit.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDBnbreComprAffichbit.getText().toString())>8){
                    Toast.makeText(this,"Le champs numéro de byte ou numéro de bit pour le sélecteur de comprimés n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numDBResetCompt.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numDBResetCompt.getText().toString())>36){
                    Toast.makeText(this,"Le champs du positionnement du mot n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(et_AutoCompr_numComprVraiByte.getText().toString())<0 || Integer.parseInt(et_AutoCompr_numComprVraiByte.getText().toString())>36){
                    Toast.makeText(this,"Le champs du numéro de byte du nombre de comprimé n'est pas bon",Toast.LENGTH_LONG).show();
                }
                else{
                    String str=et_AutoCompr_numDB.getText().toString() + "#" +
                            et_AutoCompr_numDBCompr.getText().toString() + "#" +
                            et_AutoCompr_numDB5Compr.getText().toString() + "#" +
                            et_AutoCompr_numDB10Compr.getText().toString() + "#" +
                            et_AutoCompr_numDB15Compr.getText().toString() + "#" +
                            et_AutoCompr_numDBArriveeFlacon.getText().toString() + "#" +
                            et_AutoCompr_numDBArriveeFlaconbit.getText().toString() + "#" +
                            et_AutoCompr_numDBnbreComprAffich.getText().toString() + "#" +
                            et_AutoCompr_numDBnbreComprAffichbit.getText().toString() + "#" +
                            et_AutoCompr_numComprVraiByte.getText().toString() + "#" +
                            et_AutoCompr_numDBResetCompt.getText().toString();
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

                    Toast.makeText(this,"Changements pris en compte",Toast.LENGTH_LONG).show();
                    rl_ecritBits.setVisibility(View.GONE);
                }

                break;
            case R.id.bt_autoCond_ro:

                rl_ecritBits.setVisibility(View.GONE);

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

                        if(!droit.equals("RO")){
                            writeS7=new WriteS7Conditionnement(tabBit.get(0),tabBit.get(10));
                            //writeS7.Start("192.168.10.220","0","2");
                            //writeS7.Start("192.168.0.220","0","2");
                            writeS7.Start(ipAdr,rack,slot);
                        }
                    }
                    else{
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
            case R.id.rb_AutoCompr_10compr:
            case R.id.rb_AutoCompr_15compr:
                checkCompr();
                break;
            case R.id.bt_autoCond_selecteur:
                if(!droit.equals("RO")){
                    int bit=(int)Math.pow(2,tabBit.get(6));
                    Log.i("------------>Bit", String.valueOf(bit));
                    writeS7.setWriteBool(tabBit.get(5),bit, bt_autoCond_selecteur.getText().toString()=="Activer sélecteur" ? 1 : 0);
                }
                break;
            case R.id.bt_autoCond_arriveeFlacon:
                if(!droit.equals("RO")){
                    int bit=(int)Math.pow(2,tabBit.get(8));
                    Log.i("------------>Bit", String.valueOf(bit));
                    writeS7.setWriteBool(tabBit.get(7),bit, bt_autoCond_arriveeFlacon.getText()=="Activer arrivée des flacons" ? 1 : 0);
                }
                break;
            case R.id.bt_autoCond_changeNbCompr:
                if(!droit.equals("RO")){
                    writeS7.setWriteByte(tabBit.get(9),Integer.parseInt(tv_AutoCompr_txt_nbreComprAffich.getText().toString()));
                }
                break;
            case R.id.bt_autoCond_resetCompteur:
                if(!droit.equals("RO")){
                    writeS7.setWriteInt(0);
                }
                break;
        }
    }

    private void checkCompr(){
        if(!droit.equals("RO")){
            int bit1=(int)Math.pow(2,tabBit.get(2));
            int bit2=(int)Math.pow(2,tabBit.get(3));
            int bit3=(int)Math.pow(2,tabBit.get(4));

            Log.i("------------>Bits", String.valueOf(bit1) + " et " + String.valueOf(bit2) + " et " + String.valueOf(bit3));

            writeS7.setWriteBool(tabBit.get(1),bit1, rb_AutoCompr_5compr.isChecked() ? 1 : 0);
            writeS7.setWriteBool(tabBit.get(1),bit2, rb_AutoCompr_10compr.isChecked() ? 1 : 0);
            writeS7.setWriteBool(tabBit.get(1),bit3, rb_AutoCompr_15compr.isChecked() ? 1 : 0);
        }
    }

    private void btVisibility(){
        if(droit.equals("RO")){
            bt_autoCond_arriveeFlacon.setVisibility(View.GONE);
            bt_autoCond_changeNbCompr.setVisibility(View.GONE);
            bt_autoCond_selecteur.setVisibility(View.GONE);
            bt_autoCond_resetCompteur.setVisibility(View.GONE);
            bt_autoCond_envoiRWDB.setVisibility(View.GONE);
            bt_autoCond_rw.setVisibility(View.GONE);
            Log.i("------------>Droit", "1 " + droit);
        }
        else if(droit.equals("FC")){
            bt_autoCond_arriveeFlacon.setVisibility(View.VISIBLE);
            bt_autoCond_changeNbCompr.setVisibility(View.VISIBLE);
            bt_autoCond_selecteur.setVisibility(View.VISIBLE);
            bt_autoCond_resetCompteur.setVisibility(View.VISIBLE);
            bt_autoCond_envoiRWDB.setVisibility(View.VISIBLE);
            bt_autoCond_rw.setVisibility(View.VISIBLE);
            Log.i("------------>Droit", "2 " + droit);
        }
        else{
            bt_autoCond_arriveeFlacon.setVisibility(View.VISIBLE);
            bt_autoCond_changeNbCompr.setVisibility(View.VISIBLE);
            bt_autoCond_selecteur.setVisibility(View.VISIBLE);
            bt_autoCond_resetCompteur.setVisibility(View.VISIBLE);
            bt_autoCond_envoiRWDB.setVisibility(View.VISIBLE);
            bt_autoCond_rw.setVisibility(View.GONE);
            Log.i("------------>Droit", "3 " + droit);
        }
    }

    public void problemeCo(){
        String uri = "@android:drawable/presence_busy";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        img_AutoCompr_service.setImageDrawable(getResources().getDrawable(imageResource,this.getTheme()));

        tv_AutoCompr_txt_service.setText("Problème de connexion");

        readS7.Stop();
        if(droit!="RO"){
            writeS7.Stop();
        }

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
