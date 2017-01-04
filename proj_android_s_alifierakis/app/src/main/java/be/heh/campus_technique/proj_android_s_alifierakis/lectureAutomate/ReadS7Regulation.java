package be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import be.heh.campus_technique.proj_android_s_alifierakis.Automate_Comprime;
import be.heh.campus_technique.proj_android_s_alifierakis.Automate_Regulation;
import simaticS7.S7;
import simaticS7.S7Client;
import simaticS7.S7OrderCode;

/**
 * Created by steli on 04-01-17.
 */

public class ReadS7Regulation {

    private static final int MESSAGE_PRE_EXECUTE=1;
    private static final int MESSAGE_PROGRESS_UPDATE=2;
    private static final int MESSAGE_POST_EXECUTE=3;
    private static final int MESSAGE_ERROR_EXECUTE=4;

    private AtomicBoolean isRunning=new AtomicBoolean(false);
    private  boolean running = false;

    private AutomateS7 plcS7;
    private Thread readThread;
    private S7Client comS7;
    private String[] param=new String[10];
    private byte[] datasPLC=new byte[512];

    private View vi_main_ui;

    private ArrayList<Automate_Regulation> automates7=new ArrayList<>();

    public ReadS7Regulation(Automate_Regulation a, View v){
        vi_main_ui=v;

        comS7=new S7Client();
        plcS7=new AutomateS7();

        automates7.add(a);
        readThread=new Thread(plcS7);
    }

    public void Stop(){
        //isRunning.set(false);
        Log.i("Thread : ","Ca a stoppé");
        comS7.Disconnect();
        running=false;
        isRunning.set(false);
        readThread.interrupt();
    }

    public void Start(String a, String r, String s){
        if(!readThread.isAlive()){
            param[0]=a;
            param[1]=r;
            param[2]=s;

            readThread.start();
            running=true;
            isRunning.set(true);
        }
    }

    private void downloadOnPreExecute(int t){
        Toast.makeText(vi_main_ui.getContext(), String.valueOf(t),Toast.LENGTH_LONG).show();
        //tv_main_plc.setText("PLC :" + String.valueOf(t));
    }

    private void downlodOnProgressUpdate(Object progres){
        //pb_main_progressionS7.setProgress(progress);

        //Toast.makeText(vi_main_ui.getContext(), "********" + String.valueOf(progress[0]),Toast.LENGTH_LONG).show();
        int[] progress=(int[])progres;
        for(Automate_Regulation a : automates7){
            a.lectureAutomate(progress);
        }
    }

    private void downloadOnPostExecute(){
        Toast.makeText(vi_main_ui.getContext(),"Le traitement de la tâche de fond est terminé",Toast.LENGTH_LONG).show();
        //tv_main_plc.setText("PLC : /!\\");
    }

    private void errorExecute(){
        for(Automate_Regulation a : automates7){
            a.problemeCo();
        }
    }

    private Handler monHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
                    break;
                case MESSAGE_PROGRESS_UPDATE:
                    downlodOnProgressUpdate(msg.obj);
                    break;
                case MESSAGE_POST_EXECUTE:
                    downloadOnPostExecute();
                    break;
                case MESSAGE_ERROR_EXECUTE:
                    errorExecute();
                    break;
                default:
                    break;
            }
        }
    };

    public boolean getRunning() {
        return running;
    }

    private class AutomateS7 implements  Runnable{

        @Override
        public void run(){

            try{
                comS7.SetConnectionType(S7.S7_BASIC);
                Integer res=comS7.ConnectTo(param[0],Integer.valueOf(param[1]),Integer.valueOf(param[2]));
                S7OrderCode OrderCode=new S7OrderCode();
                Integer result=comS7.GetOrderCode(OrderCode);
                int numCPU=-1;

                if(res.equals(0) && result.equals(0)){
                    numCPU=Integer.valueOf(OrderCode.Code().toString().substring(5,8));
                }
                else numCPU=0;

                sendPreExecuteMessage(numCPU);

                while(isRunning.get()){
                    if(res.equals(0)){
                        int bts = comS7.ReadArea(S7.S7AreaDB,5,0,36,datasPLC);

                        int dataBts=0;
                        int dataBts1=0;
                        int dataBts2=0;
                        int dataBts3=0;
                        int dataBts4=0;

                        dataBts=S7.GetWordAt(datasPLC,0);
                        dataBts1=S7.GetWordAt(datasPLC,16);
                        dataBts2=S7.GetWordAt(datasPLC,18);
                        dataBts3=S7.GetWordAt(datasPLC,20);
                        dataBts4=S7.GetWordAt(datasPLC,22);

                        int[] dataBtss={dataBts,dataBts1,dataBts2,dataBts3,dataBts4};
                        sendProgressMessage(dataBtss);

                        try{
                            Thread.sleep(1000);
                        }
                        catch(Exception e){

                            e.printStackTrace();
                        }
                    }

                    sendPostExecuteMessage();
                }
            }
            catch(Exception e){
                e.printStackTrace();
                ErrorMessage();
            }

        }

        private void sendPreExecuteMessage(int v){
            if(v==-1){

            }
            else{
                Message preExecuteMsg=new Message();
                preExecuteMsg.what=MESSAGE_PRE_EXECUTE;
                preExecuteMsg.arg1=v;
                monHandler.sendMessage(preExecuteMsg);
            }
        }

        private void sendProgressMessage(int[] i){
            Message progressMsg=new Message();
            progressMsg.what=MESSAGE_PROGRESS_UPDATE;

            progressMsg.obj=i;
            monHandler.sendMessage(progressMsg);
        }

        private void sendPostExecuteMessage(){
            Message postExecuteMsg=new Message();
            postExecuteMsg.what=MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }

        private void ErrorMessage(){
            Message errorExecuteMsg=new Message();
            errorExecuteMsg.what=MESSAGE_ERROR_EXECUTE;
            monHandler.sendMessage(errorExecuteMsg);
        }
    }
}
