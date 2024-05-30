
![channel_art.png](art%2Fchannel_art.png)

## 简介

基于[腾讯VasDolly](https://github.com/Tencent/VasDolly)最新版本`3.0.6`的图形界面衍生版本，旨在更好的帮助开发者构建多渠道包



## 使用

下载并解压[工具包](gui%2Fjar%2FVasDolly-GUI.zip)，找到`Startup`脚本并双击启动图形界面（注意：本地需安装java环境）

## 渠道格式说明

1. txt文件，内容格式：`渠道code,渠道名称`，如：

```
xxxx,官方包
yyyy,应用宝
......
每增加一个渠道，请换行并按如上格式填写
```

2. 相比于VasDolly的格式，多了个渠道名称，方便开发者对号入座，另外构建的渠道包文件名称也会加上渠道名称


## TODO

1. 支持多线程构建
2. 支持签名功能，最新版已支持
3. Thinking...


