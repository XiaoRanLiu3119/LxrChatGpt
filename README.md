# LxrChatGpt
一款使用kotlin开发的ChatGpt客户端.
也是出于自用目的写出来的简易版,已实现主要聊天功能

## 提示
项目代码为熟悉kotlin所编写,且未做优化,仅供参考
上下文联系过多会消耗成倍的tokens,目前限制两组对话


[ 下载Demo ](https://github.com/XiaoRanLiu3119/LxrChatGpt/releases/tag/pkg_release)

api-key  https://platform.openai.com/account/api-keys

配置api-key后即可调用api

api文档地址   https://platform.openai.com/docs/introduction

部分截图

![聊天界面](https://github.com/XiaoRanLiu3119/LxrChatGpt/blob/main/chat.png)

## 开发环境
- Android Studio 2022.2.1Beta1
- kotlin.android 1.7.10
- gradle:7.3.3-bin
- gradle plugin version:7.2.2
## 项目中用到的库,感谢大佬们
https://github.com/Aallam/openai-kotlin
- [ openai-kotlin](https://github.com/Aallam/openai-kotlin)
- [ BRV](https://github.com/liangjingkanji/BRV)
- autosize
- immersionbar
- XPopup
- TitleBar

## 进度
- [x] model: gpt-3.5-turbo,
- [x] 基本问答,默认仅可关联最近一条上下文,可代码里更改 beta
- [x] 回答呈打字效果逐字输入
- [ ] 代码区域样式独立且可复制
- [ ] 生成图片api
- [ ] 翻译api
- [ ] 其余

## 声明及协议
源码仅供学习。开源链接、源码、介绍及应用安装包未经开发者允许不接受他人的任何转发（发布到其他应用、推广网站、公众号、博客等）

基于本开源项目的其他开发者项目、软件所引发的问题，依据 GPL 3 免责协议本开发者不负责任。

GNU 通用公共许可协议[ GNU GENERAL PUBLIC LICENSE Version 3](https://www.gnu.org/licenses/gpl-3.0.html)

详见 [ LICENSE ](https://github.com/XiaoRanLiu3119/LxrChatGpt/blob/master/LICENSE)