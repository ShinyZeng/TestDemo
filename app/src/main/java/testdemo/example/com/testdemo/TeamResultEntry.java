package testdemo.example.com.testdemo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tset on 16/2/8.
 */
public class TeamResultEntry implements Serializable{

    @SerializedName("full_name")
    public String fullName;

    @SerializedName("logo_link")
    public String logoLink;

    @Override
    public String toString() {
        return "TeamResultEntry{" +
                "fullName='" + fullName + '\'' +
                ", logoLink='" + logoLink + '\'' +
                '}';
    }
}
