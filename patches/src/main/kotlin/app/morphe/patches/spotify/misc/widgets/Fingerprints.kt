package app.morphe.patches.spotify.misc.widgets

import app.morphe.patcher.fingerprint
import com.android.tools.smali.dexlib2.Opcode

internal val canBindAppWidgetPermissionFingerprint = fingerprint {
    strings("android.permission.BIND_APPWIDGET")
    opcodes(Opcode.AND_INT_LIT8)
}
