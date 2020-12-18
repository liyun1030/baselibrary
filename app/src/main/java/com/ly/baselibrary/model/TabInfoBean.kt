package com.curefun.pbl.model.bean
data class TabInfoBean(val tabInfo: TabInfo) {
    data class TabInfo(val tabList: ArrayList<Tab>)

    data class Tab(val id: String, val name: String, val apiUrl: String)
}
