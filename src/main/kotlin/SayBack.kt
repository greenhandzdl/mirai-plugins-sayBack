package com.greenhandzdl


import com.greenhandzdl.SayBack.configFolder
import com.greenhandzdl.SayBack.dataFolder
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
import org.json.JSONObject
import java.io.File

fun init() {
        //必要的初始化
        val datafile = dataFolder
        if (!datafile.exists()) {
            datafile.mkdir()
        }
        val configfile = configFolder
        if (!configfile.exists()) {
            configfile.mkdir()
        }

        //初始化config.json文件
        if(!File("$configfile/config.json").exists()){
            File("$configfile/config.json").createNewFile()
            val json = JSONObject()
            json.put("botName", "MiraiAutoReply")
            //写入文件
            File("$configfile/config.json").writeText(json.toString())
        }


        // 初始化permission文件
        if(!File("$configfile/permission.json").exists()){
            File("$configfile/permission.json").createNewFile()
            val json = JSONObject()
            json.put("", "root")//最高权限，有且只有一个（全局）
            json.put("","admin")//管理员（略次于root）
            json.put("", "group.admin")//群管理员（局部，暂时不用管）
            json.put("", "blacklist")//黑名单，不可以被使用
            //写入文件
            File("$configfile/permission.json").writeText(json.toString())
        }

        //如果没有TBP.json则创建文件
        if (!File("$configfile/TBP.json").exists()) {
            File("$configfile/TBP.json").createNewFile()
            //向文件初始几个值
            val json = JSONObject()
            json.put("SecretId", "")
            json.put("SecretKey", "")
            json.put("BotId", "")
            json.put("BotEnv", "")
            json.put("TerminalId", "")
            //写入文件
            File("$configfile/TBP.json").writeText(json.toString())
        }

        //data初始化
        //如果没有status.json则创建文件
        if (!File("$datafile/status.json").exists()) {
            File("$datafile/status.json").createNewFile()
            val json = JSONObject()
            File("$datafile/status.json").writeText(json.toString())
            json.put("", "")//(userid,好感度)
            //写入文件
            File("$datafile/status.json").writeText(json.toString())
        }

        //如果没有status_time.json则创建文件
        if (!File("$datafile/status_time.json").exists()) {
            File("$datafile/status_time.json").createNewFile()
            val json = JSONObject()
            File("$datafile/status_time.json").writeText(json.toString())
            json.put("", "")//(userid,时间戳)
            //写入文件
            File("$datafile/status_time.json").writeText(json.toString())
        }

        //如果没有guess.json则创建文件
        if (!File("$datafile/guess.json").exists()) {
            File("$datafile/guess.json").createNewFile()
            val json = JSONObject()
            File("$datafile/guess.json").writeText(json.toString())
            json.put("", "")//(userid,状态【0：已结束 1~10：正在进行中】)
            //写入文件
            File("$configfile/guess.json").writeText(json.toString())
        }

        //如果没有guess_random.json则创建文件
        if (!File("$datafile/guess_random.json").exists()) {
            File("$datafile/guess_random.json").createNewFile()
            val json = JSONObject()
            File("$datafile/guess_random.json").writeText(json.toString())
            json.put("", "")//(userid,random)
            //写入文件
            File("$configfile/guess_random.json").writeText(json.toString())
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
                //获取config.json文件中的值
                val config = JSONObject(File("$configFolder/config.json").readText())
                val botName = config.getString("botName")

                //获取permission.json文件中的值(暂时没做过来)
                //val json = JSONObject(File("$configFolder/permission.json").readText())

                when {

                    //菜单类消息
                    (message.contentToString() == "菜单") -> {
                        group.sendMessage(
                            messageChainOf(
                                At(sender) + PlainText(
                                    "\n $botName 菜单 \n" +
                                            "好感度升级类\n" +
                                            "* 签到\n" +
                                            "功能类：\n" +
                                            "* 复读 + 复读文本\n" +
                                            "* 好图 \n" +
                                            "* 维基 + 检查文本\n" +
                                            "* 问答 + 文本\n" +
                                            "游戏类：\n" +
                                            "* 猜数字 +\n" +
                                            "~ 开始|结束\n" +
                                            "~ 所猜数字大小[0,100],10次机会\n" +
                                            "* 关于\n"
                                )
                            )
                        )
                    }

                    //好感度升级类
                    message.contentToString().startsWith("签到") -> {
                        //从status.json中获取send的好感度
                        val json = JSONObject(File("$dataFolder/status.json").readText())
                        //如果没有这个人的好感度，则初始化好感度为0
                        if (!json.has(sender.toString())) {
                            json.put(sender.toString(), 0)
                        }
                        //获取好感度
                        val love = json.getInt(sender.toString()) + 1
                        //更新好感度
                        json.put(sender.toString(), love)
                        //写入文件
                        File("$dataFolder/status.json").writeText(json.toString())
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
                                            "$botName 好感度：${love - 1}\n" +
                                            "$botName 时间：$timeStr"
                                )
                            )
                        )

                    }

                    //功能类
                    message.contentToString().startsWith("复读") -> {
                        val m = message.contentToString().replace("复读", "")
                        group.sendMessage(messageChainOf(PlainText(m)))
                    }

                    /***

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

                     ***/

                    message.contentToString().startsWith("问答") -> {
                    val m = message.contentToString().replace("问答", "")
                    val responseMessage = tbp(m)
                    group.sendMessage(messageChainOf(PlainText(responseMessage)))
                    }

                    //游戏类
                    message.contentToString().startsWith("猜数字") -> {
                        val input = message.contentToString().replace("猜数字", "")
                        //获取guess.json文件
                        val status = JSONObject(File("$dataFolder/guess.json").readText())
                        //从guess中获取send状态,如果没有则建立一个
                        if (!status.has(sender.toString())) {
                            status.put(sender.toString(), 0)
                        }
                        val send = status.getString(sender.toString())
                        when (send){
                            "0" ->{
                                when (input){
                                    "开始" ->{
                                        //设置sender为10
                                        val status = JSONObject(File("$dataFolder/guess.json").readText())
                                        status.put(sender.toString(), "10")
                                        //写入guess.json
                                        File("$dataFolder/guess.json").writeText(status.toString())
                                        val random = (1..10).random()
                                        //将random写入guess_random.json
                                        val randomStatus = JSONObject()
                                        randomStatus.put(sender.toString(), random)
                                        File("$dataFolder/guess_random.json").writeText(randomStatus.toString())
                                        group.sendMessage(messageChainOf(PlainText("猜数字游戏开始！")))
                                    }
                                    else ->{
                                        group.sendMessage(messageChainOf(PlainText("请先开始游戏！注意：新用户正在初始化，需要重新提交一次开始命令！")))
                                    }
                                }
                            }
                            else ->{
                                when (input){
                                    "结束" ->{
                                        //设置sender为0
                                        val status = JSONObject(File("$dataFolder/guess.json").readText())
                                        status.put(sender.toString(), "0")
                                        //写入guess.json
                                        File("$dataFolder/guess.json").writeText(status.toString())
                                        //读取random的值
                                        val randomStatus = JSONObject(File("$dataFolder/guess_random.json").readText())
                                        val random = randomStatus.getInt(sender.toString())
                                        //将random清零写入guess_random.json
                                        randomStatus.put(sender.toString(), 0)
                                        group.sendMessage(messageChainOf(PlainText("猜数字游戏结束！值是$random")))
                                    }
                                    else ->{
                                        //读取random的值
                                        val randomStatus = JSONObject(File("$dataFolder/guess_random.json").readText())
                                        val random = randomStatus.getInt(sender.toString())
                                        //判断input是否等于random
                                        if (input.toInt() == random){
                                            //设置sender为0
                                            val status = JSONObject(File("$dataFolder/guess.json").readText())
                                            status.put(sender.toString(), "0")
                                            //写入guess.json
                                            File("$dataFolder/guess.json").writeText(status.toString())
                                            //将random清零写入guess_random.json
                                            randomStatus.put(sender.toString(), 0)
                                            group.sendMessage(messageChainOf(PlainText("猜对了！")))
                                        }
                                        else{
                                            //设置sender为sender-1
                                            val status = JSONObject(File("$dataFolder/guess.json").readText())
                                            status.put(sender.toString(), (status.getInt(sender.toString()) - 1).toString())
                                            //写入guess.json
                                            File("$dataFolder/guess.json").writeText(status.toString())
                                            //判断sender是否为0
                                            if (status.getInt(sender.toString()) == 0){
                                                //将random清零写入guess_random.json
                                                randomStatus.put(sender.toString(), 0)
                                                group.sendMessage(messageChainOf(PlainText("猜错了！")))
                                            }
                                            else{
                                                group.sendMessage(messageChainOf(PlainText("猜错了！还有${status.getInt(sender.toString())}次机会")))
                                            }
                                        }
                                        }
                                    }
                                }
                            }
                    }

                    /***
                    else -> {
                        val m = message.contentToString()
                        val responseMessage = tbp(m)
                        group.sendMessage(messageChainOf(PlainText(responseMessage)))
                    }
                    ***/
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

fun tbp(m :String) : String{
    val json = JSONObject(File("$configFolder/TBP.json").readText())
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