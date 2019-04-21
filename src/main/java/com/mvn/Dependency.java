package com.mvn;

/**
 * @author panda.
 * @version 1.0.
 * @since 2019-04-20 23:25.
 */
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;

    private static final String SPRING_GROUPID = "org.springframework";
    private static final String SPRING_VERSION_STR = "spring-framework";

    public Dependency() {
    }

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getDependency() {
        return "<dependency>\n" +
                "            <groupId>" + this.groupId + "</groupId>\n" +
                "            <artifactId>" + this.artifactId + "</artifactId>\n" +
                "            <version>" + this.version + "</version>\n" +
                "        </dependency>\n";
    }

    public String getDependencyWithoutVersion() {
        return "<dependency>\n" +
                "            <groupId>" + this.groupId + "</groupId>\n" +
                "            <artifactId>" + this.artifactId + "</artifactId>\n" +
                "        </dependency>\n";
    }

    public String getProperties() {
        String tag = SPRING_GROUPID.equals(this.groupId) ? SPRING_VERSION_STR : this.artifactId ;
        tag = tag+ ".version";
        return "<" + tag + ">" + this.version + "</" + tag + ">";
    }

    public String getDependencyManagement() {
        return "<dependency>\n" +
                "            <groupId>" + this.groupId + "</groupId>\n" +
                "            <artifactId>" + this.artifactId + "</artifactId>\n" +
                "            <version>" + getVersionVariable() + "</version>\n" +
                "        </dependency>\n";
    }

    private String getVersionVariable() {
        String version = SPRING_GROUPID.equals(this.groupId) ? SPRING_VERSION_STR:this.artifactId;
        return   "${" + version + ".version" + "}";
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
