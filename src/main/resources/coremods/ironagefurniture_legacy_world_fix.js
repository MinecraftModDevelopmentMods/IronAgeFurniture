var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var Type = Java.type('org.objectweb.asm.Type');

function prepareRecipeSettings(classNode) {
    var matched = 0;
    for (var i = 0; i < classNode.methods.size(); ++i) {
        var method = classNode.methods.get(i);
        if (!method.desc.endsWith('Lnet/minecraft/world/level/DataPackConfig;')) continue;
        var readsFile = false;
        for (var instruction = method.instructions.getFirst(); instruction !== null; instruction = instruction.getNext())
            if (instruction instanceof MethodInsnNode && instruction.getOpcode() === Opcodes.INVOKESTATIC
                    && instruction.owner === 'net/minecraft/nbt/NbtIo'
                    && instruction.desc === '(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;') readsFile = true;
        if (!readsFile) continue;
        var types = Type.getArgumentTypes(method.desc);
        var slot = (method.access & Opcodes.ACC_STATIC) !== 0 ? 0 : 1;
        var fileSlot = -1;
        for (var j = 0; j < types.length; ++j) {
            if (types[j].getDescriptor() === 'Ljava/io/File;') fileSlot = slot;
            slot += types[j].getSize();
        }
        if (fileSlot < 0) continue;
        var prefix = new InsnList();
        prefix.add(new VarInsnNode(Opcodes.ALOAD, fileSlot));
        prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                'zone/moddev/mc/ironagefurniture/api/recipes/SconceRecipeSettings', 'prepare',
                '(Ljava/io/File;)V', false));
        method.instructions.insert(prefix);
        ++matched;
    }
    if (matched !== 1) throw new Error('Cannot locate Forge 37 datapack settings reader: ' + matched);
}

// Keep item subtypes before vanilla updates saved player inventories. A late
// inventory callback cannot recover colours or metals already discarded by DFU.
function preservePlayerItems(classNode, rootFile) {
    var matched = 0;
    for (var i = 0; i < classNode.methods.size(); ++i) {
        var method = classNode.methods.get(i);
        for (var instruction = method.instructions.getFirst(); instruction !== null; instruction = instruction.getNext()) {
            if (instruction.getOpcode() !== Opcodes.INVOKESTATIC
                    || instruction.owner !== 'net/minecraft/nbt/NbtIo'
                    || instruction.desc !== '(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;') continue;
            var hook = new InsnList();
            hook.add(new InsnNode(Opcodes.DUP));
            hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                    rootFile ? 'prepareLevelPlayerData' : 'preparePlayerData',
                    '(Lnet/minecraft/nbt/CompoundTag;)V', false));
            method.instructions.insert(instruction, hook);
            ++matched;
        }
    }
    if (matched === 0) throw new Error('Cannot locate raw player data readers: ' + classNode.name);
    return classNode;
}

