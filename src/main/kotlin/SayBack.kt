package com.greenhandzdl


import com.greenhandzdl.SayBack.configFolder
import com.greenhandzdl.SayBack.dataFolder
import io.netty.handler.codec.http.HttpUtil
import kotlinx.datetime.Clock
import kotlinx.serialization.json.buildJsonObject
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.StringReader
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection
import javax.print.attribute.standard.JobOriginatingUserName
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.random.Random




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
            json.put("botNumber", "机器人qq号")
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
            json.put("Signature", "")
            json.put("BotId", "")
            json.put("BotEnv", "")
            json.put("TerminalId", "")
            //写入文件
            File("$configfile/TBP.json").writeText(json.toString())
        }
        //如果没有mly.json则创建文件
        if (!File("$configfile/mly.json").exists()) {
            File("$configfile/mly.json").createNewFile()
            //向文件初始几个值
            val json = JSONObject()
            json.put("api_key", "")
            json.put("api_secret", "")
            //写入文件
            File("$configfile/mly.json").writeText(json.toString())
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
            json.put("guess_number", "0")//猜数字游戏数字
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

fun update(){
    //v0.0.1升级到v0.0.2的定义(由于上个版本都有，这里简单接受下check用法
    //check("$dataFolder/user","user.json","guess_status")
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
        json.put("guess_number", "0")//猜数字游戏数字
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
            update()//版本更新而做的兼容



            globalEventChannel().subscribeAlways<GroupMessageEvent> {
                //sender.id group.id初始化
                val user = sender.id.toString()
                val userInWhere = group.id.toString()
                val init = use_init(user , userInWhere)

                //获取config.json文件中的值
                val config = JSONObject(File("$configFolder/config.json").readText())
                val botName = config.getString("botName")
                val botNumber  = config.getLong("botNumber")


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
                                            "* 网易云 歌名 \n" +
                                            "* 精准维基 + 检查文本\n" +
                                            "* 相关维基 + 检查文本\n" +
                                            "* 问答 + 文本\n" +
                                            /**
                                            "* 问答 + 文本\n" +
                                             */
                                            "游戏类：\n" +
                                            "* 猜数字 +\n" +
                                            "~ 开始|结束\n" +
                                            "~ 所猜数字大小[0,100],10次机会\n" +
                                            "其他：\n" +
                                            "* 关于\n"

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
                        val m = message.contentToString().replace("复读", "").replace("\\s".toRegex(), "")
                        if (m.isNotEmpty()) {
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
                        }else {
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 复读失败！\n" +
                                                "请输入复读内容！\n"
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
                            json.put("names", names)
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

                    message.contentToString().startsWith("网易云") -> {
                        val m = message.contentToString().replace("网易云", "").replace("\\s".toRegex(), "")
                        if (m.isNotEmpty()) {
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
                                            "\n 正在搜索中了，如果没有则不发送！\n" +
                                                    "$botName 货币：${coins - 1}\n"
                                        )
                                    )
                                )
                                val id = getSongsId(m)
                                //访问https://music.163.com/song/media/outer/url?id=${id}.mp3
                                val url = "https://music.163.com/song/media/outer/url?id=$id.mp3"
                                group.sendMessage(
                                    messageChainOf(
                                        PlainText(url)
                                    )
                                )

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
                        }else{
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 请输入相关指令！\n"
                                    )
                                )
                            )
                        }
                    }

                    message.contentToString().startsWith("精准维基")->{
                        val m = message.contentToString().replace("精准维基", "").replace("\\s".toRegex(), "")
                        if(m.isNotEmpty()){
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
                                            "\n 查询成功！如果没有则为寻找不到该页\n" +
                                                    "$botName 货币：${coins - 1}\n"
                                        )
                                    )
                                )
                                val result = dwiki(m)
                                val forward: ForwardMessage = buildForwardMessage {
                                    add(botNumber, botName, PlainText(result))
                                }
                                subject.sendMessage(forward)
                            }else{
                                //发送消息
                                group.sendMessage(
                                    messageChainOf(
                                        At(sender) + PlainText(
                                            "\n 查询失败，请签到后再好好试试！\n" +
                                                    "$botName 货币：$coins\n"
                                        )
                                    )
                                )
                            }
                        }else{
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 查询失败，请输入具体参数！\n"
                                    )
                                )
                            )
                        }


                    }

                    message.contentToString().startsWith("相关维基") -> {
                        val m = message.contentToString().replace("相关维基", "").replace("\\s".toRegex(), "")
                        if(m.isNotEmpty()){
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
                                            "\n 查询成功！如果没有则为寻找不到该页\n" +
                                                    "$botName 货币：${coins - 1}\n"
                                        )
                                    )
                                )
                                val result = rwiki(m)
                                val forward: ForwardMessage = buildForwardMessage {
                                    add(botNumber, botName, PlainText(result))
                                }
                                subject.sendMessage(forward)
                            }else{
                                //发送消息
                                group.sendMessage(
                                    messageChainOf(
                                        At(sender) + PlainText(
                                            "\n 查询失败，请签到后再好好试试！\n" +
                                                    "$botName 货币：$coins\n"
                                        )
                                    )
                                )
                            }
                        }else{
                            //发送消息
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 查询失败，请输入具体参数！\n"
                                    )
                                )
                            )
                        }

                    }

                    message.contentToString().startsWith("问答") -> {
                        var input = message.contentToString().replace("问答", "").replace("\\s".toRegex(), "")
                        if (input.isEmpty()) {
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 失败，请输入具体参数！\n"
                                    )
                                )
                            )
                        }else {
                            //从status.json中获取send的coins
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
                                            "\n 接受成功！\n" +
                                                    "$botName 货币：${coins - 1}\n"
                                        )
                                    )
                                )
                                val content = mly(input,2,user,sender.nick,group.name,group.name)
                                group.sendMessage(At(sender)+messageChainOf(PlainText(content)))
                            } else {
                                //发送消息
                                group.sendMessage(
                                    messageChainOf(
                                        At(sender) + PlainText(
                                            "\n 接受失败，请签到后再好好试试！\n" +
                                                    "$botName 货币：$coins\n"
                                        )
                                    )
                                )
                            }
                        }
                    }

                    /**
                    message.contentToString().startsWith("问答") -> {
                    val m = message.contentToString().replace("问答", "")
                    val responseMessage = tbp(m)
                    group.sendMessage(messageChainOf(PlainText(responseMessage)))
                    }
                     */


                    //游戏类
                    message.contentToString().startsWith("猜数字") -> {
                        var input = message.contentToString().replace("猜数字", "").replace("\\s".toRegex(), "")
                        if (input.isNotEmpty()) {
                            //获取$user.json
                            val user = JSONObject(File("$dataFolder/user/${sender.id}.json").readText())
                            var guess_status = user.getInt("guess_status")
                            var guess_number = user.getInt("guess_number")
                            //还有次数的玩家
                            if (guess_status != 0) {
                                when  {
                                    input.startsWith("开始") -> {
                                        group.sendMessage(
                                            messageChainOf(
                                                At(sender) + PlainText(
                                                    "\n 您已在游戏中，或结束游戏！\n"
                                                )
                                            )
                                        )
                                    }
                                    input.startsWith("结束") -> {
                                        user.put("guess_status", 0)
                                        user.put("guess_number", 0)
                                        File("$dataFolder/user/${sender.id}.json").writeText(user.toString())
                                        group.sendMessage(
                                            messageChainOf(
                                                At(sender) + PlainText(
                                                    "\n 游戏结束！\n" +
                                                            "$botName 欢迎下次使用!"
                                                )
                                            )
                                        )
                                    }
                                    else -> {
                                        //获取数字
                                        val number = input.toInt()
                                        //判断是否猜对
                                        if (number == guess_number) {
                                            user.put("guess_status", 0)
                                            user.put("guess_number", 0)
                                            val coins = user.getInt("coins") + 10
                                            user.put("coins", coins)
                                            File("$dataFolder/user/${sender.id}.json").writeText(user.toString())
                                            group.sendMessage(
                                                messageChainOf(
                                                    At(sender) + PlainText(
                                                        "\n $botName 恭喜你猜对了！\n" +
                                                                "您获得了10金币！\n" +
                                                                "您的金币数为：$coins\n" +
                                                                "欢迎下次使用!"
                                                    )
                                                )
                                            )
                                        } else {
                                            //判断是否大于猜测数字
                                            if (number > guess_number) {
                                                guess_status = guess_status - 1
                                                group.sendMessage(
                                                    messageChainOf(
                                                        At(sender) + PlainText(
                                                            "\n 猜测数字大了！\n" +
                                                                    "您的猜测数字：$number\n" +
                                                                    "您还有$guess_status 次机会！\n"
                                                        )
                                                    )
                                                )
                                                user.put("guess_status", guess_status)
                                                File("$dataFolder/user/${sender.id}.json").writeText(user.toString())
                                            } else {
                                                guess_status = guess_status - 1
                                                group.sendMessage(
                                                    messageChainOf(
                                                        At(sender) + PlainText(
                                                            "\n 猜测数字小了！\n" +
                                                                    "您的猜测数字：$number\n" +
                                                                    "您还有$guess_status 次机会！\n"
                                                        )
                                                    )
                                                )
                                                user.put("guess_status", guess_status)
                                                File("$dataFolder/user/${sender.id}.json").writeText(user.toString())
                                            }
                                        }
                                    }
                                }
                            }

                            //次数已用尽或未开开始的用户
                            else{
                                when{
                                    input.startsWith("开始") -> {
                                        guess_status = 10
                                        guess_number = (1..100).random()
                                        group.sendMessage(
                                            messageChainOf(
                                                At(sender) + PlainText(
                                                    "\n 游戏开始！\n" +
                                                            "您有$guess_status 次机会！\n"
                                                )
                                            )
                                        )
                                        user.put("guess_status", guess_status)
                                        user.put("guess_number", guess_number)
                                        File("$dataFolder/user/${sender.id}.json").writeText(user.toString())
                                    }else -> {  //没有开始游戏
                                    group.sendMessage(
                                        messageChainOf(
                                            At(sender) + PlainText(
                                                "\n 请先开始游戏！\n"
                                            )
                                        )
                                    )
                                    }
                                }
                            }

                        }else{
                            group.sendMessage(
                                messageChainOf(
                                    At(sender) + PlainText(
                                        "\n 请在猜数字后面加入指定命令！\n"
                                    )
                                )
                            )
                        }
                    }

                    message.contentToString().startsWith("关于") -> {
                        group.sendMessage(messageChainOf(PlainText("项目地址：https://github.com/greenhandzdl/mirai-plugins-sayBack")))
                    }
                }
            }
        }

        override fun onDisable() {
            super.onDisable()
            logger.info("sayBack onDisable")
        }
    }

