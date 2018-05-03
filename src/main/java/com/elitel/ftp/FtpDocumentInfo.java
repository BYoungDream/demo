package com.elitel.ftp;

import java.io.InputStream;

/**
 * FTP文件信息
 * created by guoyanfei on 2018/4/16
 */
public class FtpDocumentInfo {
    //文件名称
    private String filename;
    //文件路径
    private String filedirectory;
    //文件大小
    private long filesize;
    //文件最后更新时间
    private String modifytime;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiledirectory() {
        return filedirectory;
    }

    public void setFiledirectory(String filedirectory) {
        this.filedirectory = filedirectory;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
    }

}
