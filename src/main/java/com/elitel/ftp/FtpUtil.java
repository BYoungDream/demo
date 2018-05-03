package com.elitel.ftp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ftp上传、下载文件操作工具类
 * created by guoyanfei on 2018/4/13
 */
public class FtpUtil {
    private FTPClient ftpClient = null;
    private List<FtpDocumentInfo> filelist = new ArrayList<FtpDocumentInfo>();

    /**
     * 初始化ftp客户端
     * created by guoyanfei on 2018/04/13
     * @param ftpHost FTP主机服务器
     * @param ftpUser FTP登录用户
     * @param ftpPass FTP登录密码
     * @param ftpPort FTP端口号
     */
    public FtpUtil(String ftpHost,
                   String ftpUser,String ftpPass,int ftpPort){
        try{
            ftpClient = new FTPClient();
            //连接ftp服务器
            ftpClient.connect(ftpHost,ftpPort);
            //验证连接ftp服务器
            int reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)){
                ftpClient.disconnect();
                System.out.println("FTP服务器拒绝连接!");
            }else{
                System.out.println("FTP服务器连接成功!");
            }
            //登录FTP服务器
            if(!ftpClient.login(ftpUser,ftpPass)){
                ftpClient.logout();
                System.out.println("FTP服务器登录失败");
            }else {
                System.out.println("FTP服务器登录成功");
            }

        }catch (IOException e){
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                }catch (IOException f){
                    f.printStackTrace();
                }
            }
            e.printStackTrace();
        }

    }

    /**
     * 关闭ftp客户端
     * created by guoyanfei on 2018/04/13
     */
    public void closeFtpClient(){
        if(ftpClient.isConnected()){
            try{
                ftpClient.disconnect();
                System.out.println("ftp客户端已关闭!");
            }catch (IOException f){
                f.printStackTrace();
            }
        }
    }

    /**
     * show 从FTP服务器下载文件
     * created by guoyanfei on 2018/04/13
     * @param ftpPath ftp文件路径
     * @param loaclPath 本地文件路径
     * @param filename 文件名称
     * @return true或false
     */
    public Boolean downloadFtpFile(String ftpPath,String loaclPath,String filename){
        Boolean issuccess=false;
        OutputStream os = null;
        try{
            //设置中文支持
            ftpClient.setControlEncoding("UTF-8");
            //切换到根目录
            ftpClient.changeWorkingDirectory("/");
            //切换到文件目录
            ftpClient.changeWorkingDirectory(ftpPath);
            File localfile = new File(loaclPath+"\\"+filename);
            os = new BufferedOutputStream(new FileOutputStream(localfile));
            issuccess = ftpClient.retrieveFile(new String(filename.getBytes("UTF-8"),"ISO-8859-1"),os);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(os != null){
                try{
                    os.flush();
                    os.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return issuccess;
    }

    /**
     * show 上传文件到ftp服务器
     * @param ftpPath 上传到ftp路径
     * @param filename 文件名称
     * @param inputStream 上传文件流
     * @return true或false
     */
    public Boolean uploadFtpFile(String ftpPath, String filename, InputStream inputStream){
        Boolean issuccess = false;
        try{
            //设置中文支持
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.changeWorkingDirectory(ftpPath);

            issuccess = ftpClient.storeFile(filename, inputStream);
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return issuccess;
    }

    /**
     * 获取ftp服务器上文件目录
     * created by guoyanfei on 2018/04/13
     * @param ftpPath ftp服务器路径
     */
    public List<FtpDocumentInfo> getFtpfile(String ftpPath){
        try{
            if(ftpClient.isConnected()){
                ftpClient.setControlEncoding("UTF-8"); // 中文支持
                FTPFile[] ftpFiles = null;
                if(StringUtils.isEmpty(ftpPath)){
                    ftpFiles = ftpClient.listFiles();
                }else{
                    ftpFiles = ftpClient.listFiles(ftpPath);
                }

                for(int i=0;i<ftpFiles.length;i++){
                    FTPFile ftpFile = ftpFiles[i];
                    if(!ftpFile.getName().equals(".") && !ftpFile.getName().equals(".."))
                    {
                        if(ftpFile.isFile()){
                            FtpDocumentInfo ftpDocumentInfo = new FtpDocumentInfo();
                            ftpDocumentInfo.setFilename(ftpFile.getName());
                            ftpDocumentInfo.setFiledirectory(ftpPath);
                            ftpDocumentInfo.setFilesize(ftpFile.getSize());
                            DateFormat formatter = DateFormat.getDateTimeInstance();
                            String modifytime = formatter.format(ftpFile.getTimestamp().getTime());
                            ftpDocumentInfo.setModifytime(modifytime);

                            filelist.add(ftpDocumentInfo);
                        }
                        if(ftpFile.isDirectory()){
                            getFtpfile(ftpPath+"/"+ftpFile.getName());
                        }
                    }
                }
            }else{
                System.out.println("ftp服务连接失败!");
            }

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("ftp服务连接失败");
        }

        return filelist;

    }

}
