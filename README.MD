
## 功能说明
将本地jar转为pom的依赖。

## 流程说明
 1、从依赖包文件夹中获取到所有依赖jar的名称;<br>
 2、循环1名称列表在maven本地仓库中查找jar对应的路径;<br>
 3、从2得到的jar在同目录下获取对应的pom文件;<br>
 4、从pom中读取对应的属性(groupId,artifactId,version)存入列表;<br>
 5、从传入列表组装properties与dependencies的元素;<br>
 6、将5的结果集写入txt文件.

## 使用说明
启动类： com.mvn.ReadMvnApplication
1、修改启动类中的4个全局变量
```java
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
```
