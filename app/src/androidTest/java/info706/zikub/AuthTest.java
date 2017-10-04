package info706.zikub;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import info706.zikub.models.User;
import info706.zikub.services.AuthService;
import info706.zikub.services.UserService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AuthTest {

    @Test
    public void loginTest() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.10.10/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

        AuthService service = retrofit.create(AuthService.class);

        Call<String> caller = service.login("toto@gmail.com", "test1234");
        Response<String> res = caller.execute();
        Log.i("TEST LOGIN", "---------------------------------");
        Log.i("TOKEN", res.body());
    }

    @Test
    public void currentUserTest() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.10.10/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        UserService service = retrofit.create(UserService.class);

        Call<User> caller = service.getCurrent("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQxMmFlYjNhZjJhMDIzNDE1ZDNkNTg2YzJhZWIzZGEwZTUwOGYxM2QxM2IwODJlOWY5N2FlYjM4MTY4MjkwZGQyZWQ4ZDhlZDQxMWMxMDA2In0.eyJhdWQiOiIxIiwianRpIjoiZDEyYWViM2FmMmEwMjM0MTVkM2Q1ODZjMmFlYjNkYTBlNTA4ZjEzZDEzYjA4MmU5Zjk3YWViMzgxNjgyOTBkZDJlZDhkOGVkNDExYzEwMDYiLCJpYXQiOjE1MDcwNTM2NzYsIm5iZiI6MTUwNzA1MzY3NiwiZXhwIjoxNTM4NTg5Njc2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.mx6Yt3DkUAHOerJjIQvvjjNauXN0etVQMUCNMCgY6xWxKufm8N26UeTPjCJVew-CMlpcCB8zP1uQdY-8cxdIUQoIpfCDeXK3Gd-MeLJ5bqN1ksj3cengVXBMjrUST9Cr_ywy-Wn1pga4sXheEkN6LcivscBwCTJr5qTNSzYrQ9JloJR-GKdGdLLi3cH-thp8OzH_RjBdHGBA5Yb7R6t5GcKwvfwq6ZfXdpFolQa_ZcZ8Tv811A2Xd5p-TdUK_36i8Yns5WibbVTbDZUPwj_-IzGttLGxgjBita736R-18Q1XvaLWn2NIFqi3DO41-fmih_XzZ9Omq8F9_4tFxZJiqEIWUUkiXpmPEDZQVPk37KYInQMNE0olATFV4YNuSETVvnyrjSV4IMHWxHCz0gG9BPmjRYy1yfEAVwi41_ZtaNXJ1IHx6hwYQsDqqWhnrVvAnyZGA8qH46iWqps5Ds1iMSM0AQOnyg0PzW9GCFmlWeH19HrVgjPHHWFmcf_SpP6j_ghPoFYUPTFp_DvTbqSkU6kK69ialtO-uI2Py_OOdemGa1D3G7LnVwbDfBzefmCWwyfjEJhatfx97uMcD2biNkzQ9uEI9J-Ciw0NFkvrJeSX6PHK2_tSn-lRXhFJHetGTncDB96Qeo4xl4kiAQD4lU2Gekr4vt0onkiLE2AM4JU");
        Response<User> res = caller.execute();
        Log.i("TEST CURRENT USER", "---------------------------------");
        Log.i("USER", res.body().getEmail());
    }
}