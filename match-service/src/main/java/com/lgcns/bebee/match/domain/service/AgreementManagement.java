package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementHelpCategory;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementManagement {
    public Agreement createAgreement(
            Long matchId,
            EngagementType type,
            Boolean isVolunteer,
            Integer unitHoney,
            Integer totalHoney,
            String region
    ) {
        return Agreement.create(
                matchId,
                type,
                isVolunteer,
                unitHoney,
                totalHoney,
                region
        );
    }
}
