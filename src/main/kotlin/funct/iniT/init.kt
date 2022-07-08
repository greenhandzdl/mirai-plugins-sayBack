package com.greenhandzdl.funct.iniT

import com.greenhandzdl.SayBack
import com.greenhandzdl.SayBack.configFolder
import com.greenhandzdl.funct.core.checkJSON


fun iniT{
    val configFolder= configFolder
    //必要文件初始化
    checkJSON("$configFolder","config.json","botName","MiraiAutoReply")
}