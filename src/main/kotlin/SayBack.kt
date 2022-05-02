package com.greenhandzdl


import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.URL

fun init() {
        // val pluginfiles = File(Plugin.dataFolderPath)
         //如果没有data/com.greenhandzdl.SayBack/TBP.json则创建文件
        if (!File("data/TBP.json").exists()) {
            File("data/TBP.json").createNewFile()
            //向文件初始几个值
            val json = JSONObject()
            json.put("SecretId", "")
            json.put("SecretKey", "")
            json.put("BotId", "")
            json.put("BotEnv", "")
            json.put("TerminalId", "")
            //写入文件
            File("data/TBP.json").writeText(json.toString())
        }
    }


    object SayBack : KotlinPlugin(
        JvmPluginDescription(id = "com.greenhandzdl.mirai-plugins-sayBack", name = "sayBack", version = "0.0.1") {
            author("greenhandzdl")
            info("sayBack")
        }
    ) {
        override fun onEnable() {
            super.onEnable()
            logger.info("sayBack onEnable")

            init()//初始化配置文件

            globalEventChannel().subscribeAlways<GroupMessageEvent> {
                //获取消息发送人QQ号
                val sender = sender.id
                when {

                    (message.contentToString() == "菜单") -> {
                        group.sendMessage(
                            messageChainOf(
                                At(sender) + PlainText(
                                    "MiraiAutoReply 菜单 \n" +
                                            "好感度升级类\n" +
                                            "* 签到\n" +
                                            "功能类：\n" +
                                            "* 复读 + 复读文本\n" +
                                            "* 好图 \n" +
                                            "* 维基 + 检查文本\n" +
                                            "* 问答 + 文本\n" +
                                            "游戏类：\n" +
                                            "* 猜数字 + 猜测数字\n" +
                                            "关于\n"
                                )
                            )
                        )
                    }

                    message.contentToString().startsWith("签到") -> {
                        //获取机器时间
                        val time = System.currentTimeMillis()
                        //获取机器时间的年月日
                        val year = time / 1000 / 60 / 60 / 24 / 365
                        val month = time / 1000 / 60 / 60 / 24 / 30
                        val day = time / 1000 / 60 / 60 / 24
                        //获取机器时间的时分秒
                        val hour = time / 1000 / 60 / 60
                        val minute = time / 1000 / 60
                        val second = time / 1000
                        //合并年月日时分秒
                        val timeStr = "$year-$month-$day $hour:$minute:$second"
                        group.sendMessage(
                            messageChainOf(
                                At(sender) + PlainText(
                                    "签到成功！\n" +
                                            "机器时间：$timeStr"
                                )
                            )
                        )

                    }

                    message.contentToString().startsWith("复读") -> {
                        val m = message.contentToString().replace("复读", "")
                        group.sendMessage(messageChainOf(PlainText(m)))
                    }

                    message.contentToString().startsWith("好图") -> {
                        val url = "https://img.xjh.me/random_img.php"
                        val file = File("./img/")
                        if (!file.exists()) {
                            file.mkdirs()
                        }
                        val fileName = "./img/" + System.currentTimeMillis() + ".jpg"
                        val fileUrl = URL(url)
                        val conn = fileUrl.openConnection()
                        conn.connect()
                        val inputStream = conn.getInputStream()
                        val fos = FileOutputStream(fileName)
                        val buffer = ByteArray(1024)
                        var len = 0
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            fos.write(buffer, 0, len)
                        }
                        fos.close()
                        inputStream.close()
                        group.sendMessage(
                            messageChainOf(
                                At(sender) + Image(
                                    fileName
                                )
                            )
                        )
                    }

                    message.contentToString().startsWith("维基") -> {
                        val m = message.contentToString().replace("维基", "")
                        val result = wiki(m)
                        group.sendMessage(messageChainOf(PlainText(result)))

                    }

                    message.contentToString().startsWith("问答") -> {
                    val m = message.contentToString().replace("问答", "")
                    val responseMessage = TBP(m)
                    group.sendMessage(messageChainOf(PlainText(responseMessage)))
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

                    else -> {
                        val m = message.contentToString()
                        val responseMessage = TBP(m)
                        group.sendMessage(messageChainOf(PlainText(responseMessage)))
                    }
                }
            }
        }

        override fun onDisable() {
            super.onDisable()
            logger.info("sayBack onDisable")
        }
    }



fun wiki(m :String) :String{
    val requestBody =FormBody.Builder()
        .add("title", m)
        .build()
    val request = Request.Builder()
        .url("https://wiki.greenhandzdl.tk/api/rest_v1/page/related/")
        .post(requestBody)
        .build()
    val response = OkHttpClient().newCall(request).execute()
    val responseBody = response.body?.string()
    val json = JSONObject(responseBody)
    //解析每一个page和extract，并将page和extract拼接起来返回
    val pages = json.getJSONArray("pages")
    var result = ""
    for (i in 0 until pages.length()) {
        val page = pages.getJSONObject(i)
        val extract = page.getString("extract")
        val title = page.getString("title")
        result += title + ":" + extract + "\n"
    }
    return result
}

fun TBP(m :String) : String{
    val json = JSONObject(File("data/TBP").readText())
    //获取数据
    val SecretId = json.getString("SecretId")
    val SecretKey = json.getString("SecretKey")
    val BotId = json.getString("BotId")
    val BotEnv = json.getString("BotEnv")
    val TerminalId = json.getString("TerminalId")
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
    //解析json
    val jsonObject = JSONObject(responseData)
    //提取json中的ResponseMessage并转为string
    var responseMessage = jsonObject.getString("ResponseMessage")
    if (responseMessage == "") {
        responseMessage = "由于网络问题，暂时没找到答案！"
    }
    return responseMessage
}