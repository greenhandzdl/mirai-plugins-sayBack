package com.greenhandzdl

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf


object SayBack : KotlinPlugin(
    JvmPluginDescription(id = "com.greenhandzdl.mirai-plugins-sayBack", name = "sayBack", version = "0.0.1") {
        author("greenhandzdl")
        info("sayBack")
    }
)
/***
{
    override fun onEnable() {
        super.onEnable()
        logger.info { "SayBack Plugin loaded" }
        //PluginData.reload()
        sayBack.register()
        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<MessageEvent> {
            sayBack.replyMeta = message.quote()
        }
    }
    override fun onDisable() {
        super.onDisable()
        logger.info { "SayBack Plugin unloaded" }
        sayBack.unregister()
    }
}

object sayBack : CompositeCommand(
    SayBack,"SayBack",
    description = "不知道干什么的自动回复Bot"
) {
    lateinit var replyMeta: QuoteReply

    @OptIn(ExperimentalSerializationApi::class)
    @SubCommand
    suspend fun CommandSender.recall(messager: String = "") {
        when {
            messager.startsWith("复读") -> {
                trySendReplyMsg(messageChainOf(PlainText(messager)))
            }
        }

    }
}

private suspend fun trySendReplyMsg(msgChain: MessageChain, subject: Contact?=null, replyMeta:QuoteReply?=null): MessageReceipt<Contact>? {
    return if (replyMeta!=null){
        subject?.sendMessage(messageChainOf(replyMeta,msgChain))
    } else{
        subject?.sendMessage(msgChain)
    }
}
***/
{
    override fun onEnable() {
        super.onEnable()
        logger.info("sayBack onEnable")
        globalEventChannel().subscribeAlways<GroupMessageEvent>{
            when{
                (message.contentToString() == "菜单") -> {
                    group.sendMessage("MiraiAutoReply 菜单 \n" +
                            "功能类：\n" +
                                "* 复读\n"    +
                            "游戏类：\n"        +
                                "* 猜数字\n"    +
                            "关于\n"
                    )
                }
                message.contentToString().startsWith("复读") -> {
                   var m = message.contentToString()
                    m = m.replace("复读","")
                    group.sendMessage(messageChainOf(PlainText(m)))
                }
                message.contentToString().startsWith("猜数字") -> {
                    //获取消息发送人QQ号
                    var sender = sender.id
                    group.sendMessage(messageChainOf(At(sender) +PlainText("注意：当游戏开始时，您只能通过输入quit退出，其余一律视为游戏次数")))
                    val num = (1..100).random()
                    group.sendMessage("游戏开始，请输入数字在[1,100]，输入quit退出")
                    var count = 0   //游戏次数
                   //获取sender输入

            }
        }
        globalEventChannel().subscribeAlways<FriendMessageEvent>{

        }
    }

    fun onDisable() {
        super.onDisable()
        logger.info("sayBack onDisable")
    }
}
}
