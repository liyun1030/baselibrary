package com.ly.baselibrary.application

import android.app.Application
import com.common.base.application.BaseApplication
import com.common.base.bean.BaseModel
import com.common.base.tool.BaseConstant
import com.common.base.tool.CommUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

class KotlinApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
    /**
     * 创建对象
     */
    val girlModule = module {
//        single 创建的对象在整个容器的生命周期内都是存在的，因此任意地方注入都是同一实例。
//        factory 每次都创建新的对象，因此它不被保存，也不能共享实例。
        factory {
            BaseModel().status="1001"
            BaseModel().message="11111111"
        }
        single {
            BaseModel().status="1002"
            BaseModel().message="222222"
        }
        single(named("girl1")) { (type: String) ->
            BaseModel().apply {
                this.status = type
            }
        }

        single(named("girl2")) { (type: String) ->
            BaseModel().apply {
                this.status = type
            }
        }

    }
    fun initKoin(){
        startKoin {
            androidLogger(Level.DEBUG)
            androidFileProperties()
            androidContext(this@KotlinApplication)
            //注册组件
            modules(girlModule)
            modules(listOf(girlModule))
        }
    }
}