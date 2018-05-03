package com.elitel;

import com.elitel.ftp.FtpDocumentInfo;
import com.elitel.ftp.FtpUtil;
import com.elitel.quartz.HelloTestQuartz;
import com.elitel.tika.TikaUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.quartz.JobBuilder.newJob;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
//        perfromElastic();
//        perfromQuartz();
//        perfromTika();

        iktest();

    }

    /**
     * 调用elasticsearch
     */
    public static void perfromElastic(){
        ElasticSearchTest estest = new ElasticSearchTest();
        estest.connClient();
//        estest.addIndex();
//        estest.getIndex();
//        estest.deleteIndex();
//        estest.updateIndex();
//        estest.search();
//        estest.searchTemplate();
//        estest.searchPage();
//        estest.searchRefrection();
        estest.searchPagemoreIndex();
//        estest.deleteIndex("searchall");
        estest.closeClient();

//        ESClient esClient = new ESClient("127.0.0.1",9300);
//        String searchFields = "adress";
//        String searchText = "李四";
//        ESPageResponse<Map<String,Object>> esPageResponse =
//        esClient.searchDocument("singtable","mapdata",1,10
//        ,searchFields,searchText);
//        esClient.closeClient();
//
//        System.out.println(esPageResponse.getRows());
//        System.out.println(esPageResponse.getDescription());
    }

    /**
     * 定时任务quartz
     */
    public static void perfromQuartz(){
        //调用定时任务
        try{
            //创建scheduler
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


            //定义一个Trigger
//            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1") //定义name/group
//                    .startNow()//一旦加入scheduler，立即生效
//                    .withSchedule(simpleSchedule() //使用SimpleTrigger
//                            .withIntervalInSeconds(1) //每隔一秒执行一次
//                            .repeatForever()) //一直执行，奔腾到老不停歇
//                    .build();
//            Trigger trigger = CronScheduleBuilder
//                    .cronSchedule("0 34 11 * * ? *")
//                    .build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1") //定义name/group
                    .startNow()//一旦加入scheduler，立即生效
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?")) //一直执行，奔腾到老不停歇
                    .build();

            //定义一个JobDetail
            JobDetail job = newJob(HelloTestQuartz.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
                    .withIdentity("job1", "group1") //定义name/group
                    .usingJobData("serviceId", "testSearch1") //定义属性
                    .build();

            //加入这个调度
            scheduler.scheduleJob(job, trigger);

            //启动
            scheduler.start();

            //运行一段时间后关闭
            Thread.sleep(500000);
            scheduler.shutdown(true);

        }catch (Exception e){

        }
    }

    /**
     * 执行tika
     */
    public static void perfromTika(){
        //本地临时存储文件路径
        String localpath = System.getProperty("user.dir")+"\\tempfile";

        //1、获取FTP服务器文件信息
        FtpUtil ftpUtil = new FtpUtil("192.168.0.164","elitel","elitel",21);
        List<FtpDocumentInfo> ftpDocumentInfos = ftpUtil.getFtpfile("testDocument/201804181554");

        //2、从FTP服务器下载到本地并通过Tika读取内容
        if(ftpDocumentInfos != null && ftpDocumentInfos.size() > 0){
            for (FtpDocumentInfo fdi:ftpDocumentInfos) {
                //下载文件
                Boolean isdown = ftpUtil.downloadFtpFile(fdi.getFiledirectory(),localpath,fdi.getFilename());
                if(isdown){
                    //读取文件内容
                    String context = TikaUtil.getPlaintext(localpath+"\\"+fdi.getFilename());
                    System.out.println("*********Begin文件:"+fdi.getFilename()+"*******");
                    System.out.println(context.replace(" ",""));
                    System.out.println("*********End文件:"+fdi.getFilename()+"********");

                    //删除本地存储临时文件
                    File tempFile = new File(localpath+"\\"+fdi.getFilename());
                    if(tempFile.exists()){
                        tempFile.delete();
                    }

                }else {
                    System.out.println("下载失败："+fdi.getFilename());
                }

            }
        }
        ftpUtil.closeFtpClient();


    }

    /**
     * IK分词
     */
    public static void iktest(){
        String context = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节。然后 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个贱人一杯红酒一部电影在夜 深人静的晚上，关上电话静静的发呆着";
        List<String> list = new ArrayList<String>();
        StringReader re = new StringReader(context);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        try {
            while ((lex = ik.next()) != null) {
                System.out.println(lex.getLexemeText());
            }
        }catch (IOException e){
            e.printStackTrace();
        }


    }

}
