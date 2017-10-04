package info706.zikub.models;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private final static String AUTH_SETTINGS = "AUTH_SETTINGS";
    private final static String OAUTH_TOKEN = "OAUTH_TOKEN";

    private int id;
    private String name;
    private String email;
    private String password;
    private String remember_token;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public static void setOauthToken(Context ctx, String oauthToken) {
        SharedPreferences settings = ctx.getSharedPreferences(AUTH_SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(OAUTH_TOKEN, "Bearer " + oauthToken);
        editor.apply();
    }

    public static String getOauthToken(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(AUTH_SETTINGS, 0);
        return settings.getString(OAUTH_TOKEN, null);
    }
}
