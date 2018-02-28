package idv.sean.photo_insearch.model;

import java.io.Serializable;
import java.util.Map;

public class State implements Serializable {

    private String type;
    private String userId;            //the userId changing state
    private Map<String, String> usersMap;      //total usersMap

    public State() {
    }

    public State(String type, String userId, Map<String, String> usersMap) {
        this.type = type;
        this.userId = userId;
        this.usersMap = usersMap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map<String, String> usersMap) {
        this.usersMap = usersMap;
    }
}
