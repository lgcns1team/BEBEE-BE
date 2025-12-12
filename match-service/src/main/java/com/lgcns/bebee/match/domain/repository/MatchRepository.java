package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Match;

import java.util.Optional;

public interface MatchRepository {
    Optional<Match> findById(long id);
}
