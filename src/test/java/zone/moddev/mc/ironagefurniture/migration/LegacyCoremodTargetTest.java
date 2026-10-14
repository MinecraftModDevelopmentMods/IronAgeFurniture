package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/** Run the transformer against this target's actual Minecraft classes. */
public class LegacyCoremodTargetTest {
    @Test public void nativeReadersReceiveTheirHooksExactlyOnce() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        assertNotNull("Legacy Minecraft tests must use Java 8", engine);
        engine.eval(new String(Files.readAllBytes(Paths.get(
                "src/main/resources/coremods/ironagefurniture_legacy_world_fix.js")), StandardCharsets.UTF_8));
        Map<?, ?> transforms = (Map<?, ?>)((Invocable)engine).invokeFunction("initializeCoreMod");
        check(engine, transforms, "ironagefurniture_legacy_level_data",
                "net/minecraft/world/storage/SaveFormat", "captureLegacyWorld", 1);
        check(engine, transforms, "ironagefurniture_legacy_level_data",
                "net/minecraft/world/storage/SaveFormat", "prepare", 1);
        check(engine, transforms, "ironagefurniture_legacy_player_files",
                "net/minecraft/world/storage/PlayerData", "preparePlayerData", 1);
        check(engine, transforms, "ironagefurniture_legacy_chunk_data",
                "net/minecraft/world/chunk/storage/ChunkLoader", "prepareLegacyChunk", 1);
        check(engine, transforms, "ironagefurniture_legacy_chunk_data",
                "net/minecraft/world/chunk/storage/ChunkLoader", "prepareVanillaTileEntitySchema", 1);
    }

    private static void check(ScriptEngine engine, Map<?, ?> transforms, String name,
            String target, String hook, int expected) throws Exception {
        ClassNode node = new ClassNode();
        try (InputStream bytes = LegacyCoremodTargetTest.class.getResourceAsStream("/" + target + ".class")) {
            assertNotNull(target, bytes);
            new ClassReader(bytes).accept(node, 0);
        }
        ((Invocable)engine).invokeMethod(transforms.get(name), "transformer", node);
        int count = 0;
        for (MethodNode method : node.methods) for (AbstractInsnNode instruction : method.instructions)
            if (instruction instanceof MethodInsnNode && ((MethodInsnNode)instruction).name.equals(hook)) count++;
        assertEquals(target + ": " + hook, expected, count);
    }
}
