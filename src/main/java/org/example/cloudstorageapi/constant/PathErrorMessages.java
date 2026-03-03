package org.example.cloudstorageapi.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class PathErrorMessages {
    public static final String EMPTY_OR_NULL = "Path must not be null or blank";
    public static final String EMPTY = "Path cannot be empty";
    public static final String DOT_ONLY_NAME_FORMATTED = "Folder name cannot consist only of dots: %s";
    public static final String INVALID_FORMAT = "Invalid path format";
    public static final String INVALID_CHARACTER_IN_SEGMENT_FORMATTED =  "Invalid character '%s' in path segment: %s";
    public static final String SEGMENT_LENGTH_EXCEEDED_FORMATTED = "Segment length increased, maximum length %s: %s";
}

