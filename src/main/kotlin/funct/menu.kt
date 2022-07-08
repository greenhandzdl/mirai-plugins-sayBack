package com.greenhandzdl.Hit

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf

class menu {
    suspend fun menu(subject :Group) :Any? {
        subject.sendMessage(
            messageChainOf(
                PlainText(
                    "菜单 \n" +
                            "好感度升级类\n" +
                            "* 签到\n" +
                            "功能类：\n" +
                            "* 复读 + 复读文本\n" +
                            "* 好图 \n" +
                            "* 网易云 歌名 \n" +
                            "* 精准维基 + 检查文本\n" +
                            "* 相关维基 + 检查文本\n" +
                            "* 问答 + 文本\n" +
                            "游戏类：\n" +
                            "* 猜数字 +\n" +
                            "~ 开始|结束\n" +
                            "~ 所猜数字大小[0,100],10次机会\n" +
                            "其他：\n" +
                            "* 关于\n"

                )
            )
        )
        return "true"
    }
}