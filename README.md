
![channel_art.png](art%2Fchannel_art.png)

## 简介

基于[腾讯VasDolly](https://github.com/Tencent/VasDolly)最新版本`3.0.6`的图形界面衍生版本，同时增加了签名功能，旨在更好的帮助开发者构建多渠道包



## 使用说明

1. 下载并解压[最新工具包](https://github.com/ausboyue/VasDolly-GUI/releases/)，找到`Startup`脚本并双击启动图形界面（注意：需本地安装java环境）
2. 如果你的APK已经签过名可不勾选`使用自定义签名`
3. 未签名，比如你从某加固平台加固完下载的APK，可勾该选项，工具会先进行签名再进行多渠道打包
4. 默认全启用V1~V3签名，可取消勾选`V3`签名

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
2. 支持单独签名功能
3. 集成java环境


