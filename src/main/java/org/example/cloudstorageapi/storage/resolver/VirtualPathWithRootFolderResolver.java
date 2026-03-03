package org.example.cloudstorageapi.storage.resolver;

import org.example.cloudstorageapi.storage.PathDetails;

public interface VirtualPathWithRootFolderResolver<P, D extends PathDetails> {

    D resolve(P parameter, String userPath);
}

