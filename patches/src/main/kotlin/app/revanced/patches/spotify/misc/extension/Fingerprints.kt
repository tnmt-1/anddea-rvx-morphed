package app.morphe.patches.spotify.misc.extension

import app.morphe.patcher.fingerprint

internal val loadOrbitLibraryFingerprint = fingerprint {
    strings("orbit_library_load", "orbit-jni-spotify")
}
