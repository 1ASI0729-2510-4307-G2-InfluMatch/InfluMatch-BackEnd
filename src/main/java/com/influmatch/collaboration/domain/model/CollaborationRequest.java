package com.influmatch.collaboration.domain.model;

import com.influmatch.identityaccess.domain.model.User;
import com.influmatch.shared.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class CollaborationRequest extends BaseEntity {

    /** Usuario que envía la solicitud */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    /** Usuario que la recibe */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status = RequestStatusEnum.PENDING;

    private String message;

    @CreationTimestamp
    private Instant createdAt;
}
