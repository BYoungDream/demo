package com.elitel.tika;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tika检索文件内容
 * created by guoyanfei on 2018/4/16
 */
public class TikaUtil {

    /**
     * 使用parser获取文档内容
     * created by guoyanfei on 2018/04/16
     * @param filePath 本地文件路径
     * @return 文档内容
     */
    public static String getContextParser(String filePath){
        String context="";
        InputStream fileInputStream = null;
        try{
            File file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(new WriteOutContentHandler(1024*1024*1024));
            Metadata metadata = new Metadata();
            metadata.set(Metadata.CONTENT_ENCODING, "utf-8");
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
            ParseContext parseContext=new ParseContext();
            parser.parse(fileInputStream,handler,metadata,parseContext);
            //打印元数据
//            for(String name:metadata.names()) {
//                System.out.println(name+":"+metadata.get(name));
//            }
            context = handler.toString();

        }catch (TikaException tke){
            tke.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch (SAXException saxe){
            saxe.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        return context;
    }

    /**
     * 获取简单纯文本内容
     * created by guoyanfei on 2018/04/16
     * @param filePath 本地文档路径
     * @return 文本内容
     */
    public static String getPlaintext(String filePath){
        String context = "";
        try {
            Tika tika = new Tika();
            context = tika.parseToString(new File(filePath));
        }catch (IOException e){
            e.printStackTrace();
        }catch (TikaException te){
            te.printStackTrace();
        }
        return context;
    }

}
