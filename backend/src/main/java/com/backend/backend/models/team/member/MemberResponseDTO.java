package com.backend.backend.models.team.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MemberResponseDTO {
    private Long id;

    private Long teamId;

    private Long userId;

    public MemberResponseDTO(Member member) {
        this(member.getId(), member.getTeam().getId(), member.getUser().getId());
    }
}
