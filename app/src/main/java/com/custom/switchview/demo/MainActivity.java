package com.custom.switchview.demo;

import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.custom.switchview.SwitchView;
import com.custom.switchview.demo.base.BaseActivity;
import com.custom.switchview.demo.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected ActivityMainBinding initViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        binding.switchView.setStatus(true);
        binding.switchView.setTrackOnColor(ContextCompat.getColor(this, R.color.DarkSeaGreen));
        binding.switchView.setTrackOnText("困难");
        binding.switchView.setTrackOnTextColor(ContextCompat.getColor(this, R.color.white));

        binding.switchView.setTrackOffColor(ContextCompat.getColor(this, R.color.DarkGray));
        binding.switchView.setTrackOffText("简单");
        binding.switchView.setTrackOffTextColor(ContextCompat.getColor(this, R.color.white));

        binding.switchView.setTrackTextSize(24);

        binding.switchView.setThumbColor(ContextCompat.getColor(this, R.color.white));
        binding.switchView.setThumbText("难度");
        binding.switchView.setThumbTextColor(ContextCompat.getColor(this, R.color.black));
        binding.switchView.setThumbTextSize(20);

        binding.switchView.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
                String s = isChecked ? "困难" : "简单";
                Toast.makeText(context, "难度: " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}