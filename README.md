# 跨平台剪切板同步

使用Java开发的跨平台剪切板同步. 目前支持 Max OS, Windows 系统.

## 编译 & 执行

```base
# mvn clean compile assembly:singles.jar 
# java -jar target/clipboard-sync-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 技术架构

客户端通过定时器监视剪切板变化(没查到JDK自带剪贴板变化监听方法), 如果发生变化将剪贴板发布到RabbitMQ上, 其他客户端监听RabbitMQ, 并更新本地剪切板。

## Road Map

目前只实现了基本的剪切板文本同步.

后续将支持 iOS 和 Android, 以及开发PWA用于支持Web场景, 并支持图片和文件等数据同步.

建立安全的剪切板传输, 支持剪切板内容正则匹配过滤. 目前是测试阶段, 不要用于敏感数据.

