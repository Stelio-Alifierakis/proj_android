package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.security.MessageDigest;

import be.heh.campus_technique.proj_android_s_alifierakis.BDD.User;
import be.heh.campus_technique.proj_android_s_alifierakis.BDD.UserAccessBDD;

public class AddUser extends Activity {

    Button bt_AddUser_add;
    Button bt_AddUser_annulation;

    TextView tv_AddUser_titre;
    TextView tv_AddUser_login;
    TextView tv_AddUser_password;
    TextView tv_AddUser_droit;

    EditText et_AddUser_login;
    EditText et_AddUser_pwd;

    RadioButton rb_AddUser_ro;
    RadioButton rb_AddUser_rw;
    RadioButton rb_AddUser_fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        bt_AddUser_add=(Button) findViewById(R.id.bt_AddUser_add);
        bt_AddUser_annulation=(Button) findViewById(R.id.bt_AddUser_annulation);

        tv_AddUser_titre=(TextView) findViewById(R.id.tv_AddUser_titre);
        tv_AddUser_login=(TextView) findViewById(R.id.tv_AddUser_login);
        tv_AddUser_password=(TextView) findViewById(R.id.tv_AddUser_password);
        tv_AddUser_droit=(TextView) findViewById(R.id.tv_AddUser_droit);

        et_AddUser_login=(EditText) findViewById(R.id.et_AddUser_login);
        et_AddUser_pwd=(EditText) findViewById(R.id.et_AddUser_pwd);


        rb_AddUser_ro=(RadioButton) findViewById(R.id.rb_AddUser_ro);
        rb_AddUser_rw=(RadioButton) findViewById(R.id.rb_AddUser_rw);
        rb_AddUser_fc=(RadioButton) findViewById(R.id.rb_AddUser_fc);
    }

    public void onAddUserClickManager(View v){

        switch(v.getId()){
            case R.id.bt_AddUser_add :
                String droit= (rb_AddUser_ro.isChecked()? "RO": (rb_AddUser_rw.isChecked()? "RW" : (rb_AddUser_fc.isChecked()? "FC" : "RO") ) );

                Hash h=new Hash();

                String hashCdc=h.hashage(et_AddUser_pwd.getText().toString(),"SHA-1");

                /*String hashCdc="";
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA-1");
                    md.update(et_AddUser_pwd.getText().toString().getBytes());
                    byte[] hashByte=md.digest();

                    char[] hexArray = "0123456789ABCDEF".toCharArray();

                    StringBuffer buffer = new StringBuffer();
                    for (int j = 0; j < hashByte.length; j++) {
                        buffer.append(hexArray[(hashByte[j] >> 4) & 0x0f]);
                        buffer.append(hexArray[hashByte[j] & 0x0f]);
                    }

                    hashCdc=buffer.toString();

                }
                catch (Exception e){
                    e.printStackTrace();
                }*/

                try{
                    User user1=new User(et_AddUser_login.getText().toString(),hashCdc,droit);
                    UserAccessBDD userBD=new UserAccessBDD(this);
                    userBD.openForWrite();
                    userBD.insertUser(user1);
                    userBD.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                Intent intent = new Intent(this,listeUser.class);
                startActivity(intent);
                finish();

                break;
            case R.id.bt_AddUser_annulation :
                Intent intent2 = new Intent(this,listeUser.class);
                startActivity(intent2);
                finish();
                break;
        }

    }
}
