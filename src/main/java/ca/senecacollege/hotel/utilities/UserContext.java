package ca.senecacollege.hotel.utilities;

import ca.senecacollege.hotel.models.AdminUser;

public class UserContext {
    private static final ThreadLocal<AdminUser> currentUser = new ThreadLocal<>();

    public static AdminUser getUser(){
        return currentUser.get();
    }

    public static void clear(){
        currentUser.remove();
    }

    public static void setUser(AdminUser user){
        currentUser.set(user);
    }
}
