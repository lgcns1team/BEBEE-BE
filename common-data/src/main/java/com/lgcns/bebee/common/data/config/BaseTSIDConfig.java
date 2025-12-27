package com.lgcns.bebee.common.data.config;

import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Slf4j
public abstract class BaseTSIDConfig {

    /**
     * TSID Factory 빈 등록
     * 각 서비스에서 상속하여 사용
     *
     * @param nodeId 노드 식별자 (환경 변수에서 주입)
     * @return TSID Factory 인스턴스
     */
    @Bean
    public TSID.Factory tsidFactory(@Value("${tsid.node.id}") int nodeId){
        int nodeBits = 10;
        int maxNodeId = (1 << nodeBits) - 1;

        if (nodeId < 0 || nodeId > maxNodeId){
            throw new IllegalArgumentException(
                String.format("Node ID must be between 0 and %d, but got %d", maxNodeId, nodeId)
            );
        }

        TSID.Factory factory = TSID.Factory.builder()
                .withNode(nodeId)
                .withNodeBits(nodeBits)
                .build();

        return factory;
    }
}
