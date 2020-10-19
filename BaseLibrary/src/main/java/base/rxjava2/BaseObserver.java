package base.rxjava2;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.common.base.bean.BaseBean;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @description: 自定义Observer
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    protected Context mContext;
    private String labelTxt;
    private ProgressDialog dialog;
    private boolean isShowLoading;

    public BaseObserver(Context ctx, String text) {
        this.mContext = ctx;
        this.labelTxt = text;
        initDialog(ctx);
    }

    private void initDialog(Context activity) {
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }

    public void setLoading(boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
    }

    //开始
    @Override
    public void onSubscribe(Disposable d) {
        if (isShowLoading) {
            onRequestStart();
        }
    }

    //获取数据
    @Override
    public void onNext(BaseBean<T> baseEntity) {
        try {
            onSuccees(baseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //失败
    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);  //网络错误
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    //结束
    @Override
    public void onComplete() {
        onRequestEnd();//请求结束
    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccees(BaseBean<T> t) throws Exception;


    /**
     * 返回失败
     *
     * @param e
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;

    protected void onRequestStart() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    protected void onRequestEnd() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
