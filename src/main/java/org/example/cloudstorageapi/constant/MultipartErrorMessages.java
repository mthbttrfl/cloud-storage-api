package org.example.cloudstorageapi.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class MultipartErrorMessages {
    public static final String SIZE_EXCEED_FORMATTED = "Maximum upload size exceeded, max size %s";
    public static final String INVALID_REQUEST = "Invalid multipart request";
}
