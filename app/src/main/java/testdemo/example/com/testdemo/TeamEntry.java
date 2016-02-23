package testdemo.example.com.testdemo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tset on 16/2/7.
 */
public class TeamEntry implements Serializable{

    @SerializedName("reason")
    public String reason;

    @SerializedName("result")
    public TeamResultEntry result;

    @Override
    public String toString() {
        return "TeamEntry{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
