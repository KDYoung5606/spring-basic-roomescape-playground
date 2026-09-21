package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.member.LoginMember;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity createWaiting(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        if (waitingRequest.getDate() == null || waitingRequest.getTheme() == null || waitingRequest.getTime() == null) {
            throw new BusinessException(ErrorCode.BLANK_WAITING);
        }
        WaitingResponse waiting = waitingService.save(waitingRequest, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity cancelWaiting(@PathVariable Long id, LoginMember loginMember) {
        waitingService.cancel(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
