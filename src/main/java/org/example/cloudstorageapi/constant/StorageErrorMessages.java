package org.example.cloudstorageapi.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class StorageErrorMessages {
    public static final String UNKNOWN_STORAGE = "Unknown error with the storage, try again later";

    public static final String RESOURCE_NOT_EXIST_FORMATTED = "Resource does not exist: %s";
    public static final String RESOURCE_NAME_EXIST_FORMATTED = "A resource with the same name already exists: %s";
    public static final String RESOURCE_EXIST_FORMATTED = "Resource already exists: %s";
    public static final String DIRECTORY_NOT_EXIST_FORMATTED = "Directory does not exist: %s";
    public static final String FILE_NOT_DIRECTORY_FORMATTED = "The file cannot be a directory: %s";

    public static class MoveOrRename{
        public static final String PATH_NOT_DIFFERENT = "The paths must be different";
        public static final String DIRECTORY_TO_FILE = "Cannot move or rename a directory to a file path";
        public static final String DIRECTORY_INTO_ITSELF_OR_SUBDIRECTORY = "Cannot move a directory into itself or its subdirectory";
        public static final String FILE_TO_DIRECTORY = "Cannot move or rename a file to a directory path";
        public static final String DIRECTORY_AND_FILE_CHANGED = "Invalid directory move or rename operation: both path and name are changed";
    }


    public static class Bucket{
        public static final String FAIL_CHECK_EXIST_FORMATTED = "Failed to check bucket existence: %s";
        public static final String FAIL_CREATE_FORMATTED = "Failed to create bucket: %s";
        public static final String FAIL_INITIALIZE = "Failed to initialize bucket";
    }

    public static class Upload{
        public static final String FILES_IN_FILE_FORMATTED = "Cannot upload files to file: %s";
        public static final String DAMAGED_FILE = "The resource being loaded is corrupted";
    }
}
