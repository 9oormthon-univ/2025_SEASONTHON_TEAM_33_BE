package com.goormthon.samsamejo.security.info;

import com.goormthon.samsamejo.domain.type.ERole;

public record UserClaims(
        Long id,
        ERole role
) {
}
