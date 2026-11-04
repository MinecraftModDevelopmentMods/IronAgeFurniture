var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

// Forge 37 has no Forge40 world-aware pre-read hook. Wrap its actual selected
// world's file read, preserving the original exception and return contract.
function initializeCoreMod() {
    return {
        'ironagefurniture_native_level_data': {
            'target': {'type': 'CLASS', 'name': 'net.minecraft.world.level.storage.LevelStorageSource'},
            'transformer': function(classNode) {
                var matched = 0;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (!method.desc.endsWith('Lnet/minecraft/world/level/storage/PrimaryLevelData;')) continue;
                    for (var call = method.instructions.getFirst(); call !== null; call = call.getNext()) {
                        if (call instanceof MethodInsnNode && call.getOpcode() === Opcodes.INVOKESTATIC
                                && call.owner === 'net/minecraft/nbt/NbtIo'
                                && call.desc === '(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;') {
                            call.owner = 'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook';
                            call.name = 'readNativeLevelData';
                            ++matched;
                        }
                    }
                }
                if (matched !== 1) throw new Error('Cannot locate Forge37 selected-world reader: ' + matched);
                return classNode;
            }
        }
    };
}
