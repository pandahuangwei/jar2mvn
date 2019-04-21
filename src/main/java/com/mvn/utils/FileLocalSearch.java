package com.mvn.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author panda.
 * @version 1.0.
 * @since 2019-04-20 22:11.
 */
public class FileLocalSearch {

    public static File getFile(File folder, String keyword) {
        List<File> files = searchFiles(folder, keyword);
        return files == null || files.isEmpty() ? null : files.get(0);
    }

    public static List<File> searchFiles(File folder, final String keyword) {
        List<File> result = new ArrayList<>();
        if ((folder.isFile())) {
            result.add(folder);
        }

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return file.getName().equals(keyword);
            }
        });

        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                    break;
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFiles(file, keyword));
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<File> files = searchFiles(new File("D:\\Program Files\\repo"), "antlr-2.7.7.jar");
        System.out.println("共找到:" + files.size() + "个文件");
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
