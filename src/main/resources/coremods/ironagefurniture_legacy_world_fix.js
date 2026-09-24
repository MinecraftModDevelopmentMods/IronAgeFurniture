var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'ironagefurniture_legacy_level_data': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.storage.SaveFormat'
            },
            'transformer': function(classNode) {
                for (var methodIndex = 0; methodIndex < classNode.methods.size(); ++methodIndex) {
                    var method = classNode.methods.get(methodIndex);
                    if (method.desc.indexOf('Lnet/minecraft/world/storage/SaveFormat$LevelSave;Ljava/io/File;') < 0
                            || !method.desc.endsWith('Lnet/minecraft/world/storage/ServerWorldInfo;')) {
                        continue;
                    }

                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (!(instruction instanceof MethodInsnNode)
                                || instruction.getOpcode() !== Opcodes.INVOKESTATIC
                                || instruction.owner !== 'net/minecraft/nbt/CompressedStreamTools'
                                || instruction.desc !== '(Ljava/io/File;)Lnet/minecraft/nbt/CompoundNBT;') {
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
                                '(Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lnet/minecraft/nbt/CompoundNBT;)V',
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
                'name': 'net.minecraft.world.chunk.storage.ChunkLoader'
            },
            'transformer': function(classNode) {
                for (var methodIndex = 0; methodIndex < classNode.methods.size(); ++methodIndex) {
                    var method = classNode.methods.get(methodIndex);
                    if (method.desc.indexOf('Ljava/util/function/Supplier;Lnet/minecraft/nbt/CompoundNBT;)') < 0
                            || !method.desc.endsWith('Lnet/minecraft/nbt/CompoundNBT;')) {
                        continue;
                    }

                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    prefix.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                            'prepareLegacyChunk',
                            '(Lnet/minecraft/nbt/CompoundNBT;)V',
                            false));
                    method.instructions.insert(prefix);

                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (instruction.getOpcode() === Opcodes.ARETURN) {
                            method.instructions.insertBefore(instruction, new MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                                    'finalizeLegacyChunk',
                                    '(Lnet/minecraft/nbt/CompoundNBT;)Lnet/minecraft/nbt/CompoundNBT;',
                                    false));
                        }
                    }
                }
                return classNode;
            }
        }
    };
}
