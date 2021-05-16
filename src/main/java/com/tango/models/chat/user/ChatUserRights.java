package com.tango.models.chat.user;

public enum ChatUserRights {
    GOD,
    ADMIN,
    USER;

    public static ChatUserRights getInstance(String s) {
        if (s.equals("GOD")) return GOD;
        if (s.equals("ADMIN")) return ADMIN;
        return USER;
    }
}
