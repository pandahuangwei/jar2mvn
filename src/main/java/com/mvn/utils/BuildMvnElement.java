package com.mvn.utils;

import com.mvn.Dependency;
import javafx.util.Pair;

import java.util.List;

/**
 * @author panda.
 * @version 1.0.
 * @since 2019-04-21 02:16.
 */
public class BuildMvnElement {

    public static void getPropertiesAndDependencyManagement(List<Dependency> list,StringBuilder sbProperties,StringBuilder sbDependencyManagement,StringBuilder sbDependency) {
        if(list ==null || list.isEmpty()) {
            System.out.println("没有任何依赖...");
        }
        list.sort((x,y)->x.getArtifactId().compareTo(y.getArtifactId()));
       /* StringBuilder sbProperties = new StringBuilder(10240);
        StringBuilder sbDependencyManagement = new StringBuilder(10240);*/
        for (Dependency d:list){
            sbProperties.append(d.getProperties()).append("\n");
            sbDependencyManagement.append(d.getDependencyManagement());
            sbDependency.append(d.getDependencyWithoutVersion());
        }
    }
}
