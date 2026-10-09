var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

// PlayerEntity's reader is too late for metadata-only pre-flattening items:
// vanilla has already datafixed them. Preserve variants immediately after the
// compressed file is read, including the single-player data in level.dat.
function preserveRawPlayerData(classNode, methods, expected) {
    var reader = ASMAPI.mapMethod('func_74796_a');
    var matched = 0;
    for (var i = 0; i < classNode.methods.size(); ++i) {
        var method = classNode.methods.get(i);
        if (methods.indexOf(method.name) < 0) continue;
        for (var instruction = method.instructions.getFirst(); instruction !== null; instruction = instruction.getNext()) {
            if (instruction.getOpcode() !== Opcodes.INVOKESTATIC
                    || instruction.owner !== 'net/minecraft/nbt/CompressedStreamTools'
                    || instruction.name !== reader
                    || instruction.desc !== '(Ljava/io/InputStream;)Lnet/minecraft/nbt/CompoundNBT;') continue;
            var hook = new InsnList();
            hook.add(new InsnNode(Opcodes.DUP));
            hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook', 'preparePlayerData',
                    '(Lnet/minecraft/nbt/CompoundNBT;)V', false));
            method.instructions.insert(instruction, hook);
            ++matched;
        }
    }
    if (matched !== expected) throw new Error('Cannot locate raw legacy player data readers: ' + classNode.name);
    return classNode;
}

function initializeCoreMod() {
    return {
        'ironagefurniture_legacy_player_files': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveHandler' },
            'transformer': function(classNode) {
                return preserveRawPlayerData(classNode, [ASMAPI.mapMethod('func_75752_b'), 'getPlayerNBT'], 2);
            }
        },
        'ironagefurniture_legacy_single_player_data': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.storage.SaveFormat' },
            'transformer': function(classNode) { return preserveRawPlayerData(classNode, ['getWorldData'], 1); }
        },
        'ironagefurniture_legacy_player_items': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.entity.player.PlayerEntity' },
            'transformer': function(classNode) {
                var reader = ASMAPI.mapMethod('func_70037_a');
                var matched = 0;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.name !== reader || method.desc !== '(Lnet/minecraft/nbt/CompoundNBT;)V') continue;
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook', 'preparePlayerData',
                            '(Lnet/minecraft/nbt/CompoundNBT;)V', false));
                    method.instructions.insert(prefix);
                    ++matched;
                }
                if (matched !== 1) throw new Error('Cannot locate the legacy player inventory reader');
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