function initializeCoreMod() {
    return {
        'ironagefurniture_legacy_player_files': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.level.storage.PlayerDataStorage' },
            'transformer': function(classNode) { return preservePlayerItems(classNode, false); }
        },
        'ironagefurniture_legacy_level_data': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.level.storage.LevelStorageSource'
            },
            'transformer': function(classNode) {
                preservePlayerItems(classNode, true);
                prepareRecipeSettings(classNode);
                var matched = 0;
                for (var methodIndex = 0; methodIndex < classNode.methods.size(); ++methodIndex) {
                    var method = classNode.methods.get(methodIndex);
                    // Forge 37 reads world data in a lambda which captures the
                    // LevelSave. Find that argument, not a slot from an older release.
                    if (!method.desc.endsWith('Lnet/minecraft/world/level/storage/PrimaryLevelData;')) continue;
                    var arguments = Type.getArgumentTypes(method.desc);
                    var slot = (method.access & Opcodes.ACC_STATIC) !== 0 ? 0 : 1;
                    var saveSlot = -1;
                    for (var argumentIndex = 0; argumentIndex < arguments.length; ++argumentIndex) {
                        if (arguments[argumentIndex].getDescriptor() === 'Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;')
                            saveSlot = slot;
                        slot += arguments[argumentIndex].getSize();
                    }
                    if (saveSlot < 0) continue;

                    for (var instruction = method.instructions.getFirst(); instruction !== null;
                            instruction = instruction.getNext()) {
                        if (!(instruction instanceof MethodInsnNode)
                                || instruction.getOpcode() !== Opcodes.INVOKESTATIC
                                || instruction.owner !== 'net/minecraft/nbt/NbtIo'
                                || instruction.desc !== '(Ljava/io/File;)Lnet/minecraft/nbt/CompoundTag;') {
                            continue;
                        }

                        var capture = new InsnList();
                        capture.add(new InsnNode(Opcodes.DUP));
                        capture.add(new VarInsnNode(Opcodes.ALOAD, saveSlot));
                        capture.add(new InsnNode(Opcodes.SWAP));
                        capture.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                                'captureLegacyWorld',
                                '(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/nbt/CompoundTag;)V',
                                false));
                        method.instructions.insert(instruction, capture);
                        ++matched;
                        break;
                    }
                }
                if (matched !== 1) throw new Error('Cannot locate Forge 37 world registry reader: ' + matched);
                return classNode;
            }
        },
        'ironagefurniture_legacy_entity_data': {
            'target': { 'type': 'CLASS', 'name': 'net.minecraft.world.level.chunk.storage.EntityStorage' },
            'transformer': function(classNode) {
                var matched = 0;
                for (var i = 0; i < classNode.methods.size(); ++i) {
                    var method = classNode.methods.get(i);
                    if (method.desc !== '(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;') continue;
                    var prefix = new InsnList();
                    prefix.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    prefix.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook', 'prepareEntityData',
                            '(Lnet/minecraft/nbt/CompoundTag;)V', false));
                    method.instructions.insert(prefix);
                    ++matched;
                }
                if (matched !== 1) throw new Error('Cannot locate Forge 37 separate entity updater: ' + matched);
                return classNode;
            }
        },
        'ironagefurniture_legacy_chunk_data': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.level.chunk.storage.ChunkStorage'
            },
            'transformer': function(classNode) {
                var matched = 0;
                for (var methodIndex = 0; methodIndex < classNode.methods.size(); ++methodIndex) {
                    var method = classNode.methods.get(methodIndex);
                    if (method.desc.indexOf('Ljava/util/function/Supplier;Lnet/minecraft/nbt/CompoundTag;)') < 0
                            || !method.desc.endsWith('Lnet/minecraft/nbt/CompoundTag;')) {
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
                    ++matched;

                    var schemaHooked = false;
                    for (var call = method.instructions.getFirst(); call !== null; call = call.getNext()) {
                        if (!(call instanceof MethodInsnNode) || call.getOpcode() !== Opcodes.INVOKESTATIC
                                || call.desc.indexOf('Lcom/mojang/datafixers/DataFixer;') < 0
                                || call.desc.indexOf('Lnet/minecraft/nbt/CompoundTag;') < 0) continue;
                        // Run after other mods' method-entry hooks, immediately
                        // before the first (pre-flattening) vanilla schema update.
                        var schema = new InsnList();
                        schema.add(new VarInsnNode(Opcodes.ALOAD, 3));
                        schema.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                                'zone/moddev/mc/ironagefurniture/migration/LegacyWorldDataHook',
                                'prepareVanillaTileEntitySchema', '(Lnet/minecraft/nbt/CompoundTag;)V', false));
                        method.instructions.insertBefore(call, schema);
                        schemaHooked = true;
                        break;
                    }
                    if (!schemaHooked) throw new Error('Cannot locate Forge 37 first chunk schema update');

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
                if (matched !== 1) throw new Error('Cannot locate Forge 37 chunk updater: ' + matched);
                return classNode;
            }
        },
        'ironagefurniture_expand_legacy_block_states': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.util.datafix.fixes.BlockStateData'
            },
            'transformer': function(classNode) {
                for (var fieldIndex = 0; fieldIndex < classNode.fields.size(); ++fieldIndex) {
                    var field = classNode.fields.get(fieldIndex);
                    if (field.desc === '[Lcom/mojang/serialization/Dynamic;'
                            && (field.name === 'MAP' || field.name === 'f_14934_')) {
                        field.access = field.access & ~Opcodes.ACC_FINAL;
                    }
                }
                return classNode;
            }
        }
    };
}
