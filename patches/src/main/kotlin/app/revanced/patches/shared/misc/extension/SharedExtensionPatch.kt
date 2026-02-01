package app.morphe.patches.shared.misc.extension

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.FingerprintBuilder
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.fingerprint
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.Method

internal const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/shared/utils/Utils;"

/**
 * A patch to extend with an extension shared with multiple patches.
 *
 * @param extensionName The name of the extension to extend with.
 */
@Suppress("unused")
fun sharedExtensionPatch(
    extensionName: String,
    vararg hooks: ExtensionHook,
) = bytecodePatch {
    dependsOn(sharedExtensionPatch(*hooks))

    extendWith("extensions/shared.mpe")
}

/**
 * A patch to extend with the "shared" extension.
 *
 * @param hooks The hooks to get the application context for use in the extension,
 * commonly for the onCreate method of exported activities.
 */
fun sharedExtensionPatch(
    vararg hooks: ExtensionHook,
) = bytecodePatch {
    extendWith("extensions/shared.mpe")

    execute {
        if (classDefByOrNull { EXTENSION_CLASS_DESCRIPTOR == it.type } == null) {
            throw PatchException("Shared extension is not available. This patch can not succeed without it.")
        }
    }

    finalize {
        // The hooks are made in finalize to ensure that the context is hooked before any other patches.
        hooks.forEach { hook -> hook(EXTENSION_CLASS_DESCRIPTOR) }
    }
}

@Suppress("CONTEXT_RECEIVERS_DEPRECATED")
class ExtensionHook internal constructor(
    internal val fingerprint: Fingerprint,
    private val insertIndexResolver: BytecodePatchContext.(Method) -> Int,
    private val contextRegisterResolver: BytecodePatchContext.(Method) -> String,
) {
    context(BytecodePatchContext)
    operator fun invoke(extensionClassDescriptor: String) {
        val insertIndex = insertIndexResolver(fingerprint.method)
        val contextRegister = contextRegisterResolver(fingerprint.method)

        fingerprint.method.addInstruction(
            insertIndex,
            "invoke-static/range { $contextRegister .. $contextRegister }, " +
                    "$extensionClassDescriptor->setContext(Landroid/content/Context;)V",
        )
    }
}

fun extensionHook(
    insertIndexResolver: BytecodePatchContext.(Method) -> Int = { 0 },
    contextRegisterResolver: BytecodePatchContext.(Method) -> String = { "p0" },
    fingerprint: Fingerprint,
) = ExtensionHook(fingerprint, insertIndexResolver, contextRegisterResolver)

@Suppress("unused")
fun extensionHook(
    insertIndexResolver: BytecodePatchContext.(Method) -> Int = { 0 },
    contextRegisterResolver: BytecodePatchContext.(Method) -> String = { "p0" },
    fingerprintBuilderBlock: FingerprintBuilder.() -> Unit,
) = extensionHook(insertIndexResolver, contextRegisterResolver, fingerprint(block = fingerprintBuilderBlock))
