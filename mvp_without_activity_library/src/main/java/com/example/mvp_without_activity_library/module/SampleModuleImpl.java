package com.example.mvp_without_activity_library.module;


import com.example.mvp_without_activity_library.contracts.SampleContracts;

public class SampleModuleImpl implements SampleContracts.ISampleModule {

    @Override
    public boolean change(boolean isSuccess) {
        return !isSuccess; //如果是真返回假，如果是假返回真。
    }
}
