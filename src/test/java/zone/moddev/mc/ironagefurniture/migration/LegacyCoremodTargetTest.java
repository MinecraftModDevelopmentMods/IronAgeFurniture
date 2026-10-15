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
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/** Run the transformer against this target's actual Minecraft classes. */
public class LegacyCoremodTargetTest {
    @Test public void flatteningTableIsWritableInDevelopmentAndPackagedNames() throws Exception {
        for (boolean packaged : new boolean[]{false, true}) {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval(new String(Files.readAllBytes(Paths.get(
                    "src/main/resources/coremods/ironagefurniture_legacy_world_fix.js")), StandardCharsets.UTF_8));
            Map<?, ?> transforms = (Map<?, ?>)((Invocable)engine).invokeFunction("initializeCoreMod");
            ClassNode node = new ClassNode();
            try (InputStream bytes = getClass().getResourceAsStream("/net/minecraft/util/datafix/fixes/BlockStateData.class")) {
                new ClassReader(bytes).accept(node, 0);
            }
            int matched = 0;
            for (FieldNode field : node.fields) if (field.name.equals("MAP")) {
                if (packaged) field.name = "f_14934_";
                matched++;
            }
            assertEquals(1, matched);
            ((Invocable)engine).invokeMethod(transforms.get("ironagefurniture_expand_legacy_block_states"), "transformer", node);
            for (FieldNode field : node.fields) if (field.name.equals(packaged ? "f_14934_" : "MAP"))
                assertEquals("Legacy table remains final", 0, field.access & Opcodes.ACC_FINAL);
        }
    }
    @Test public void nativeReadersReceiveTheirHooksExactlyOnce() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        assertNotNull("Legacy Minecraft tests must use Nashorn engine", engine);
        engine.eval(new String(Files.readAllBytes(Paths.get(
                "src/main/resources/coremods/ironagefurniture_legacy_world_fix.js")), StandardCharsets.UTF_8));
        Map<?, ?> transforms = (Map<?, ?>)((Invocable)engine).invokeFunction("initializeCoreMod");
        check(engine, transforms, "ironagefurniture_legacy_level_data",
                "net/minecraft/world/level/storage/LevelStorageSource", "captureLegacyWorld", 1);
        check(engine, transforms, "ironagefurniture_legacy_level_data",
                "net/minecraft/world/level/storage/LevelStorageSource", "prepare", 1);
        check(engine, transforms, "ironagefurniture_legacy_player_files",
                "net/minecraft/world/level/storage/PlayerDataStorage", "preparePlayerData", 1);
        check(engine, transforms, "ironagefurniture_legacy_entity_data",
                "net/minecraft/world/level/chunk/storage/EntityStorage", "prepareEntityData", 1);
        check(engine, transforms, "ironagefurniture_legacy_chunk_data",
                "net/minecraft/world/level/chunk/storage/ChunkStorage", "prepareLegacyChunk", 1);
        check(engine, transforms, "ironagefurniture_legacy_chunk_data",
                "net/minecraft/world/level/chunk/storage/ChunkStorage", "prepareVanillaTileEntitySchema", 1);
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
