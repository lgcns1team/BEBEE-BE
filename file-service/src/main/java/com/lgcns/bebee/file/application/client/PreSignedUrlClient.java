package com.lgcns.bebee.file.application.client;

import com.lgcns.bebee.file.domain.PresignedUrl;

public interface PreSignedUrlClient {
    PresignedUrl generatePresignedUrl(String directory, String entityId, String originFileName, String contentType);
}
