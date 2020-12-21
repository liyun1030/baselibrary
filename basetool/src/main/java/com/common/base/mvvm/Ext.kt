package com.common.base.mvvm

import com.common.base.bean.BaseBean

/*数据解析扩展函数*/
public fun <T> BaseBean<T>.dataConvert(): T {
    if (code.equals("2000")) {
        return data
    } else {
        throw Exception(message)
    }
}