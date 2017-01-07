package be.heh.campus_technique.proj_android_s_alifierakis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.heh.campus_technique.proj_android_s_alifierakis.BDD.User;
import be.heh.campus_technique.proj_android_s_alifierakis.BDD.UserAccessBDD;

public class listeUser extends Activity {

    TextView tv_listeUser_data;
    ListView tv_listeUser_liste;
    Button bt_listeUser_comeBack;
    Button bt_listeUser_addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_user);

        tv_listeUser_data=(TextView)findViewById(R.id.tv_listeUser_data);
        tv_listeUser_data.setText("Contenu du fichier texte : \n");

        tv_listeUser_liste=(ListView)findViewById(R.id.tv_listeUser_liste);

        bt_listeUser_comeBack=(Button)findViewById(R.id.bt_listeUser_comeBack);
        bt_listeUser_addUser=(Button)findViewById(R.id.bt_listeUser_addUser);

        UserAccessBDD userBD =new UserAccessBDD(this);

        userBD.openForRead();
        ArrayList<User> tabUser=userBD.getAllUsers();
        userBD.close();

        ArrayAdapter<User> adapter=new ArrayAdapter<User>(this,android.R.layout.simple_list_item_1,tabUser);
        tv_listeUser_liste.setAdapter(adapter);
    }

    public void onListeUserClickManager(View v){

        switch(v.getId()){

            case R.id.bt_listeUser_comeBack :
                Intent intent2 = new Intent(this,Connection.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.bt_listeUser_addUser :
                Intent intent = new Intent(this,AddUser.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
