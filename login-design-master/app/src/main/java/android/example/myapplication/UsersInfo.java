package android.example.myapplication;

public class UsersInfo {
    public int _id;
    public String name;
    public String email;
    public String password;

    public UsersInfo(){

    }

    public UsersInfo(String name){
        this.name = name;
    }
    public UsersInfo(String name,String password){
        this.email = name;
        this.password = password;
    }

    public UsersInfo(String name,String email,String password){
        this.email = name;
        this.name = email;
        this.password = password;
    }

}