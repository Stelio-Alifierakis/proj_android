package be.heh.campus_technique.proj_android_s_alifierakis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GestUser extends AppCompatActivity {

    TextView tv_GestUser_login;

    EditText et_GestUser_ancientMDP;
    EditText et_GestUser_nouveauMDP;

    Button bt_GestUser_comeBack;
    Button bt_GestUser_changerMDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gest_user);

        tv_GestUser_login=(TextView)findViewById(R.id.tv_GestUser_login);

        et_GestUser_ancientMDP=(EditText)findViewById(R.id.et_GestUser_ancientMDP);
        et_GestUser_nouveauMDP=(EditText)findViewById(R.id.et_GestUser_nouveauMDP);

        bt_GestUser_comeBack=(Button)findViewById(R.id.bt_GestUser_comeBack);
        bt_GestUser_changerMDP=(Button)findViewById(R.id.bt_GestUser_changerMDP);
    }
}
