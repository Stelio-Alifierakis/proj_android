package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Connection extends Activity {

    private Button bt_connection_text_co1;
    private Button bt_connection_text_lancerAutomate1;
    private Button bt_connection_text_co2;
    private Button bt_connection_text_lancerAutomate2;

    private LinearLayout ll_connection_coAutomate1;
    private LinearLayout ll_connection_coAutomate2;

    private EditText et_connection_ipAUtomate1;
    private EditText et_connection_rackAUtomate1;
    private EditText et_connection_slotAUtomate1;
    private EditText et_connection_ipAUtomate2;
    private EditText et_connection_rackAUtomate2;
    private EditText et_connection_slotAUtomate2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        bt_connection_text_co1=(Button) findViewById(R.id.bt_connection_text_co1);
        bt_connection_text_lancerAutomate1=(Button) findViewById(R.id.bt_connection_text_lancerAutomate1);
        bt_connection_text_co2=(Button) findViewById(R.id.bt_connection_text_co2);
        bt_connection_text_lancerAutomate2=(Button) findViewById(R.id.bt_connection_text_lancerAutomate2);

        ll_connection_coAutomate1=(LinearLayout) findViewById(R.id.ll_connection_coAutomate1);
        ll_connection_coAutomate2=(LinearLayout) findViewById(R.id.ll_connection_coAutomate2);

        et_connection_ipAUtomate1=(EditText) findViewById(R.id.et_connection_ipAUtomate1);
        et_connection_rackAUtomate1=(EditText) findViewById(R.id.et_connection_rackAUtomate1);
        et_connection_slotAUtomate1=(EditText) findViewById(R.id.et_connection_slotAUtomate1);
        et_connection_ipAUtomate2=(EditText) findViewById(R.id.et_connection_ipAUtomate2);
        et_connection_rackAUtomate2=(EditText) findViewById(R.id.et_connection_rackAUtomate2);
        et_connection_slotAUtomate2=(EditText) findViewById(R.id.et_connection_slotAUtomate2);
    }

    public void onConnexionClickManager(View v){
        switch(v.getId()){
            case R.id.bt_connection_text_co1 :
                ll_connection_coAutomate1.setVisibility(View.VISIBLE);
                ll_connection_coAutomate2.setVisibility(View.GONE);
                break;
            case R.id.bt_connection_text_co2 :
                ll_connection_coAutomate2.setVisibility(View.VISIBLE);
                ll_connection_coAutomate1.setVisibility(View.GONE);
                break;
        }
    }
}