fun getSongsId(m: String): String{
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("http://music.cyrilstudio.top/search?keywords=$m&limit=1")
        .get()
        .build()
    val response = OkHttpClient().newCall(request).execute()
    val responseBody = response.body?.string()
    val json = JSONObject(responseBody)
    //判空responseBody
    if (responseBody != null) {
        // 获取result[songs[{id}]]
        val result = json.getJSONObject("result")
        val songs = result.getJSONArray("songs")
        // 提取songs[{id}]
        val id = songs.getJSONObject(0).getInt("id")
        return id.toString()
    }else{
        return "没有搜索到歌曲"
    }
}

fun dwiki(m: String): String {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://zh.wikipedia.wikimirror.org/api/rest_v1/page/html/$m?redirect=true&stash=false")
        .get()
        .build()
    val response = OkHttpClient().newCall(request).execute()
    val responseBody = response.body?.string()
    //去除responseBody中的html标签，并换行
    if(!responseBody.isNullOrEmpty()){
        val result = responseBody.replace("<[^>]*>".toRegex(), "").replace("\n", "")
        return result
    }else{
        return "查询失败，请签到后再好好试试！"
    }
}
fun rwiki(m :String) :String{
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://zh.wikipedia.wikimirror.org/api/rest_v1/page/related/$m")
        .get()
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

fun mly(message :String,type :Int,userId :String,userName :String,groupId :String,groupName :String) :String{
    //读取/mly.json文件
    val rjson = JSONObject(File("$configFolder/mly.json").readText())
    //获取api_key,api_secret
    val api_key = rjson.getString("api_key")
    val api_secret = rjson.getString("api_secret")

    val mollyUrl = "https://i.mly.app/reply"
    val connection = URL(mollyUrl).openConnection() as HttpsURLConnection

    // 设置请求
    connection.requestMethod = "POST"
    connection.connectTimeout = 5000
    connection.doOutput = true
    connection.doInput = true
    connection.useCaches = false
    connection.instanceFollowRedirects = true

    // 设置Api请求头
    connection.setRequestProperty("Api-Key", api_key)
    connection.setRequestProperty("Api-Secret", api_secret)
    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")

    //将json请求写入连接
    val json = """
        {
            "content": "$message",
            "type": $type,
            "from": "$userId",
            "fromName": "$userName",
            "to": "$groupId",
            "toName": "$groupName"
        }
    """.trimIndent()
    connection.outputStream.write(json.toByteArray())
    connection.outputStream.flush()
    connection.outputStream.close()

    // 测试中判断网络：val responseCode = connection.responseCode

    // 读取返回的数据
    val inputStream = connection.inputStream
    val inputStreamReader = inputStream.reader()
    val response = inputStreamReader.readText()
    val jsonR = JSONObject(response)
    //读取response里面的内容{data[{content}]}
    val data = jsonR.getJSONArray("data")
    val content = data.getJSONObject(0).getString("content")

    // 关闭连接
    inputStream.close()
    inputStreamReader.close()
    connection.disconnect()

    return content
}



/**
fun tbp(m :String) : String{
    val json = JSONObject(File("$configFolder/TBP.json").readText())
    //获取数据
    val SecretId = json.getString("SecretId")
    val Signature = json.getString("Signature")
    val BotId = json.getString("BotId")
    val BotEnv = json.getString("BotEnv")
    val TerminalId = json.getString("TerminalId")
    val Nonce = Random.nextInt(0, 100000) //正整数的随机值
    val client = okhttp3.OkHttpClient()
    val requestBody = FormBody.Builder()
        .add("SecretId", SecretId)
        .add("Signature", Signature)
        .add("BotId", BotId)
        .add("BotEnv", BotEnv)
        .add("TerminalId", TerminalId)
        .add("Version","2019-06-27")
        .add("Action","TextProcess")
        .add("Timestamp", Clock.System.now().epochSeconds.toString())
        .add("Nonce", Nonce.toString())
        .add("InputText", m)
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
**/