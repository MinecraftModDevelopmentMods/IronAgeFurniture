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
    @Test public void selectedNativeWorldReaderIsWrappedExactlyOnce() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        assertNotNull("Forge 37 supplies its native Nashorn engine", engine);
        engine.eval(new String(Files.readAllBytes(Paths.get(
                "src/main/resources/coremods/ironagefurniture_native_level_data.js")), StandardCharsets.UTF_8));
        Map<?, ?> transforms = (Map<?, ?>)((Invocable)engine).invokeFunction("initializeCoreMod");
        check(engine, transforms, "ironagefurniture_native_level_data",
                "net/minecraft/world/level/storage/LevelStorageSource", "readNativeLevelData", 1);
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
