package org.example.cloudstorageapi.storage.minio.resolver;

import lombok.extern.slf4j.Slf4j;
import org.example.cloudstorageapi.exception.PathIllegalArgumentException;
import org.example.cloudstorageapi.storage.PathDetails;
import org.example.cloudstorageapi.storage.minio.MinIOPathDetails;
import org.example.cloudstorageapi.storage.resolver.VirtualPathResolver;
import org.springframework.stereotype.Component;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.example.cloudstorageapi.constant.PathErrorMessages.*;

@Slf4j
@Component
public class MinIOVirtualPathResolver implements VirtualPathResolver<PathDetails> {

    private static final Pattern DOT_ONLY = Pattern.compile("^\\.+$");
    private static final int MAX_SIZE_SEGMENT = 200;
    private static final Set<Character> INVALID_CHARS = Set.of(
            '/', '\\', ':', '*', '?', '"', '<', '>', '|'
    );

    @Override
    public MinIOPathDetails resolve(String rawPath, boolean hasRootFolder) {
        log.debug("Start resolving path: {}, has root folder: {}", rawPath, hasRootFolder);

        if (rawPath == null || rawPath.isBlank()) {
            throw new PathIllegalArgumentException(EMPTY_OR_NULL);
        }

        boolean isFolder = rawPath.endsWith("/");
        log.debug("Path is folder: {}", isFolder);

        List<String> segments = splitAndValidate(normalize(rawPath));

        if (isFolder && DOT_ONLY.matcher(segments.getLast()).matches()) {
            throw new PathIllegalArgumentException(DOT_ONLY_NAME_FORMATTED.formatted(segments.getLast()));
        }

        if (hasRootFolder) {
            String root = segments.getFirst() + "/";
            String path = segments.size() == 1
                    ? ""
                    : buildPath(segments.subList(1, segments.size()), isFolder);

            return new MinIOPathDetails(root, path);
        }

        return new MinIOPathDetails(buildPath(segments, isFolder));
    }

    private String normalize(String rawPath) {
        try {
            log.debug("Start normalizing path: {}", rawPath);
            String normalize = Path.of(rawPath.replaceFirst("^/+", ""))
                    .normalize()
                    .toString()
                    .replace("\\", "/");
            log.debug("Normalizing successfully: {}", normalize);
            return normalize;
        } catch (InvalidPathException ex) {
            throw new PathIllegalArgumentException(INVALID_FORMAT);
        }
    }

    private List<String> splitAndValidate(String path) {
        log.debug("Start splitting path: {}", path);
        List<String> segments = Arrays.stream(path.split("/"))
                .filter(s -> !s.isBlank())
                .toList();
        log.debug("Splitting completed: {}", segments);
        for (String segment : segments) {
            log.debug("Start validating segment: {}", segment);
            validateSegment(segment);
            log.debug("Segment validated: {}", segment);
        }

        return segments;
    }

    private void validateSegment(String segment) {
        if(segment.length() > MAX_SIZE_SEGMENT){
            throw new PathIllegalArgumentException(SEGMENT_LENGTH_EXCEEDED_FORMATTED.formatted(MAX_SIZE_SEGMENT, segment));
        }

        for (char c : segment.toCharArray()) {
            if (INVALID_CHARS.contains(c)) {
                throw new PathIllegalArgumentException(INVALID_CHARACTER_IN_SEGMENT_FORMATTED.formatted(c, segment));
            }
        }
    }

    private String buildPath(List<String> segments, boolean isFolder) {
        log.debug("Start building path: {}", segments);
        if (segments.isEmpty()) {
            return "";
        }
        String path = String.join("/", segments);
        path = isFolder ? path + "/" : path;
        log.debug("Path building successfully: {}", path);
        return path;
    }
}