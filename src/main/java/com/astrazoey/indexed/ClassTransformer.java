package com.astrazoey.indexed;

public interface ClassTransformer {
    byte[] transform(String name, String transformedName, byte[] bytes);
}