package com.thunisoft.zipper.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.zip.ZipInputStream;

public class CreateZipUtil {
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(CreateZipUtil.class);

    /**
     * 创建ZIP文件
     *
     * @param sourcePath 文件或文件夹路径
     * @param zipPath    生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String zipPath) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            zos.setEncoding("gbk");// 此处修改字节码方式。
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            LOG.error("创建ZIP文件失败", e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(zos);
        }
    }

    /**
     * 不打包第一目录
      * @param file
     * @param parentPath
     * @param zos
     */
    private static void writeZip(File file, String parentPath,
                                 ZipOutputStream zos) {
        File fa[] = file.listFiles();
        for (int i = 0; i < fa.length; i++) {
            if (fa[i].exists()) {
                if (fa[i].isDirectory()) {// 处理文件夹
                    parentPath = fa[i].getName() + File.separator;
                    File[] files = fa[i].listFiles();
                    if (files.length != 0) {
                        for (File f : files) {
                            writeZipson(f, parentPath, zos);
                        }
                    } else { // 空目录则创建当前目录
                        try {
                            zos.putNextEntry(new ZipEntry(parentPath));
                        } catch (IOException e) {
                            LOG.error("创建ZIP文件失败", e);
                        }
                    }
                } else {
                    parentPath = "";
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(fa[i]);
                        ZipEntry ze = new ZipEntry(parentPath + fa[i].getName());
                        zos.putNextEntry(ze);
                        byte[] content = new byte[1024];
                        int len;
                        while ((len = fis.read(content)) != -1) {
                            zos.write(content, 0, len);
                            zos.flush();
                        }

                    } catch (FileNotFoundException e) {
                        LOG.error("创建ZIP文件失败", e);
                    } catch (IOException e) {
                        LOG.error("创建ZIP文件失败", e);
                    } finally {
                        try {
                            if (fis != null) {
                                fis.close();
                            }
                        } catch (IOException e) {
                            LOG.error("创建ZIP文件失败", e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 打包从第一目录开始打包
      * @param file
     * @param parentPath
     * @param zos
     */
    private static void writeZipson(File file, String parentPath,
                                    ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {// 处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files.length != 0) {
                    for (File f : files) {
                        writeZipson(f, parentPath, zos);
                    }
                } else { // 空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        LOG.error("创建ZIP文件失败", e);
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }
                } catch (Exception e) {
                    LOG.error("创建ZIP文件失败", e);
                }  finally {
                    org.apache.commons.io.IOUtils.closeQuietly(fis);
                }
            }
        }
    }



}
