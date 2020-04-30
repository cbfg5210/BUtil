package com.util

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * This is a Log tool，with this you can the following
 *
 *  1. use KLog.d(),you could print whether the method execute,and the default tag is current class's name
 *  1. use KLog.d(msg),you could print log as before,and you could location the method with a click in Android Studio Logcat
 *  1. use KLog.json(),you could print json string with well format automatic
 *
 *
 * @author zhaokaiqiang
 * github https://github.com/ZhaoKaiQiang/KLog
 * 15/11/17 扩展功能，添加对文件的支持
 * 15/11/18 扩展功能，增加对XML的支持，修复BUG
 * 15/12/8  扩展功能，添加对任意参数的支持
 * 15/12/11 扩展功能，增加对无限长字符串支持
 * 16/6/13  扩展功能，添加对自定义全局Tag的支持,修复内部类不能点击跳转的BUG
 * 16/6/15  扩展功能，添加不能关闭的KLog.debug(),用于发布版本的Log打印,优化部分代码
 */
object KLog {
    private const val LINE_SEPARATOR = "/"
    private const val NULL_TIPS = "Log with null object"
    private const val TAG_DEFAULT = "KLog"
    private const val FILE_PREFIX = "KLog_"
    private const val FILE_FORMAT = ".log"

    private const val MAX_LENGTH = 4000
    private const val JSON_INDENT = 4
    private const val STACK_TRACE_INDEX = 5

    const val V = 0x1
    const val D = 0x2
    const val I = 0x3
    const val W = 0x4
    const val E = 0x5
    const val A = 0x6

    private const val JSON = 0x7

    //XML=JSON+E
    private const val XML = 0xC

    private var globalTag: String = TAG_DEFAULT
    private var isShowLog = false

    fun init(isShowLog: Boolean, tag: String = TAG_DEFAULT) {
        this.isShowLog = isShowLog
        this.globalTag = tag
    }

    fun v(msg: Any?) {
        printLog(V, null, msg)
    }

    fun v(tag: String, objects: Any?) {
        printLog(V, tag, objects)
    }

    fun d(msg: Any?) {
        printLog(D, null, msg)
    }

    fun d(tag: String, objects: Any?) {
        printLog(D, tag, objects)
    }

    fun i(msg: Any?) {
        printLog(I, null, msg)
    }

    fun i(tag: String, objects: Any?) {
        printLog(I, tag, objects)
    }

    fun w(msg: Any?) {
        printLog(W, null, msg)
    }

    fun w(tag: String, objects: Any?) {
        printLog(W, tag, objects)
    }

    fun e(msg: Any?) {
        printLog(E, null, msg)
    }

    fun e(tag: String, objects: Any?) {
        printLog(E, tag, objects)
    }

    fun a(msg: Any?) {
        printLog(A, null, msg)
    }

    fun a(tag: String, objects: Any?) {
        printLog(A, tag, objects)
    }

    fun json(level: Int, jsonFormat: String) {
        printLog(JSON + level, null, jsonFormat)
    }

    fun json(level: Int, tag: String, jsonFormat: String) {
        printLog(JSON + level, tag, jsonFormat)
    }

    fun xml(level: Int, xml: String) {
        printLog(XML + level, null, xml)
    }

    fun xml(level: Int, tag: String, xml: String) {
        printLog(XML + level, tag, xml)
    }

    fun map(level: Int, map: Map<String, Any>) {
        printLog(JSON + level, null, map)
    }

    fun mapString(level: Int, tag: String, map: Map<String, String>) {
        printLog(JSON + level, tag, map)
    }

    fun map(level: Int, tag: String, map: Map<String, Any>?) {
        if (map.isNullOrEmpty()) {
            printLog(level, tag, null)
            return
        }
        val jsonObject = JSONObject()
        for ((key, value) in map) {
            jsonObject.put(key, value.toString())
        }
        printLog(JSON + level, tag, jsonObject.toString())
    }

    fun file(targetDirectory: File, msg: Any) {
        printFile(null, targetDirectory, null, msg)
    }

    fun file(tag: String, targetDirectory: File, msg: Any) {
        printFile(tag, targetDirectory, null, msg)
    }

    fun file(tag: String, targetDirectory: File, fileName: String, msg: Any) {
        printFile(tag, targetDirectory, fileName, msg)
    }

    fun debug(msg: Any) {
        printDebug(null, msg)
    }

    fun debug(tag: String, objects: Any) {
        printDebug(tag, objects)
    }

    /*
     * log内容处理
     * */
    private fun wrapperContent(tagStr: String?, objects: Any?): Array<String> {
        val targetElement = Thread.currentThread().stackTrace[STACK_TRACE_INDEX]
        val className = targetElement.fileName
        val methodName = targetElement.methodName
        var lineNumber = targetElement.lineNumber

        if (lineNumber < 0) {
            lineNumber = 0
        }

        val tag = tagStr ?: globalTag
        val msg = objects?.toString() ?: NULL_TIPS
        val headString = "[($className:$lineNumber)#$methodName] "

        return arrayOf(tag, msg, headString)
    }

