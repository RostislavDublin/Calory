package rdublin.portal.auth.oauth2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class PortalAccessTokenClaims {
    public static final String CLAIM_KEY_USER_ID = "user_id";
    public static final String CLAIM_KEY_USER_NAME = "user_name";
    public static final String CLAIM_KEY_ENABLED = "user_enabled";
    public static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "user_account_non_expired";
    public static final String CLAIM_KEY_CREDENTIALS_NON_EXPIRED = "user_credentials_non_expired";
    public static final String CLAIM_KEY_ACCOUNT_NON_LOCKED = "user_credentials_non_locked";

    public static final List<String> FILTERED_CLAIMS_KEYS =
            Collections.unmodifiableList(Arrays.asList(new String[]{
                    CLAIM_KEY_USER_ID,
                    CLAIM_KEY_USER_NAME,
                    CLAIM_KEY_ENABLED,
                    CLAIM_KEY_ACCOUNT_NON_EXPIRED,
                    CLAIM_KEY_CREDENTIALS_NON_EXPIRED,
                    CLAIM_KEY_ACCOUNT_NON_LOCKED}));

}
