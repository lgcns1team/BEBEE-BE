package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_chatroom_members",
            columnNames = {"disabled_id", "helper_id"}
        )
    }
)
public class Chatroom extends BaseTimeEntity {
    @Id @Tsid
    @Column(name = "chatroom_id")
    private Long id;

    @Column(name = "disabled_id", nullable = false)
    private Long disabledId;

    @Column(name = "helper_id", nullable = false)
    private Long helperId;

    @Column(length = 50)
    private String title;
}
