package com.common.base.rxjava2.model;

public abstract class SuperBase<CONTRACT> {
    public abstract CONTRACT getContract(); //子类必须实现方法，具体实现哪些看传过来的接口中有哪些方法
}
