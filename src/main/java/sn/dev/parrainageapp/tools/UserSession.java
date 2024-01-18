package sn.dev.parrainageapp.tools;

import lombok.Getter;
import sn.dev.parrainageapp.entities.Utilisateur;

public class UserSession {

    @Getter
    private static Utilisateur userConn;
    @Override
    public String toString() {
        return userConn.getPrenom() + " " + userConn.getNom();
    }

    public static void setUserConn(Utilisateur userConn) {
        UserSession.userConn = userConn;
    }

    public static void clearSession() {
        userConn = null;
    }

}
