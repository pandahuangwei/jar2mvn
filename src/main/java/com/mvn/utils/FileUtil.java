package com.mvn.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author panda.
 * @version 1.0.
 * @since 2019-04-20 22:29.
 */
public class FileUtil {

    private static final String TYPE_JAR = "jar";
    private static final String GROUPID_BEGIN = "<groupId>";
    private static final String GROUPID_END = "</groupId>";

    private static final String ARTIFACTID_BEGIN = "<artifactId>";
    private static final String ARTIFACTID_END = "</artifactId>";

    private static final String VERSION_BEGIN = "<version>";
    private static final String VERSION_END = "</version>";

    public static List<String> getAllJarName(String path) {
        List<String> fileName = new ArrayList<>(256);
        getAllFileName(path, fileName, TYPE_JAR);
        return fileName;
    }

    private static void getAllFileName(String path, List<String> fileName, String type) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            if (type == null) {
                fileName.addAll(Arrays.asList(names));
            } else {
                for (String name : names) {
                    if (name.endsWith(type)) {
                        fileName.add(name);
                    }
                }
            }

        }
        if (files == null) {
            System.out.println("没有文件");
            return;
        }
        for (File a : files) {
            if (a.isDirectory()) {
                getAllFileName(a.getAbsolutePath(), fileName, type);
            }
        }
    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static String readGroupId(String src) {
        return subStr(src, GROUPID_BEGIN, GROUPID_END);
    }

    public static String readArtifactId(String src) {
        return subStr(src, ARTIFACTID_BEGIN, ARTIFACTID_END);
    }

    public static String readVersion(String src) {
        return subStr(src, VERSION_BEGIN, VERSION_END);
    }

    public static void writeTxt(String path, String txtName, String... contents) {
        txtName = txtName + "_" + getTimestamp() + ".txt";
        FileWriter fw;
        File file = new File(path + "\\" + txtName);
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            int i = 1;
            for (String txt : contents) {
                bw.append(txt);
                bw.append("\n\n");
                bw.append("==========part_"+i+"=======\n");
                i++;
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String subStr(String src, String beginStr, String endStr) {
        int indexBegin = src.indexOf(beginStr);
        int indexEnd = src.indexOf(endStr);
        return src.substring(indexBegin + beginStr.length(), indexEnd);
    }

    private static String getTimestamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    public static void main(String[] args) {
        /*List<String> fileName = getAllJarName("D:\\jartest\\lib");
        System.out.println("共找到:" + fileName.size() + "个文件");
        for (String file : fileName) {
            System.out.println(file);
        }*/

        String result = readToString("D:\\Program Files\\repo\\antlr\\antlr\\2.7.7\\antlr-2.7.7.pom");
        System.out.println(readGroupId(result));
        System.out.println(readArtifactId(result));
        System.out.println(readVersion(result));
    }


}
