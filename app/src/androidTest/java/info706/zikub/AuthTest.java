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

        Call<User> caller = service.getCurrent(User.getOauthToken());
        Response<User> res = caller.execute();
        Log.i("TEST CURRENT USER", "---------------------------------");
        Log.i("USER", res.body().email);
    }
}