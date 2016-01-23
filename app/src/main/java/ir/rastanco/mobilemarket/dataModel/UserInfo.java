package ir.rastanco.mobilemarket.dataModel;

/**
 * Created by ShaisteS on 1394/11/0.
 */
public class UserInfo {

    private String userEmail; //Email
    private int  userLoginStatus; //0:logout,1:login
    private int  userId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserLoginStatus() {
        return userLoginStatus;
    }

    public void setUserLoginStatus(int userLoginStatus) {
        this.userLoginStatus = userLoginStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
