package org.example.cloudstorageapi.storage.minio;

import org.example.cloudstorageapi.storage.PathDetails;

public class MinIOPathDetails implements PathDetails {

    private final String rootFolder;
    private final String pathWithOutRoot;

    public MinIOPathDetails(String rootFolder, String pathWithOutRoot){
        this.rootFolder = rootFolder;
        this.pathWithOutRoot = pathWithOutRoot;
    }

    public MinIOPathDetails(String pathWithOutRoot){
        this.rootFolder = "";
        this.pathWithOutRoot = pathWithOutRoot;
    }

    @Override
    public String getFullPath() {
        return rootFolder + pathWithOutRoot;
    }

    @Override
    public String getLastSegmentName() {

        String fullPath = getFullPath();

        if (fullPath == null || fullPath.isEmpty()) {
            return "";
        }

        if (isDirectory()) {
            fullPath = fullPath.substring(0, fullPath.length() - 1);
        }

        int lastSlash = fullPath.lastIndexOf('/');

        String lastSegment;
        if(lastSlash >= 0){
            lastSegment = fullPath.substring(lastSlash + 1);
        }else{
            lastSegment = fullPath;
        }

        return lastSegment;
    }

    @Override
    public String getLastSegment() {
        return isDirectory() ? getLastSegmentName() + "/" : getLastSegmentName();
    }

    @Override
    public boolean hasRootFolder() {
        return !rootFolder.isBlank();
    }

    @Override
    public boolean isDirectory() {
        String fullPath = getFullPath();
        if(fullPath.isBlank()){
            return true;
        }
        return fullPath.endsWith("/");
    }

    @Override
    public String getPrefix() {
        String pathWithOutRoot = getPathWithOutRootFolder();

        if (pathWithOutRoot == null || pathWithOutRoot.isBlank()) {
            return "";
        }

        if (isDirectory()) {
            pathWithOutRoot = pathWithOutRoot.substring(0, pathWithOutRoot.length() - 1);
        }

        int lastSlash = pathWithOutRoot.lastIndexOf('/');

        if (lastSlash < 0) {
            return "";
        }

        return pathWithOutRoot.substring(0, lastSlash + 1);
    }


    @Override
    public String getRootFolder() {
        return rootFolder;
    }

    @Override
    public String getPathWithOutRootFolder() {
        return pathWithOutRoot;
    }
}