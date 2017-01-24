package be.heh.campus_technique.proj_android_s_alifierakis.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by steli on 05-01-17.
 */

public class UserAccessBDD  {

    private static final int VERSION=1;
    private static final String NOM_DB="User.db";

    private static final String TABLE_USER="table_user";
    private static final String COL_ID="ID";
    private static final int NUM_COL_ID=0;
    private static final String COL_LOGIN="LOGIN";
    private static final int NUM_COL_LOGIN=1;
    private static final String COL_PASSWORD="PASSWORD";
    private static final int NUM_COL_PASSWORD=2;
    private static final String COL_DROIT="DROIT";
    private static final int NUM_COL_DROIT=3;

    private SQLiteDatabase db;
    private UserBDDSqlite userdb;

    public UserAccessBDD(Context c){
        userdb=new UserBDDSqlite(c, NOM_DB, null, VERSION);
    }

    public void openForWrite(){
        db=userdb.getWritableDatabase();
    }

    public void openForRead(){
        db=userdb.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public long insertUser(User u){
        ContentValues content=new ContentValues();
        content.put(COL_LOGIN,u.getLogin());
        content.put(COL_PASSWORD,u.getPassword());
        content.put(COL_DROIT,u.getDroit());

        return db.insert(TABLE_USER, null, content);
    }

    public int updateUser(int i, User u){
        ContentValues content=new ContentValues();
        content.put(COL_LOGIN,u.getLogin());
        content.put(COL_PASSWORD,u.getPassword());
        content.put(COL_DROIT,u.getDroit());
        Log.i("testBd",String.valueOf(i) + " " + u.getLogin() + " " + u.getPassword());
        return db.update(TABLE_USER,content,COL_ID + " = " + i , null);
    }

    public int removeUser(String login){
        return db.delete(TABLE_USER,COL_LOGIN + " = " + login,null);
    }

    public User getUser(String login){
        Cursor c=db.query(TABLE_USER,new String[]{
                COL_ID, COL_LOGIN, COL_PASSWORD, COL_DROIT
        }, COL_LOGIN + " LIKE \"" + login + "\"", null, null, null, COL_LOGIN);

        return cursorToUser(c);
    }

    public int countAdmin(String right){
        /*Cursor c=db.query(TABLE_USER,new String[]{
                COL_ID, COL_LOGIN, COL_PASSWORD, COL_DROIT
        }, COL_DROIT + " LIKE \"" + right + "\"", null, null, null, COL_LOGIN);*/

        Cursor c=db.rawQuery("select count(*) from " + TABLE_USER + " where " + COL_DROIT + "='" + right + "'",null);
        c.moveToFirst();
        int count=c.getInt(0);
        c.close();
        return count;
    }

    public User cursorToUser(Cursor c){
        if(c.getCount() == 0){
            c.close();
            return null;
        }

        c.moveToFirst();

        User user1 = new User();
        user1.setId(c.getInt(NUM_COL_ID));
        user1.setLogin(c.getString(NUM_COL_LOGIN));
        user1.setPassword(c.getString(NUM_COL_PASSWORD));
        user1.setDroit(c.getString(NUM_COL_DROIT));

        c.close();

        return  user1;
    }


    public ArrayList<User> getAllUsers(){
        Cursor c=db.query(TABLE_USER,new String[]{
                COL_ID, COL_LOGIN, COL_PASSWORD, COL_DROIT
        }, null, null, null, null, COL_LOGIN);

        if(c.getCount()==0){
            c.close();
            return null;
        }

        ArrayList<User> tabUser=new ArrayList<>();

        while(c.moveToNext()){
            User user1=new User();
            user1.setId(c.getInt(NUM_COL_ID));
            user1.setLogin(c.getString(NUM_COL_LOGIN));
            user1.setPassword(c.getString(NUM_COL_PASSWORD));
            user1.setDroit(c.getString(NUM_COL_DROIT));
            tabUser.add(user1);
        }
        c.close();
        return tabUser;
    }

}
