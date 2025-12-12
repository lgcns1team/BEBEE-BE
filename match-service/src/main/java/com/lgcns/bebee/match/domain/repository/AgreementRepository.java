package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Agreement;

public interface AgreementRepository {
    Agreement save(Agreement agreement);
    Agreement findById(long id);
}
