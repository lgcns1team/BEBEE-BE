package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchReader {
    private final MatchRepository matchRepository;
    
    @Transactional(readOnly = true)
    public Match getById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> MatchErrors.MATCH_NOT_FOUND.toException());
    }
}