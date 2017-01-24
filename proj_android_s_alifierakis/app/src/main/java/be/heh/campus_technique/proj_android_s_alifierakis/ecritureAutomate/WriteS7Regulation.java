package be.heh.campus_technique.proj_android_s_alifierakis.ecritureAutomate;

import android.util.Log;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

import simaticS7.S7;
import simaticS7.S7Client;

/**
 * Created by steli on 05-01-17.
 */

public class WriteS7Regulation {
    private AtomicBoolean isRunning=new AtomicBoolean(false);
    private  boolean running = false;

    private View vi_main_ui;

    private AutomateS7 plcS7;
    private Thread writeThread;
    private S7Client comS7;
    private String[] parConnexion=new String[10];
    private byte[] motCommande=new byte[2];
    private byte[] motCommande2=new byte[8];

    public WriteS7Regulation(){
        comS7=new S7Client();
        plcS7=new AutomateS7();
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
                    Integer writePLC=comS7.WriteArea(S7.S7AreaDB,5,2,2,motCommande);
                    writePLC = comS7.WriteArea(S7.S7AreaDB,5,28,8,motCommande2);
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

    }

    public void setWriteInt(int b, int pose){
        S7.SetWordAt(motCommande2,pose,b);
        for(byte bb : motCommande2){
            Log.i("verif byte",b + " " + pose + " " +Byte.toString(bb));
        }

    }

}
