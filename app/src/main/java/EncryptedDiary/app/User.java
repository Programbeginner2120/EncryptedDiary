package EncryptedDiary.app;

public class User {

    private int userID;
    private String username;

    public User(int userID, String username){
        this.userID = userID;
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userID=").append(userID);
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
