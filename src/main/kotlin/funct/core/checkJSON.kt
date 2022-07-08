package com.greenhandzdl.funct.core


import org.json.JSONObject
import java.io.File


/***
 * 这是什么？
 *  检查工具（用于json检查值是否存在，否则创建）
 */


fun checkJSON(pathName :String,fileName:String = "test.json",key:String = "checkJSON",keyValue:String = "0" ) {
    val folder = File(pathName)
    val file = File(pathName + File.separator + fileName)
    val json = JSONObject()
    if(!folder.exists()){
        folder.mkdir()
        file.createNewFile()
        json.put(key, keyValue)
        File("$file").writeText(json.toString())
    }else{
        if(!file.exists()) {
            file.createNewFile()
            json.put(key, keyValue)
            File("$file").writeText(json.toString())
        }else{
            //如果keyvalue为空，则添加到json中
            if(key.isEmpty()){
                json.put(key, keyValue)
                File("$file").writeText(json.toString())
            }
        }
    }
}