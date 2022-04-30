package com.greenhandzdl


import com.google.gson.Gson
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

object SayBack : KotlinPlugin(
    JvmPluginDescription(id = "com.greenhandzdl.mirai-plugins-sayBack", name = "sayBack", version = "0.0.1") {
        author("greenhandzdl")
        info("sayBack")
    }
)
{
    override fun onEnable() {
        super.onEnable()
        logger.info("sayBack onEnable")
        globalEventChannel().subscribeAlways<GroupMessageEvent>{
            //获取消息发送人QQ号
            val sender = sender.id
            when {
                (message.contentToString() == "菜单") -> {
                    group.sendMessage(
                        messageChainOf(
                            At(sender) + PlainText(
                                "MiraiAutoReply 菜单 \n" +
                                        "功能类：\n" +
                                        "* 复读 + 复读文本\n" +
                                        "* 问答 + 文本\n" +
                                        "游戏类：\n" +
                                        "* 猜数字 + 猜测数字\n" +
                                        "关于\n"
                            )
                        )
                    )
                }
                message.contentToString().startsWith("复读") -> {
                    val m = message.contentToString().replace("复读", "")
                    group.sendMessage(messageChainOf(PlainText(m)))
                }
                message.contentToString().startsWith("问答") -> {
                    val m = message.contentToString().replace("问答", "")

                    val requestBody = FormBody.Builder()
                        .add("SecretId", SecretId)
                        .add("SecretKey", SecretKey)
                        .add("BotId", BotId)
                        .add("BotEnv", BotEnv)
                        .add("TerminalId", TerminalId)
                        .add("InputText", m)
                        .build()
                    val request = Request.Builder()
                        .url("https://tbp.tencentcloudapi.com")
                        .post(requestBody)
                        .build()
                    val response = OkHttpClient().newCall(request).execute()
                    val responseData = response.body?.string()
                    val responseJson = responseData?.substring(responseData.indexOf("{"), responseData.lastIndexOf("}") + 1)
                    val responseJsonObject = Gson(responseJson)
                    val answer = responseJsonObject.getString("Answer")
                    group.sendMessage(messageChainOf(PlainText(answer)))
                }
                message.contentToString().startsWith("猜数字") -> {
                    val input = message.contentToString().replace("猜数字", "")
                    val random = (1..10).random()
                    if (input == random.toString()) {
                        group.sendMessage(messageChainOf(At(sender) + PlainText("恭喜你猜对了,猜中的数字是$random")))
                    } else if (input > random.toString()) {
                        group.sendMessage(messageChainOf(At(sender) + PlainText("猜的数字大了，数是$random")))
                    } else if (input < random.toString()) {
                        group.sendMessage(messageChainOf(At(sender) + PlainText("猜的数字小了，数是$random")))
                    }
                }
            }
        }
    }

    override fun onDisable() {
        super.onDisable()
        logger.info("sayBack onDisable")
    }
}
