package com.greenhandzdl

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object SayBack : KotlinPlugin(
    JvmPluginDescription(
        id = "com.greenhandzdl:mirai-plugins-sayBack",
        name = "sayBack",
        version = "0.0.1",
    ) {
        author("greenhandzdl")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}