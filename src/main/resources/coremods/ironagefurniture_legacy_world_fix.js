var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'ironagefurniture_legacy_level_data': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.level.storage.LevelStorageSource'
            },
            'transformer': function(classNode) {
                for (var methodIndex = 0; methodIndex < classNode.methods.size(); ++methodIndex) {
                    var method = classNode.methods.get(methodIndex);
                    if (method.desc.indexOf('Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Ljava/io/File;') < 0
                            || !method.desc.endsWith('Lnet/minecraft/world/level/storage/PrimaryLevelData;')) {
                        continue;
                    }

                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (!(instruction instanceof MethodInsnNode)
                                || instruction.getOpcode() !== Opcodes.INVOKESTATIC
                                || instruction.owner !== 'net/minecraft/nbt/NbtIo'
                                || instruction.desc !== '(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;') {
                            continue;
                        }

                        var storedRoot = instruction.getNext();
                        if (!(storedRoot instanceof VarInsnNode) || storedRoot.getOpcode() !== Opcodes.ASTORE) {
                            continue;
                        }

                        var capture = new InsnList();
                        capture.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        capture.add(new VarInsnNode(Opcodes.ALOAD, storedRoot.var));
                        capture.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                                'captureLegacyWorld',
                                '(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/nbt/CompoundTag;)V',
                                false));
                        method.instructions.insert(storedRoot, capture);
                        break;
                    }
                }
                return classNode;
            }
        },
        'ironagefurniture_legacy_chunk_data': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.level.chunk.storage.ChunkStorage'
            },
            'transformer': function(classNode) {
                for (var methodIndex = 0; methodIndex < classNode.methods.size(); ++methodIndex) {
                    var method = classNode.methods.get(methodIndex);
                    if (method.desc !== '(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Supplier;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;') {
                        continue;
                    }

                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    prefix.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                            'prepareLegacyChunk',
                            '(Lnet/minecraft/nbt/CompoundTag;)V',
                            false));
                    method.instructions.insert(prefix);

                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (instruction.getOpcode() === Opcodes.ARETURN) {
                            method.instructions.insertBefore(instruction, new MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                                    'finalizeLegacyChunk',
                                    '(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;',
                                    false));
                        }
                    }
                }
                return classNode;
            }
        },
        'ironagefurniture_expand_legacy_block_states': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.util.datafix.fixes.BlockStateData'
            },
            'transformer': function(classNode) {
                var mappedName = ASMAPI.mapField('f_14934_');
                for (var fieldIndex = 0; fieldIndex < classNode.fields.size(); ++fieldIndex) {
                    var field = classNode.fields.get(fieldIndex);
                    if (field.desc === '[Lcom/mojang/serialization/Dynamic;'
                            && (field.name === mappedName || field.name === 'MAP' || field.name === 'f_14934_')) {
                        field.access = field.access & ~Opcodes.ACC_FINAL;
                    }
                }
                return classNode;
            }
        }
    };
}
