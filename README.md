# SwitchView
SwitchView自定义开关按钮
![](https://github.com/1594443/SwitchView/blob/master/gif/SwitchView.gif)
## 特性
* 支持自定义track颜色，track文字，文字颜色和文字大小
* 支持自定义thumb颜色，文字，文字颜色和文字大小
* 支持setOnCheckedChangeListener监听
* 支持padding
## 如何使用
### 1.gradle
```java
repositories {
    maven { url "https://jitpack.io" }
}
```
```java
dependencies {
    implementation 'com.github.1594443:SwitchView:1.0.0'
}
```
VERSION_CODE : [here](https://github.com/1594443/SwitchView/releases)
### 2.xml
```xml
<com.custom.switchview.SwitchView
    android:id="@+id/switch_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="5dp"
    app:status="true" />
```
### 3.属性说明
#### 根据自己的需要添加属性
xml | code | 说明
---|---|---
app:status|setStatus|开关状态
app:track_on_color|setTrackOnColor|开-track颜色
app:track_on_text|setTrackOnText|开-track文字
app:track_on_text_color|setTrackOnTextColor|开-track文字颜色
app:track_off_color|setTrackOffColor|关-track颜色
app:track_off_text|setTrackOffText|关-track文字
app:track_off_text_color|setTrackOffTextColor|关-track文字颜色
app:track_text_size|setTrackTextSize|track文字大小
app:thumb_color|setThumbColor|thumb颜色
app:thumb_text|setThumbText|thumb文字
app:thumb_text_color|setThumbTextColor|thumb文字颜色
app:thumb_text_size|setThumbTextSize|thumb文字大小

### 4.监听
```java
switchView.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
        
    }
});
```
### 5.更多
细节可以参考 demo/ 示例

### 有问题需要 欢迎留言~