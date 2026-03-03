package org.example.cloudstorageapi.storage.resolver;

import org.example.cloudstorageapi.storage.PathDetails;

public interface VirtualPathResolver <D extends PathDetails> {

    D resolve(String rawPath, boolean hasRoot);
}
