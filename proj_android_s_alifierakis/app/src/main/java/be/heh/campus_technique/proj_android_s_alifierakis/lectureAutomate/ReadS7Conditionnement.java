package be.heh.campus_technique.proj_android_s_alifierakis.lectureAutomate;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.Console;
import java.util.concurrent.atomic.AtomicBoolean;
import simaticS7.S7;
import simaticS7.S7Client;
import simaticS7.S7OrderCode;

/**
 * Created by steli on 16-12-16.
 */

public class ReadS7Conditionnement {
    private static final int MESSAGE_PRE_EXECUTE=1;
    private static final int MESSAGE_PROGRESS_UPDATE=2;
    private static final int MESSAGE_POST_EXECUTE=3;

    private AtomicBoolean isRunning=new AtomicBoolean(false);

    private View vi_main_ui;

    private AutomateS7 plcS7;
    private Thread readThread;
    private S7Client comS7;
    private String[] param=new String[10];
    private byte[] datasPLC=new byte[512];

    public ReadS7Conditionnement(View v){
        vi_main_ui=v;

        comS7=new S7Client();
        plcS7=new AutomateS7();

        readThread=new Thread(plcS7);
    }

    public void Stop(){
        isRunning.set(false);
        comS7.Disconnect();
        readThread.interrupt();
    }

    public void Start(String a, String r, String s){
        if(!readThread.isAlive()){
            param[0]=a;
            param[1]=r;
            param[2]=s;

            readThread.start();
            isRunning.set(true);
        }
    }

    private void downloadOnPreExecute(int t){
        Toast.makeText(vi_main_ui.getContext(), String.valueOf(t),Toast.LENGTH_LONG).show();
        //tv_main_plc.setText("PLC :" + String.valueOf(t));
    }

    private void downlodOnProgressUpdate(int progress){
        //pb_main_progressionS7.setProgress(progress);
    }

    private void downloadOnPostExecute(){
        Toast.makeText(vi_main_ui.getContext(),"Le traitement de la tâche de fond est terminé",Toast.LENGTH_LONG).show();
        //tv_main_plc.setText("PLC : /!\\");
    }

    private Handler monHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MESSAGE_PRE_EXECUTE:
                    downloadOnPreExecute(msg.arg1);
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
                        //int retInfo=comS7.ReadArea(S7.S7AreaDB,5,9,2,datasPLC);
                        int bts = (byte)comS7.ReadArea(S7.S7AreaDB,5,0,2,datasPLC);
                        int dataBts=0;
                        int dataB=0;
                        if(bts==0){
                            dataBts=S7.GetWordAt(datasPLC,0);
                            sendProgressMessage(dataBts);
                        }

                        Log.i("Variable API -> ", String.valueOf(dataBts));
                    }

                    try{
                        Thread.sleep(500);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }
                sendPostExecuteMessage();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        private void sendPreExecuteMessage(int v) {
            Message preExecuteMsg=new Message();
            preExecuteMsg.what=MESSAGE_PRE_EXECUTE;
            preExecuteMsg.arg1=v;
            monHandler.sendMessage(preExecuteMsg);
        }

        private void sendProgressMessage(int i) {
            Message progressMsg=new Message();
            progressMsg.what=MESSAGE_PROGRESS_UPDATE;
            progressMsg.arg1=i;
            monHandler.sendMessage(progressMsg);
        }

        private void sendPostExecuteMessage() {
            Message postExecuteMsg=new Message();
            postExecuteMsg.what=MESSAGE_POST_EXECUTE;
            monHandler.sendMessage(postExecuteMsg);
        }
    }
}