package com.greenhandzdl

import kotlinx.serialization.ExperimentalSerializationApi
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.utils.info



object SayBack : KotlinPlugin(
    JvmPluginDescription(id = "com.greenhandzdl.mirai-plugins-sayBack", name = "sayBack", version = "0.0.1") {
        author("greenhandzdl")
    }
) {
    override fun onEnable() {
        logger.info { "SayBack Plugin loaded" }
        //PluginData.reload()
        sayBack.register()
        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<MessageEvent> {
            sayBack.replyMeta = message.quote()
        }
    }
    override fun onDisable() {
        logger.info { "SayBack Plugin unloaded" }
        sayBack.unregister()
    }
}


object sayBack :CompositeCommand(
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