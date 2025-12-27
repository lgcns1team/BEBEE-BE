package com.lgcns.bebee.file.domain;

public record PresignedUrl(String uploadUrl, String fileUrl, Long expiresIn) {
}
