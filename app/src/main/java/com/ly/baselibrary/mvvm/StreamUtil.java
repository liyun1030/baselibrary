package com.ly.baselibrary.mvvm;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.stream.Stream;

public class StreamUtil {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testSteam(){
        Integer[] nums=new Integer[10];
        Stream<Integer> stream= Arrays.stream(nums);
        Stream<Integer> stream2 = Stream.of(1,2,3,4,5,6);

        Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2).limit(6);
        stream2.forEach(System.out::println);

    }

}
