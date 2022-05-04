# mirai-plugins-sayBack
> 一个致力于让你的机器人回复你的消息的插件（说了跟没说一样）
## 面向对象
* 具有一定能力
* 懒人
## 功能（0.0.2):
### 好感度升级类:
* 签到
### 功能类:
* 复读 + 复读文本
* 好图
* 网易云 歌名
* 精准维基 + 检查文本
* 相关维基 + 检查文本
* 问答 + 文本 [如果有人能帮忙修复就更好了]
### 游戏类：
* 猜数字
> 开始|结束 | 所猜数字大小[0,100],10次机会
## 配置文件参考：
* 配置文件：config.yml
```
{"botName":"MiraiAutoReply"} 机器人名字
```
* TBP导入（暂时不需要去管）：
```
{"SecretId":"",
  "Signature": "",
  "BotId":"",
  "BotEnv":"",
  "TerminalId":""}
```
(文档参考1)[https://cloud.tencent.cn/document/api/1060/37438]

(文档参考2)[https://cloud.tencent.cn/document/api/1060/37431]

(Signature生成)[https://console.cloud.tencent.com/api/explorer?Product=tbp&Version=2019-06-27&Action=TextReset&SignVersion=]

(手动生成参考1)[https://cloud.tencent.cn/document/api/1060/37432]

(手动生成参考2)[https://cloud.tencent.com/document/product/649/72342]

## 数据文件：
* 群文件：$group.json
```
{"":"","permission":""}
权限(0，禁止使用；1，允许使用)
```
* 用户文件：$user.json
```
{"guess_status":"0",//猜数字状态
"coins":0,//货币数量
"guess_number":"",//猜数字剩余次数
"permission":"1",//权限(0，禁止使用；1，允许使用；2，管理权限；3，root权限)
"time":,//签到时间
"status":6}//好感度
```
> 注意：未提出的建议保留，可能部分为正在开发内容或者正在使用内容，删去会导致插件异常
## 关于:
QQGroup:994915558