package com.ly.baselibrary.mvvm;

import android.util.Log;

import com.common.base.bean.BaseModel;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class RxAndroidUtil {

    public static void main(String[] args) {
        testRxJava();
    }

    public static void testRxJava() {
        List list = new ArrayList();
        ArrayList<BaseModel> tmp=new ArrayList<>();
        if (list != null && list.size() > 0) {
            Observable.fromArray(new Integer[]{1, 2, 3, 4, 5, 6}).flatMapIterable((Function<Integer, Iterable<BaseModel>>) num -> {
                BaseModel bean = new BaseModel();
                BaseModel bean2 = new BaseModel();
                return new ArrayList<BaseModel>() {
                    {
                        add(bean);
                        add(bean2);
                    }
                };
            }).subscribe(new Observer<BaseModel>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull BaseModel applyClassifyBean) {
                    Log.e("----",applyClassifyBean.getMessage());
                    tmp.add(applyClassifyBean);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

        }

        Observable.fromArray(new Integer[]{1, 2, 3}).flatMapIterable(new Function<Integer, Iterable<String>>() {
            @Override
            public Iterable<String> apply(final Integer s) throws Exception {
                return new ArrayList<String>() {{
                    add("a" + s);
                    add("b" + s);
                }};
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("----", s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

//        if (list != null && list.size() > 0) {
//            List<Item> mDetailTypeItems = new ArrayList<>();
//            int listSize = list.size();
//            if (listSize > 10) listSize = 10;
//            int toIndex = 2;
//            for (int i = 0; i < listSize; i += 2) {
//                if (i + 2 > listSize) {
//                    toIndex = listSize - i;
//                }
//                List<DistributeTeacherModel.DataBean> listDesBeans = list.subList(i, i + toIndex);
//                mDetailTypeItems.add(new ProductParamSupportTypeModel(listDesBeans));
//            }
//            mDetailItems.addAll(mDetailTypeItems);
//            if (list.size() > 10)
//                mDetailItems.add(new ProductParamSupportMoreTypeModel(getContext().getString(R.string.Open), list, mDetailTypeItems));
//        }

    }

    /**
     * String数组的数据转换成Integer
     */
    public void testList() {
        Observable.fromArray(new String[]{"1", "2", "3"}).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return Integer.valueOf(s);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer integer) {
                Log.e("----", "----onNext:" + integer);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
