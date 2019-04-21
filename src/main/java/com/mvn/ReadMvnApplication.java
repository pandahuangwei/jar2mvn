package com.mvn;

import com.mvn.utils.BuildMvnElement;
import com.mvn.utils.FileLocalSearch;
import com.mvn.utils.FileUtil;
import com.mvn.utils.TaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author panda.
 * @version 1.0.
 * @since 2019-04-20 23:11.
 */
public class ReadMvnApplication {
    private static Logger logger = LoggerFactory.getLogger(ReadMvnApplication.class);
    private static String JAR = ".jar";
    private static String POM = ".pom";
    /**
     * 依赖的jar包目录路径/maven的本地仓库路径
     */
    private static String DEPEND_JAR_PATH = "D:\\jartest\\lib";
    private static String MAVEN_REPO = "D:\\Program Files\\repo";
    /**
     * 生成结果txt文件的路径、名称
     */
    private static String TXT_OUT_PATH = "D:\\Program Files";
    private static String TXT_OUT_NAME = "test1";

    public static void main(String[] args) {
        //单线程搜索读取jar对应的pom
        // List<Dependency> dependencies = readSingleThread();
        //多线程搜索读取jar对应的pom
        List<Dependency> dependencies = readMultiThread();
        writeTxt(dependencies);
    }


    /**
     * 1、从传入列表组装properties与dependencies的元素;<br>
     * 2、将5的结果集写入txt文件.
     */
    private static void writeTxt(List<Dependency> list) {
        StringBuilder properties = new StringBuilder(10240);
        StringBuilder management = new StringBuilder(10240);
        StringBuilder dependency = new StringBuilder(10240);
        BuildMvnElement.getPropertiesAndDependencyManagement(list, properties, management, dependency);
        FileUtil.writeTxt(TXT_OUT_PATH, TXT_OUT_NAME, properties.toString(), management.toString(), dependency.toString());
        System.out.println("输出txt文件完成...");
    }


    /**
     * 1、从依赖包文件夹中获取到所有依赖jar的名称;<br>
     * 2、循环1名称列表在maven本地仓库中查找jar对应的路径;<br>
     * 3、从2得到的jar在同目录下获取对应的pom文件;<br>
     * 4、从pom中读取对应的属性(groupId,artifactId,version)存入列表;<br>
     */
    private static List<Dependency> readSingleThread() {
        long startTime = System.currentTimeMillis();
        List<String> allJarName = FileUtil.getAllJarName(DEPEND_JAR_PATH);
        List<Dependency> list = new ArrayList<>(256);
        for (String jarName : allJarName) {
            Dependency dependency = getDependency(jarName);
            if (dependency == null) {
                continue;
            }
            list.add(dependency);
        }
        System.out.println("单线程搜索-耗时(ms):" + (System.currentTimeMillis() - startTime));
        return list;
    }

    private static List<Dependency> readMultiThread() {
        long startTime = System.currentTimeMillis();
        List<String> allJarName = FileUtil.getAllJarName(DEPEND_JAR_PATH);

        TaskManager taskManager = TaskManager.newInstance(10);
        List<Dependency> list = new CopyOnWriteArrayList<>();
        for (String jarName : allJarName) {
            taskManager.executeTaskWhileNoFull(new Runnable() {
                @Override
                public void run() {
                    Dependency dependency = getDependency(jarName);
                    if (dependency != null) {
                        list.add(dependency);
                    }
                }
            });
        }
        taskManager.waitTaskFinish();
        taskManager.shutdown();
        System.out.println("多线程搜索-耗时(ms):" + (System.currentTimeMillis() - startTime));
        return list;
    }

    private static Dependency getDependency(String jarName) {
        Dependency dependency = null;
        File file = FileLocalSearch.getFile(new File(MAVEN_REPO), jarName);
        if (file == null) {
            System.err.println("找不到对应jar包：" + jarName);
        }
        String jarPath = file.getAbsolutePath();
        String pomPath = jarPath.replace(JAR, POM);
        System.out.println("jarName:" + jarName + " \njarPath：" + jarPath + " \npomPath: " + pomPath);

        String pomTxt = null;
        if (!isExists(pomPath)) {
            System.err.println("pomPath不存在: " + pomPath);
            pomPath = rebuildPomPath(pomPath);
        }

        if (!isExists(pomPath)) {
            System.err.println("重新组装后的pomPath仍然不存在: " + pomPath);
            return dependency;
        }

        pomTxt = FileUtil.readToString(pomPath);
        if (pomTxt != null) {
            String groupId = FileUtil.readGroupId(pomTxt);
            String artifactId = FileUtil.readArtifactId(pomTxt);
            String version = FileUtil.readVersion(pomTxt);
            dependency = new Dependency(groupId, artifactId, version);
        }
        return dependency;
    }

    private static boolean isExists(String pomPath) {
        return new File(pomPath).exists();
    }

    /**
     * 处理特殊情况：
     * <p>
     * 第一种情况：获取最后一个"-"的位置lastIndex，截取(0,lastIndex)+.pom组成新的pom文件
     * json-lib-2.4-jdk15.jar 对应的pom是json-lib-2.4.pom
     * </p>
     *
     * @param pomPath path
     * @return string
     */
    private static String rebuildPomPath(String pomPath) {
        int lastIndex = pomPath.lastIndexOf("-");
        return pomPath.substring(0, lastIndex) + POM;
    }
}
