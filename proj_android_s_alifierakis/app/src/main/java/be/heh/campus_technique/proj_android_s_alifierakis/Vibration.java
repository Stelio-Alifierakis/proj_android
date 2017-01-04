package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by steli on 04-01-17.
 */

public class Vibration {
    private static  final int MESSAGE_PRE_EXECUTE=1;
    private static  final int MESSAGE_PROGRESS_UPDATE=2;
    private static  final int MESSAGE_POST_EXECUTE=3;

    private Vibrator vib;
    private AlertDialog.Builder alerte;

    private AtomicBoolean isRunning= new AtomicBoolean(false);
    private int single=0;

    public Vibration(Vibrator v, AlertDialog.Builder a){
        vib=v;
        alerte=a;
    }

    public void Start() {
        if (!monThread.isAlive()){
            monThread.start();
            isRunning.set(true);
        }
    }

    public  void stop() {
        isRunning.set(false);
        monThread.interrupt();
    }

    private void downloadPreExecute(){

    }

    private void downlodOnProgressUpdate(int i){
        Log.i("test vibration",String.valueOf(i));
        if(i==1){
            alerte.setTitle("Alerte niveau").setMessage("Niveau trop haut !").setNeutralButton("Résoudre le problème", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            }).create().show();
            vib.vibrate(10000);
        }
        else {
            vib.cancel();
        }
    }

    private void downloadOnPostExecute(){
        single=0;
    }

    private Handler monHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadPreExecute();
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downlodOnProgressUpdate(msg.arg1);
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                default:
                    break;
            }
        }
    };

    private Thread monThread=new Thread(new Runnable(){
        @Override
        public void run(){
            try{
                sendPreExecute();
                while(isRunning.get()){
                    sendProgressMessage(single++);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
                sendPostExecute();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    });

    private void sendPreExecute(){
        Message preExecuteMsg=new Message();
        preExecuteMsg.what = MESSAGE_PRE_EXECUTE;
        monHandler.sendMessage(preExecuteMsg);
    }

    private void sendPostExecute(){
        Message postExecuteMsg=new Message();
        postExecuteMsg.what = MESSAGE_POST_EXECUTE;
        monHandler.sendMessage(postExecuteMsg);
    }

    private void sendProgressMessage(int i){
        Message progressMsg=new Message();
        progressMsg.what=MESSAGE_PROGRESS_UPDATE;
        progressMsg.arg1 = i;
        monHandler.sendMessage(progressMsg);
    }
}
