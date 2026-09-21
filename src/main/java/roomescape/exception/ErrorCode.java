package roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    NO_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 토큰이 없습니다."),
    NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "회원이 존재하지 않습니다."),
    BLANK_TIME(HttpStatus.BAD_REQUEST, "시간 값은 필수입니다."),
    BLANK_RESERVATION(HttpStatus.BAD_REQUEST, "날짜, 테마, 시간은 필수입니다."),
    BLANK_WAITING(HttpStatus.BAD_REQUEST, "날짜, 테마, 시간은 필수입니다."),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "이미 예약된 시간입니다."),
    DUPLICATE_WAITING(HttpStatus.BAD_REQUEST, "이미 예약했거나 대기 중인 시간입니다."),
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "시간이 존재하지 않습니다."),
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마가 존재하지 않습니다."),
    WAITING_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 대기가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
