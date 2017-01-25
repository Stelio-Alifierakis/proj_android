package be.heh.campus_technique.proj_android_s_alifierakis.ecritureAutomate;

import android.util.Log;
import android.util.Xml;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

import be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate.ReadS7Conditionnement;
import simaticS7.S7;
import simaticS7.S7Client;

/**
 * Created by steli on 23-12-16.
 */

public class WriteS7Conditionnement {

    private AtomicBoolean isRunning=new AtomicBoolean(false);
    private  boolean running = false;

    private View vi_main_ui;

    private WriteS7Conditionnement.AutomateS7 plcS7;
    private Thread writeThread;
    private S7Client comS7;
    private String[] parConnexion=new String[10];
    private byte[] motCommande=new byte[10];
    private byte[] motCommande2=new byte[2];
    private int numDB;
    private int debutEcritMots;

    public WriteS7Conditionnement(){
        comS7=new S7Client();
        plcS7=new AutomateS7();
        numDB=5;
        debutEcritMots=20;
        writeThread=new Thread(plcS7);
    }

    public WriteS7Conditionnement(int numDB, int debutEcritMots){
        comS7=new S7Client();
        plcS7=new AutomateS7();
        this.numDB=numDB;
        this.debutEcritMots=debutEcritMots;
        writeThread=new Thread(plcS7);
    }

    public void Stop(){
        running=false;
        isRunning.set(false);
        comS7.Disconnect();
        writeThread.interrupt();
    }

    public void Start(String a, String r, String s){
        if(!writeThread.isAlive()){
            parConnexion[0]=a;
            parConnexion[1]=r;
            parConnexion[2]=s;

            writeThread.start();
            running=false;
            isRunning.set(true);
        }
    }

    private class AutomateS7 implements  Runnable{

        @Override
        public void run(){

            try{

                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res=comS7.ConnectTo(parConnexion[0],Integer.valueOf(parConnexion[1]),Integer.valueOf(parConnexion[2]));

                while(isRunning.get() && (res.equals(0))){
                    Integer writePLC=comS7.WriteArea(S7.S7AreaDB,numDB,5,5,motCommande);
                    writePLC = comS7.WriteArea(S7.S7AreaDB,numDB,debutEcritMots,2,motCommande2);

                    /*if(res.equals(0) && writePLC.equals(0)){
                        Log.i("res WRITE : ", String.valueOf(res) + " ********** " + String.valueOf(writePLC));
                    }*/
                }

                try{
                    Thread.sleep(1000);
                }
                catch(Exception e){

                    e.printStackTrace();
                }

            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    public  void setWriteBool(int bytes,int b, int v){

        if(v==1) motCommande[bytes] = (byte) (b | motCommande[bytes]);

        else motCommande[bytes] = (byte) (~b & motCommande[bytes]);

        //String test = new String(motCommande, Charset.forName("UTF-8"));
        //Log.i("---------->"," changement de byte en action " + String.valueOf(bytes) + " " + String.valueOf(b) + " " + String.valueOf(v) + " " + Integer.toHexString(motCommande[bytes]));
        //int i=0;
    }

    public void setWriteByte(int bytes,int b){
        motCommande[bytes] = (byte) b;
       //Log.i("---------->",Integer.toHexString(motCommande[bytes]));
    }

    public void setWriteInt(int b){
        S7.SetWordAt(motCommande2,0,b);
    }

    public boolean getRunning() {
        return running;
    }
}
