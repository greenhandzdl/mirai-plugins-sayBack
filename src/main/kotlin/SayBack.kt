package com.greenhandzdl


import com.greenhandzdl.SayBack.configFolder
import com.greenhandzdl.SayBack.dataFolder
import io.netty.handler.codec.http.HttpUtil
import kotlinx.datetime.Clock
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
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
import java.io.FileOutputStream
import java.io.StringReader
import java.net.URL
import java.net.URLConnection
import javax.xml.parsers.DocumentBuilderFactory

fun init() {
        //必要的初始化(文件夹)
        val configfile = configFolder
        if (!configfile.exists()) {
            configfile.mkdir()
        }
        val datafile = dataFolder
        if (!datafile.exists()) {
            datafile.mkdir()
        }
        if(!File("$datafile/IMC").exists()){
            File("$datafile/IMC").mkdir()
        }
        if(!File("$datafile/group").exists()){
            File("$datafile/group").mkdir()
        }
        if(!File("$datafile/user").exists()){
            File("$datafile/user").mkdir()
        }

        //config文件夹下的文件
        //初始化config.json文件
        if(!File("$configfile/config.json").exists()){
            File("$configfile/config.json").createNewFile()
            val json = JSONObject()
            json.put("botName", "MiraiAutoReply")
            //写入文件
            File("$configfile/config.json").writeText(json.toString())
        }
        //初始化word.json文件
        if(!File("$configfile/word.json").exists()){
            File("$configfile/word.json").createNewFile()
            val json = JSONObject()
            json.put("questions", "answers")
            //写入文件
            File("$configfile/word.json").writeText(json.toString())
        }
        //初始化敏感词.txt文件
        if(!File("$configfile/敏感词.txt").exists()){
            File("$configfile/敏感词.txt").createNewFile()
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

        //data文件夹下的文件
        //初始化group.json文件
        if (!File("$datafile/group/group.json").exists()) {
            File("$datafile/group/group.json").createNewFile()
            val json = JSONObject()
            json.put("permission", "")//权限(0，禁止使用；1，允许使用)
            json.put("", "")//想好再加
            //写入文件
            File("$datafile/group/group.json").writeText(json.toString())
        }
        //初始化user.json文件
        if (!File("$datafile/user/user.json").exists()) {
            File("$datafile/user/user.json").createNewFile()
            val json = JSONObject()
            json.put("permission", "1")//权限(0，禁止使用；1，允许使用；2，管理权限；3，root权限)
            json.put("coins", "0")//货币
            json.put("time", "")//签到时间
            json.put("status", "0")//好感值
            json.put("guess_status", "0")//猜数字游戏状态（0，未开始；1，进行中；）
            json.put("guess_number", "")//猜数字游戏数字
            //写入文件
            File("$datafile/user/user.json").writeText(json.toString())
        }
        //初始化Image.json文件
        if (!File("$datafile/IMC/Image.json").exists()) {
            File("$datafile/IMC/Image.json").createNewFile()
            val json = JSONObject()
            json.put("names", "0")//名字
            //写入文件
            File("$datafile/IMC/Image.json").writeText(json.toString())
        }
    }

fun check(pathName :String,FileName :String,keyValue :String) {
    val folder = File(pathName)
    val file = File(pathName + File.separator + FileName)
    val json = JSONObject()
    if(!folder.exists()){
        folder.mkdir()
        file.createNewFile()
        json.put(keyValue, "0")
        File("$file").writeText(json.toString())
    }else{
        if(!file.exists()) {
            file.createNewFile()
            json.put(keyValue, "0")
            File("$file").writeText(json.toString())
        }else{
            //如果keyvalue为空，则添加到json中
            if(keyValue.isEmpty()){
                json.put(keyValue, "0")
                File("$file").writeText(json.toString())
            }
        }
    }
}

//使用中初始化
fun use_init(user :String, userInWhere :String) {

    val datafile = dataFolder
    //初始化group.json文件
    if (!File("$datafile/group/$userInWhere.json").exists()) {
        File("$datafile/group/$userInWhere.json").createNewFile()
        val json = JSONObject()
        json.put("permission", "")//权限(0，禁止使用；1，允许使用)
        json.put("", "")//想好再加
        //写入文件
        File("$datafile/group/$userInWhere.json").writeText(json.toString())
        val groupNotification = "创建了一个新的群，群号为：$userInWhere"
    }else
    {
        //读取文件
        val json = JSONObject(File("$datafile/group/$userInWhere.json").readText())
        if (json.getString("permission") == "0") {
            val groupNotification = "群号为：$userInWhere 的群已被禁止使用"
        }else{
            val groupNotification = "群号为：$userInWhere 的群已被使用"
        }
    }
    //初始化user.json文件
    if (!File("$datafile/user/$user.json").exists()) {
        File("$datafile/user/$user.json").createNewFile()
        val json = JSONObject()
        json.put("permission", "1")//权限(0，禁止使用；1，允许使用；2，管理权限；3，root权限)
        json.put("coins", "0")//货币
        json.put("time", "0")//签到时间
        json.put("status", "0")//好感值
        json.put("guess_status", "0")//猜数字游戏状态（0，未开始；1，进行中；）
        json.put("guess_number", "")//猜数字游戏数字
        //写入文件
        File("$datafile/user/$user.json").writeText(json.toString())
        val userNotification = "创建了一个新的用户，用户名为：$user"
    }else{
        //读取文件
        val json = JSONObject(File("$datafile/user/$user.json").readText())
        if (json.getString("permission") == "0") {
            val userNotification = "用户名为：$user 的用户已被禁止使用"
        }else{
            val userNotification = "用户名为：$user 的用户已被使用"
        }
    }
}

//图片下载
fun download(downLoadUrl: String , filename : String) :Boolean{
    val url = URL(downLoadUrl)
    val con: URLConnection = url.openConnection()
    val `is`: java.io.InputStream = con.getInputStream()
    val bs = ByteArray(1024)
    var len: Int
    val file = File(filename)
    val os = FileOutputStream(file, true)
    while (`is`.read(bs).also { len = it } != -1) {
        os.write(bs, 0, len)
    }
    os.close()
    `is`.close()
    return true
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
                //sender.id group.id初始化
                val user = sender.id.toString()
                val userInWhere = group.id.toString()
                val init = use_init(user , userInWhere)

                //获取config.json文件中的值
                val config = JSONObject(File("$configFolder/config.json").readText())
                val botName = config.getString("botName")


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
                                            "* 好图 \n"
                                            /**
                                            "* 维基 + 检查文本\n" +
                                            "* 问答 + 文本\n" +
                                            "游戏类：\n" +
                                            "* 猜数字 +\n" +
                                            "~ 开始|结束\n" +
                                            "~ 所猜数字大小[0,100],10次机会\n" +
                                            "* 关于\n"
                                            */
                                )
                            )
                        )
                    }

                    //好感度升级类
                    message.contentToString().startsWith("签到") -> {
                        //从status.json中获取send的好感度
                        val json = JSONObject(File("$dataFolder/user/$user.json").readText())
                        //获取coins
                        val coins = json.getInt("coins")
                        //获取status
                        val status = json.getInt("status")
                        //获取time
                        var time = json.getInt("time")

                        //如果time为0则赋值当前时间减去40
                        if (time == 0) {
                            time = (Clock.System.now().epochSeconds - 400).toInt()
                            //更新签到时间
                            json.put("time", time)
                            File("$dataFolder/user/$user.json").writeText(json.toString())
                        }

                        //获取当前时间 - 300
                        val nowTime = (Clock.System.now().epochSeconds - 300).toInt()

                        if(time < nowTime){
                            //获取签到时间
                            val time_now = nowTime + 300
                            //更新签到时间
                            json.put("time", time_now)
                            //更新coins
                            json.put("coins", coins + 1)
                            //更新好感度
                            json.put("status", status + 1)
                            //更新status.json
                            File("$dataFolder/user/$user.json").writeText(json.toString())
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 签到成功！\n" +
                                                "$botName 货币：${coins + 1}\n" +
                                                "$botName 好感度：${status + 1}\n" +
                                                "$botName 时间：$time_now\n"
                                    )
                                )
                            )
                        }else{
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 哦哦，签到失败！待会再试试吧！\n" +
                                                "$botName 货币：$coins\n" +
                                                "$botName 好感度：$status\n" +
                                                "$botName 时间：$time\n"
                                    )
                                )
                            )
                        }


                    }

                    //功能类
                    //复读
                    message.contentToString().startsWith("复读") -> {
                        //从status.json中获取send的好感度
                        val json = JSONObject(File("$dataFolder/user/$user.json").readText())
                        //获取coins
                        val coins = json.getInt("coins")
                        if (coins >= 1) {
                            //更新coins
                            json.put("coins", coins - 1)
                            //更新status.json
                            File("$dataFolder/user/$user.json").writeText(json.toString())
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 复读成功！\n" +
                                                "$botName 货币：${coins - 1}\n"
                                    )
                                )
                            )
                            val m = message.contentToString().replace("复读", "")
                            group.sendMessage(messageChainOf(PlainText(m)))
                        }else{
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 复读失败！\n" +
                                                "$botName 货币：$coins\n"
                                    )
                                )
                            )
                        }
                    }

                    //获取url
                    /***
                    val url = "https://img.xjh.me/random_img.php?return="
                    if(download(direcurl,names.toString()) == true) {
                    subject.sendImage(File(names.toString()))
                    }else{
                    subject.sendMessage(PlainText("糟了，图片下载失败了捏~"))
                    }
                     */
                    message.contentToString().startsWith("好图") -> {
                        //从status.json中获取send的好感度
                        val status = JSONObject(File("$dataFolder/user/$user.json").readText())
                        //获取coins
                        val coins = status.getInt("coins")
                        if (coins >= 1) {
                            //更新coins
                            status.put("coins", coins - 1)
                            //更新status.json
                            File("$dataFolder/user/$user.json").writeText(status.toString())
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 好起来了！\n" +
                                                "$botName 货币：${coins - 1}\n"
                                    )
                                )
                            )

                            //获取data/IMC/Image.json
                            val json = JSONObject(File("$dataFolder/IMC/Image.json").readText())
                            //获取names并且更新
                            val names = json.getInt("names") + 1
                            json.put("names", names + 1)
                            //更新data/IMC/Image.json
                            File("$dataFolder/IMC/Image.json").writeText(json.toString())
                            val back = URL("https://img.xjh.me/random_img.php?return=json").readText()
                            val json_first = JSONObject(back)
                            try {
                                val imgurl : String ="https:" + json_first.getString("img")
                                val name : String = "$dataFolder/IMC/" + names + ".jpg"
                                if(download(imgurl,name) == true) {
                                    subject.sendImage(java.io.File(name))
                                }
                            }catch (e: Exception){
                                subject.sendMessage(e.toString())
                            }
                        }else{
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 好不起来，请签到后再好好试试！\n" +
                                                "$botName 货币：$coins\n"
                                    )
                                )
                            )
                        }

                }




                    /***
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
                    /***
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
                    */
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
    val client = okhttp3.OkHttpClient()
    val requestBody = FormBody.Builder()
        .add("SecretId", SecretId)
        .add("SecretKey", SecretKey)
        .add("BotId", BotId)
        .add("BotEnv", BotEnv)
        .add("TerminalId", TerminalId)
        .add("Version","2019-06-27")
        .add("Action","TextProcess")
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