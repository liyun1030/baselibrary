package com.common.base.tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 集合工具
 * Created by Yan Kai on 2016/6/13.
 */
public class CollectionUtils {

    public static boolean isEqualCollection(final Collection<?> a, final Collection<?> b) {
        if (a.size() != b.size()) {
            return false;
        }
        final CardinalityHelper<Object> helper = new CardinalityHelper<>(a, b);
        if (helper.cardinalityA.size() != helper.cardinalityB.size()) {
            return false;
        }
        for (final Object obj : helper.cardinalityA.keySet()) {
            if (helper.freqA(obj) != helper.freqB(obj)) {
                return false;
            }
        }
        return true;
    }

    public static <O> Map<O, Integer> getCardinalityMap(final Iterable<? extends O> coll) {
        final Map<O, Integer> count = new HashMap<>();
        for (final O obj : coll) {
            final Integer c = count.get(obj);
            if (c == null) {
                count.put(obj, 1);
            } else {
                count.put(obj, c + 1);
            }
        }
        return count;
    }

    private static class CardinalityHelper<O> {
        final Map<O, Integer> cardinalityA;
        final Map<O, Integer> cardinalityB;

        public CardinalityHelper(final Iterable<? extends O> a, final Iterable<? extends O> b) {
            cardinalityA = CollectionUtils.getCardinalityMap(a);
            cardinalityB = CollectionUtils.getCardinalityMap(b);
        }

        public int freqA(final Object obj) {
            return getFreq(obj, cardinalityA);
        }

        public int freqB(final Object obj) {
            return getFreq(obj, cardinalityB);
        }

        private int getFreq(final Object obj, final Map<?, Integer> freqMap) {
            final Integer count = freqMap.get(obj);
            if (count != null) {
                return count;
            }
            return 0;
        }
    }



    /**
     * 对字符串进行由小到大排序
     * @param str    String[] 需要排序的字符串数组
     */
    public static String strSort(String str){
        char [] b = str.toCharArray();
        Arrays.sort(b);
        String str2 = String.copyValueOf(b);
        return str2;
    }


    /**
     * 取两个字符串中的相同字符
     * @param str1
     * @param str2
     * @return
     */
    public static String getSameChar(String str1, String str2)
    {
        String s="";
        for(int i=0;i<str1.length();i++)//获取第一个字符串中的单个字符
            for(int j=0;j<str2.length();j++)//获取第er个字符串中的单个字符
            {
                if(str1.charAt(i)==str2.charAt(j))//判断字符是否相同
                    s=s+str1.charAt(i);
            }
        return s;
    }

}
