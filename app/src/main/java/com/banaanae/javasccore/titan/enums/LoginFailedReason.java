package com.banaanae.javasccore.titan.enums;

public enum LoginFailedReason {
    CUSTOM(1),
    PATCH(7),
    UPDATE(8),
    REDIRECT(9),
    MAINTENANCE(10),
    BANNED(11),
    PLAYED_TOO_MUCH(12),
    ACC_LOCKED(13),
    UPDATING_NOT_READY(16),
    // 24?-26 SCID
    DELETED_REQUEST(27),
    DELETED_INACTIVE(28),
    DELETION_WARNING(29),
    LOCATION_RESTRICTED(30);

    private final int code;

    private LoginFailedReason(int code) { this.code = code; }

    public int getCode() { return code; }

    public static LoginFailedReason fromCode(int code) {
        for (LoginFailedReason r : values()) {
            if (r.code == code) return r;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
