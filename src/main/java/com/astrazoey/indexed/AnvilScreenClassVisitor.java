package com.astrazoey.indexed;

import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.*;

public final class AnvilScreenClassVisitor extends ClassVisitor {
    private static final String ANVIL_SCREEN_HANDLER = "net.minecraft.class_1706";
    private static final String FORGING_SCREEN_HANDLER = "net.minecraft.class_4861";
    private static final String UPDATE_RESULT = "method_24928";

    private final String updateResult;
    private final String updateResultDesc;

    AnvilScreenClassVisitor(ClassVisitor parent, String updateResult, String updateResultDesc) {
        super(Opcodes.ASM9, parent);
        this.updateResult = updateResult;
        this.updateResultDesc = updateResultDesc;
    }

    static ClassTransformer createTransformer(MappingResolver mappings) {


        String anvilScreenHandler = mappings.mapClassName("intermediary", ANVIL_SCREEN_HANDLER);
        String updateResult = mappings.mapMethodName("intermediary", FORGING_SCREEN_HANDLER, UPDATE_RESULT, "()V");

        return (name, transformedName, bytes) -> {
            if (name.equals(anvilScreenHandler)) {
                ClassReader reader = new ClassReader(bytes);
                ClassWriter writer = new ClassWriter(0);

                ClassVisitor visitor = new AnvilScreenClassVisitor(writer, updateResult, "()V");
                reader.accept(visitor, 0);

                return writer.toByteArray();
            }

            return bytes;
        };
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(this.updateResult) && descriptor.equals(this.updateResultDesc)) {
            return new MethodVisitor(this.api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                private int ordinal;

                @Override
                public void visitIincInsn(int var, int increment) {


                    if(increment == 2) {
                        if(this.ordinal++ != 1) {
                            super.visitIincInsn(var, increment);
                        }
                    } else if (increment == 1) {
                        if(this.ordinal++ != 0) {
                            super.visitIincInsn(var, increment);
                        }
                    }

                    /*
                    if (increment == 1) {
                        if (this.ordinal++ == 0 || this.ordinal++ == 4) {
                            // delete the first and third inc 1 instruction
                            System.out.println("ran thingo");
                            return;
                        }
                    }
                    super.visitIincInsn(var, increment);
                    */

                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
