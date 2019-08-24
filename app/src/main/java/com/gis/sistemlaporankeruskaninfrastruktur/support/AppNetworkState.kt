package com.gis.sistemlaporankeruskaninfrastruktur.support

sealed class NetworkingState{
    class Create: NetworkingState()
    class Destroy: NetworkingState()
    class ShowLoading(val status:Boolean): NetworkingState()
    class ResponseSuccess<T>(val respon:T): NetworkingState()
    class ResponseFailure(val respon:String?): NetworkingState()
}

interface ViewNetworkState{
    var networkState: NetworkingState
}

interface IView{
    fun showLoading(status:Boolean)
    fun requestSuccess(response:Any?)
    fun requestFailure(response:String?)
    fun setupView()
}