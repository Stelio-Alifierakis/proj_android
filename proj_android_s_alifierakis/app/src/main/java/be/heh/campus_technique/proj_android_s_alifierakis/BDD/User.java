package be.heh.campus_technique.proj_android_s_alifierakis.BDD;

/**
 * Created by steli on 05-01-17.
 */

public class User {

    private int id;
    private String login;
    private String password;
    private String droit;

    public User(){}

    public User(String l, String p, String d){

        this.setLogin(l);
        this.setPassword(p);
        this.setDroit(d);

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();

        sb.append("ID : " + Integer.toString(getId()) +"\n" + "Login : " + getLogin() +"\n" + "Password : " + getPassword()  +"\n" + "Droit : " + getDroit());

        return sb.toString();
    }

    public String getDroit() {
        return droit;
    }

    public void setDroit(String droit) {
        this.droit = droit;
    }
}
