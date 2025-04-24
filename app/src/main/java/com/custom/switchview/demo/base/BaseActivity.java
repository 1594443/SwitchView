package com.custom.switchview.demo.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;


public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB binding;
    protected AppCompatActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = initViewBinding();
        setContentView(binding.getRoot());
        //定义初始化数据的方法
        initData();
    }

    protected abstract VB initViewBinding();

    protected abstract void initData();
}
