package info706.zikub;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import info706.zikub.models.User;
import info706.zikub.services.UserService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RetrofitTest {
    @Test
    public void serviceTest() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.10.10/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService service = retrofit.create(UserService.class);

        Call<List<User>> caller = service.findAll();
        Response<List<User>> result = caller.execute();
        if(result.isSuccessful()) {
            Log.i("TEST", "------------------------");
            Log.i("HTTP RESPONSE", result.body().get(0).email);
        }
    }
}