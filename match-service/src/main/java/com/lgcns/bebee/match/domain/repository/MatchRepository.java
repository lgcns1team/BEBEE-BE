package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
