package base.rxjava2;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @description:保存cookie
 */

public class CookiesSaveInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            String header = originalResponse.header("Set-Cookie");
//            SharePreferencesUtils.setString(MyApp.myApp,"cookiess",header);
        }
        return originalResponse;
    }

}
