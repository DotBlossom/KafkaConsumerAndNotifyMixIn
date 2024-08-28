package com.delta.delta.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class NotificationsDto {

    // type case -> redirect in front? else include URL
    private String eventType;

    private Long senderId;
    private Long receiverId;

    private Long postId;

    private Boolean isRead;

}

// Actions : follow , postLike(To postRedirect, commentRedirectInPost)