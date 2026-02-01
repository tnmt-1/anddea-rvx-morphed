package app.morphe.patches.spotify.misc.fix.login

import app.morphe.patcher.fingerprint
import app.morphe.util.literal

internal val katanaProxyLoginMethodHandlerClassFingerprint = fingerprint {
    strings("katana_proxy_auth")
}

internal val katanaProxyLoginMethodTryAuthorizeFingerprint = fingerprint {
    strings("e2e")
    literal { 0 }
}
