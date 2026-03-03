package org.example.cloudstorageapi.storage;

public interface PathDetails {

    String getFullPath();

    String getLastSegmentName();

    String getLastSegment();

    boolean hasRootFolder();

    boolean isDirectory();

    String getPrefix();

    String getRootFolder();

    String getPathWithOutRootFolder();
}