    /*
     * log输出
     * */
    private fun printLog(type: Int, tagStr: String?, objects: Any?) {
        if (!isShowLog) {
            return
        }

        val contents = wrapperContent(tagStr, objects)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]

        when (type) {
            V, D, I, W, E, A -> checkLenPrint(type, tag, headString + msg)
            JSON + V, JSON + D, JSON + I, JSON + W, JSON + E -> printJson(type, tag, msg, headString)
            XML + V, XML + D, XML + I, XML + W, XML + E -> printXml(type, tag, msg, headString)
        }
    }

    private fun printDebug(tagStr: String?, objects: Any) {
        val contents = wrapperContent(tagStr, objects)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]
        checkLenPrint(D, tag, headString + msg)
    }

    /**
     * 检查长度，如果超出MAX_LENGTH则分段输出
     *
     * @param type
     * @param tag
     * @param msg
     */
    private fun checkLenPrint(type: Int, tag: String, msg: String) {
        val length = msg.length
        val countOfSub = length / MAX_LENGTH

        if (countOfSub == 0) {
            //length<MAX_LENGTH
            print(type, tag, msg)
            return
        }
        //length>MAX_LENGTH
        var index = 0
        for (i in 0 until countOfSub) {
            val sub = msg.substring(index, index + MAX_LENGTH)
            print(type, tag, sub)
            index += MAX_LENGTH
        }
        print(type, tag, msg.substring(index, length))
    }

    private fun print(type: Int, tag: String, sub: String) {
        when (type) {
            V -> Log.v(tag, sub)
            D -> Log.d(tag, sub)
            I -> Log.i(tag, sub)
            W -> Log.w(tag, sub)
            E -> Log.e(tag, sub)
            A -> Log.wtf(tag, sub)
        }
    }

    private fun printJsonXml(level: Int, tag: String, msg: String, headString: String) {
        print(level, tag, "╔═══════════════════════════════════════════════════════════════════════════════════════")

        val message = headString + LINE_SEPARATOR + msg

        if (message.length > MAX_LENGTH) {
            checkLenPrint(level, tag, message)
        } else {
            val lines = message.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (line in lines) {
                print(level, tag, "║ $line")
            }
        }

        print(level, tag, "╚═══════════════════════════════════════════════════════════════════════════════════════")
    }

    /*
     * file
     * */

    /**
     * @param tagStr
     * @param targetDirectory
     * @param fileName
     * @param objectMsg
     */
    private fun printFile(tagStr: String?, targetDirectory: File, fileName: String?, objectMsg: Any) {
        if (!isShowLog) {
            return
        }
        val contents = wrapperContent(tagStr, objectMsg)
        val tag = contents[0]
        val msg = contents[1]
        val headString = contents[2]

        printFile(tag, targetDirectory, fileName, headString, msg)
    }

    /**
     * @param tag
     * @param targetDirectory
     * @param fileName
     * @param headString
     * @param msg
     */
    private fun printFile(tag: String, targetDirectory: File, fileName: String?, headString: String, msg: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA)
        val nFileName =
                if (TextUtils.isEmpty(fileName)) FILE_PREFIX + formatter.format(System.currentTimeMillis()) + FILE_FORMAT
                else fileName!!

        if (save(targetDirectory, nFileName, msg)) {
            Log.d(tag, headString + " save log success ! location is >>>" + targetDirectory.absolutePath + "/" + nFileName)
        } else {
            Log.e(tag, headString + "save log fails !")
        }
    }

    /**
     * @param dic
     * @param fileName
     * @param msg
     * @return
     */
    private fun save(dic: File, fileName: String, msg: String): Boolean {
        val file = File(dic, fileName)

        return try {
            val outputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(outputStream, "UTF-8")
            outputStreamWriter.write(msg)
            outputStreamWriter.flush()
            outputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 输出json格式
     * @param type Int
     * @param tag String
     * @param msg String
     * @param headString String
     */
    private fun printJson(type: Int, tag: String, msg: String, headString: String) {
        val message: String = try {
            when {
                msg.startsWith("{") -> JSONObject(msg).toString(JSON_INDENT)
                msg.startsWith("[") -> JSONArray(msg).toString(JSON_INDENT)
                else -> msg
            }
        } catch (e: JSONException) {
            msg
        }

        printJsonXml(type - JSON, tag, message, headString)
    }

    /*
     * xml
     * */
    private fun printXml(type: Int, tag: String, xml: String, headString: String) {
        val nXml = if (TextUtils.isEmpty(xml)) NULL_TIPS else formatXML(xml)
        printJsonXml(type - XML, tag, nXml, headString)
    }

    private fun formatXML(inputXML: String): String {
        return try {
            val xmlInput = StreamSource(StringReader(inputXML))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.transform(xmlInput, xmlOutput)
            xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
        } catch (e: Exception) {
            e.printStackTrace()
            inputXML
        }
    }
}