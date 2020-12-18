package com.curefun.pbl.fragment

import com.amap.api.track.AMapTrackClient
import com.amap.api.track.ErrorCode
import com.amap.api.track.OnTrackLifecycleListener
import com.amap.api.track.TrackParam
import com.amap.api.track.query.model.*
import com.common.base.adapter.BaseMultiTypeAdapter
import com.common.base.fragment.BaseFragment
import com.common.base.network.Constant
import com.common.base.tool.CommUtils
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.ly.baselibrary.R
import com.ly.baselibrary.application.MyApplication
import com.ly.baselibrary.model.UserInfoModel
import com.ly.baselibrary.view.NewOrderDialog
import kotlinx.android.synthetic.main.fragment_create_order.*
import me.drakeet.multitype.Items


/**
 * created by 李云 on 2019/6/18
 * 本类的作用:创单
 */
class CreateOrderFragment : BaseFragment(), XRecyclerView.LoadingListener,
    NewOrderDialog.CloseListener {
    override fun initData() {
    }

    var model: UserInfoModel? = null
    var newOrderDialog: NewOrderDialog? = null
    public var items: Items = Items()
    public val mAdapter by lazy {
        activity?.let {
            BaseMultiTypeAdapter(Items())
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_create_order
    }

    override fun initView() {
        model = MyApplication.getInstance().spInstance.getObject(
            Constant.SP_USER_KEY,
            UserInfoModel::class.java
        )
        //添加自定义分割线
//        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
//        divider.setDrawable(ContextCompat.getDrawable(this.activity!!, R.drawable.custom_divider_class)!!)
//        listView.addItemDecoration(divider)
        newOrderDialog = NewOrderDialog(activity, this)
        newOrderDialog?.show()
        emptyLayout.setOnClickListener {
            onRefresh()
        }
    }

    /**
     * 轨迹
     */
    fun initTrack() {
        // 轨迹服务ID
        val serviceId: Long = 0
        // 设备标识
        val terminalName = model?.phone
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        val isNeedObjectStorage = false
        // 初始化轨迹服务客户端
        var aMapTrackClient = AMapTrackClient(activity?.applicationContext);
        // 初始化轨迹服务
        var trackLifecycleListener = object : OnTrackLifecycleListener {
            override fun onBindServiceCallback(p0: Int, p1: String?) {
            }

            override fun onStartGatherCallback(status: Int, msg: String?) {
                if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
                    status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED
                ) {
                    CommUtils.showToast(activity, "定位采集开启成功！")
                } else {
                    CommUtils.showToast(activity, "定位采集启动异常," + msg)
                }
            }

            override fun onStartTrackCallback(status: Int, msg: String?) {
                if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
                    status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
                    status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED
                ) {
                    // 服务启动成功，继续开启收集上报
                    aMapTrackClient.startGather(this);
                } else {
                    CommUtils.showToast(activity, "轨迹上报服务服务启动异常," + msg)
                }
            }

            override fun onStopGatherCallback(p0: Int, p1: String?) {
            }

            override fun onStopTrackCallback(p0: Int, p1: String?) {
            }

        }
        var trackListener = object : OnTrackListener {

            override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {
                TODO("Not yet implemented")
            }

            override fun onCreateTerminalCallback(addTerminalResponse: AddTerminalResponse?) {
                if (addTerminalResponse?.isSuccess() == true) {
                    // 创建完成，开启猎鹰服务
                    var terminalId = addTerminalResponse.getTid();
                    aMapTrackClient.startTrack(
                        TrackParam(
                            serviceId,
                            terminalId
                        ), trackLifecycleListener
                    );
                } else {
                    // 请求失败
                    CommUtils.showToast(
                        activity,
                        "请求失败," + addTerminalResponse?.errorMsg
                    )
                }
            }

            override fun onDistanceCallback(p0: DistanceResponse?) {
            }

            override fun onLatestPointCallback(p0: LatestPointResponse?) {
            }

            override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {
            }

            override fun onQueryTrackCallback(p0: QueryTrackResponse?) {
            }

            override fun onAddTrackCallback(p0: AddTrackResponse?) {
            }

            override fun onParamErrorCallback(p0: ParamErrorResponse?) {
            }
        }
        aMapTrackClient.queryTerminal(
            QueryTerminalRequest(serviceId, terminalName),
            object : OnTrackListener {
                override fun onQueryTerminalCallback(queryTerminalResponse: QueryTerminalResponse?) {
                    if (queryTerminalResponse?.isSuccess == true) {
                        if (queryTerminalResponse.getTid() <= 0) {
                            aMapTrackClient.addTerminal(
                                AddTerminalRequest(terminalName, serviceId),
                                trackListener
                            )
                        } else {
                            // terminal已经存在，直接开启猎鹰服务
                            var terminalId = queryTerminalResponse.getTid();
                            aMapTrackClient.startTrack(
                                TrackParam(
                                    serviceId,
                                    queryTerminalResponse.tid
                                ), trackLifecycleListener
                            );
                        }
                    }
                }

                override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {
                }

                override fun onDistanceCallback(p0: DistanceResponse?) {
                }

                override fun onLatestPointCallback(p0: LatestPointResponse?) {
                }

                override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {
                }

                override fun onQueryTrackCallback(p0: QueryTrackResponse?) {
                }

                override fun onAddTrackCallback(p0: AddTrackResponse?) {
                }

                override fun onParamErrorCallback(p0: ParamErrorResponse?) {
                }

            })
    }

    override fun onResume() {
        super.onResume()
        items.clear()
    }


    override fun onLoadMore() {
    }

    fun hideLoading() {
    }

    override fun onRefresh() {
        items.clear()
        onResume()
    }

    /**
     * 同意
     */
    override fun agreen() {
        //前往目地的
    }

}
