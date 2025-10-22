package homes.comm.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnumTokenStatus {
    AUTHENTICATED,
    EXPIRED,
    INVALID
}
