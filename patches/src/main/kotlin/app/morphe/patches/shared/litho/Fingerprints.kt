package app.morphe.patches.shared.litho

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.literal
import app.morphe.util.*
import app.morphe.util.fingerprint.legacyFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.StringReference

internal val componentContextParserFingerprint = Fingerprint(
    strings = listOf("Number of bits must be positive")
)

internal val componentContextSubParserFingerprint2 = legacyFingerprint(
    name = "componentContextSubParserFingerprint2",
    returnType = "L",
    strings = listOf("Number of bits must be positive"),
)

internal val lithoFilterFingerprint = Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    custom = { _, classDef ->
        classDef.endsWith("/LithoFilterPatch;")
    }
)

internal val emptyComponentFingerprint = Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.CONSTRUCTOR),
    parameters = listOf(),
    strings = listOf("EmptyComponent"),
    custom = { _, classDef ->
        classDef.methods.filter { AccessFlags.STATIC.isSet(it.accessFlags) }.size == 1
    }
)

internal val lithoComponentNameUpbFeatureFlagFingerprint = Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = emptyList(),
    filters = listOf(literal(45631264L))
)

internal val lithoConverterBufferUpbFeatureFlagFingerprint = Fingerprint(
    returnType = "L",
    filters = listOf(literal(45419603L))
)

internal val conversionContextFingerprintToString = Fingerprint(
    parameters = listOf(),
    strings = listOf(
        "ConversionContext{containerInternal=",
        ", widthConstraint=",
        ", heightConstraint=",
        ", templateLoggerFactory=",
        ", rootDisposableContainer=",
        ", identifierProperty="
    )
)

internal val protobufBufferReferenceLegacyFingerprint = Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("I", "Ljava/nio/ByteBuffer;"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IPUT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.SUB_INT_2ADDR,
    )
)

internal const val BUFFER_UPD_FEATURE_FLAG = 45419603L

internal val bufferUpbFeatureFlagFingerprint = legacyFingerprint(
    name = "bufferUpbFeatureFlagFingerprint",
    returnType = "L",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.STATIC,
    parameters = listOf("L"),
    literals = listOf(BUFFER_UPD_FEATURE_FLAG),
)

internal val byteBufferFingerprint = legacyFingerprint(
    name = "byteBufferFingerprint",
    returnType = "V",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = listOf("I", "Ljava/nio/ByteBuffer;"),
    opcodes = listOf(
        null,
        Opcode.IF_EQZ,
        Opcode.IPUT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.SUB_INT_2ADDR,
        Opcode.IPUT,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IPUT,
        Opcode.RETURN_VOID,
        Opcode.CONST_4,
        Opcode.IPUT,
        Opcode.IPUT,
        Opcode.GOTO
    ),
    // Check method count and field count to support both YouTube and YouTube Music
    customFingerprint = { _, classDef ->
        classDef.methods.count() > 6
                && classDef.fields.count() > 4
    },
)

internal val emptyComponentFingerprint2 = legacyFingerprint(
    name = "emptyComponentFingerprint2",
    accessFlags = AccessFlags.PRIVATE or AccessFlags.CONSTRUCTOR,
    parameters = emptyList(),
    strings = listOf("EmptyComponent"),
    customFingerprint = { _, classDef ->
        classDef.methods.filter { AccessFlags.STATIC.isSet(it.accessFlags) }.size == 1
    }
)

internal val componentContextParserFingerprint2 = legacyFingerprint(
    name = "componentContextParserFingerprint2",
    strings = listOf("Number of bits must be positive"),
)

internal val componentContextParserLegacyFingerprint = legacyFingerprint(
    name = "componentContextParserLegacyFingerprint",
    returnType = "L",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    opcodes = listOf(
        Opcode.INVOKE_INTERFACE,
        Opcode.INVOKE_STATIC_RANGE,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IGET_OBJECT,
    ),
    customFingerprint = { method, _ ->
        method.indexOfFirstInstruction {
            opcode == Opcode.CONST_STRING &&
                    getReference<StringReference>()
                        ?.string.toString()
                        .startsWith("Error while converting")
        } >= 0
    }
)

internal val componentCreateFingerprint = legacyFingerprint(
    name = "componentCreateFingerprint",
    strings = listOf(
        "Element missing correct type extension",
        "Element missing type"
    )
)

internal val lithoThreadExecutorFingerprint = legacyFingerprint(
    name = "lithoThreadExecutorFingerprint",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.CONSTRUCTOR,
    parameters = listOf("I", "I", "I"),
    customFingerprint = { method, classDef ->
        classDef.superclass == "Ljava/util/concurrent/ThreadPoolExecutor;" &&
                method.containsLiteralInstruction(1L) // 1L = default thread timeout.
    }
)

internal const val PATH_UPD_FEATURE_FLAG = 45631264L

internal val pathUpbFeatureFlagFingerprint = legacyFingerprint(
    name = "pathUpbFeatureFlagFingerprint",
    returnType = "Z",
    accessFlags = AccessFlags.PUBLIC or AccessFlags.FINAL,
    parameters = emptyList(),
    literals = listOf(PATH_UPD_FEATURE_FLAG),
)
