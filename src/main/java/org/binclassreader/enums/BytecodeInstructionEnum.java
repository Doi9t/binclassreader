/*
 *    Copyright 2014 - 2016 Yannick Watier
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.binclassreader.enums;

import org.binclassreader.utils.BaseUtils;

/**
 * Created by Yannick on 5/23/2016.
 */

public enum BytecodeInstructionEnum {
    UNKNOWN((short) 0x00, false),
    NOP((short) 0x00, false),
    ACONST_NULL((short) 0x01, false),
    ICONST_M1((short) 0x02, false),
    ICONST_0((short) 0x03, false),
    ICONST_1((short) 0x04, false),
    ICONST_2((short) 0x05, false),
    ICONST_3((short) 0x06, false),
    ICONST_4((short) 0x07, false),
    ICONST_5((short) 0x08, false),
    LCONST_0((short) 0x09, false),
    LCONST_1((short) 0x0A, false),
    FCONST_0((short) 0x0B, false),
    FCONST_1((short) 0x0C, false),
    FCONST_2((short) 0x0D, false),
    DCONST_0((short) 0x0E, false),
    DCONST_1((short) 0x0F, false),
    BIPUSH((short) 0x10, false, BytecodeExtraByteEnum.INDEX),
    SIPUSH((short) 0x11, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    LDC((short) 0x12, false, BytecodeExtraByteEnum.INDEX),
    LDC_W((short) 0x13, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    LDC2_W((short) 0x14, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    ILOAD((short) 0x15, false, BytecodeExtraByteEnum.INDEX),
    LLOAD((short) 0x16, false, BytecodeExtraByteEnum.INDEX),
    FLOAD((short) 0x17, false, BytecodeExtraByteEnum.INDEX),
    DLOAD((short) 0x18, false, BytecodeExtraByteEnum.INDEX),
    ALOAD((short) 0x19, false, BytecodeExtraByteEnum.INDEX),
    ILOAD_0((short) 0x1A, false),
    ILOAD_1((short) 0x1B, false),
    ILOAD_2((short) 0x1C, false),
    ILOAD_3((short) 0x1D, false),
    LLOAD_0((short) 0x1E, false),
    LLOAD_1((short) 0x1F, false),
    LLOAD_2((short) 0x20, false),
    LLOAD_3((short) 0x21, false),
    FLOAD_0((short) 0x22, false),
    FLOAD_1((short) 0x23, false),
    FLOAD_2((short) 0x24, false),
    FLOAD_3((short) 0x25, false),
    DLOAD_0((short) 0x26, false),
    DLOAD_1((short) 0x27, false),
    DLOAD_2((short) 0x28, false),
    DLOAD_3((short) 0x29, false),
    ALOAD_0((short) 0x2A, false),
    ALOAD_1((short) 0x2B, false),
    ALOAD_2((short) 0x2C, false),
    ALOAD_3((short) 0x2D, false),
    IALOAD((short) 0x2E, false),
    LALOAD((short) 0x2F, false),
    FALOAD((short) 0x30, false),
    DALOAD((short) 0x31, false),
    AALOAD((short) 0x32, false),
    BALOAD((short) 0x33, false),
    CALOAD((short) 0x34, false),
    SALOAD((short) 0x35, false),
    ISTORE((short) 0x36, false, BytecodeExtraByteEnum.INDEX),
    LSTORE((short) 0x37, false, BytecodeExtraByteEnum.INDEX),
    FSTORE((short) 0x38, false, BytecodeExtraByteEnum.INDEX),
    DSTORE((short) 0x39, false, BytecodeExtraByteEnum.INDEX),
    ASTORE((short) 0x3A, false, BytecodeExtraByteEnum.INDEX),
    ISTORE_0((short) 0x3B, false),
    ISTORE_1((short) 0x3C, false),
    ISTORE_2((short) 0x3D, false),
    ISTORE_3((short) 0x3E, false),
    LSTORE_0((short) 0x3F, false),
    LSTORE_1((short) 0x40, false),
    LSTORE_2((short) 0x41, false),
    LSTORE_3((short) 0x42, false),
    FSTORE_0((short) 0x43, false),
    FSTORE_1((short) 0x44, false),
    FSTORE_2((short) 0x45, false),
    FSTORE_3((short) 0x46, false),
    DSTORE_0((short) 0x47, false),
    DSTORE_1((short) 0x48, false),
    DSTORE_2((short) 0x49, false),
    DSTORE_3((short) 0x4A, false),
    ASTORE_0((short) 0x4B, false),
    ASTORE_1((short) 0x4C, false),
    ASTORE_2((short) 0x4D, false),
    ASTORE_3((short) 0x4E, false),
    IASTORE((short) 0x4F, false),
    LASTORE((short) 0x50, false),
    FASTORE((short) 0x51, false),
    DASTORE((short) 0x52, false),
    AASTORE((short) 0x53, false),
    BASTORE((short) 0x54, false),
    CASTORE((short) 0x55, false),
    SASTORE((short) 0x56, false),
    POP((short) 0x57, false),
    POP2((short) 0x58, false),
    DUP((short) 0x59, false),
    DUP_X1((short) 0x5A, false),
    DUP_X2((short) 0x5B, false),
    DUP2((short) 0x5C, false),
    DUP2_X1((short) 0x5D, false),
    DUP2_X2((short) 0x5E, false),
    SWAP((short) 0x5F, false),
    IADD((short) 0x60, false),
    LADD((short) 0x61, false),
    FADD((short) 0x62, false),
    DADD((short) 0x63, false),
    ISUB((short) 0x64, false),
    LSUB((short) 0x65, false),
    FSUB((short) 0x66, false),
    DSUB((short) 0x67, false),
    IMUL((short) 0x68, false),
    LMUL((short) 0x69, false),
    FMUL((short) 0x6A, false),
    DMUL((short) 0x6B, false),
    IDIV((short) 0x6C, false),
    LDIV((short) 0x6D, false),
    FDIV((short) 0x6E, false),
    DDIV((short) 0x6F, false),
    IREM((short) 0x70, false),
    LREM((short) 0x71, false),
    FREM((short) 0x72, false),
    DREM((short) 0x73, false),
    INEG((short) 0x74, false),
    LNEG((short) 0x75, false),
    FNEG((short) 0x76, false),
    DNEG((short) 0x77, false),
    ISHL((short) 0x78, false),
    LSHL((short) 0x79, false),
    ISHR((short) 0x7A, false),
    LSHR((short) 0x7B, false),
    IUSHR((short) 0x7C, false),
    LUSHR((short) 0x7D, false),
    IAND((short) 0x7E, false),
    LAND((short) 0x7F, false),
    IOR((short) 0x80, false),
    LOR((short) 0x81, false),
    IXOR((short) 0x82, false),
    LXOR((short) 0x83, false),
    IINC((short) 0x84, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    I2L((short) 0x85, false),
    I2F((short) 0x86, false),
    I2D((short) 0x87, false),
    L2I((short) 0x88, false),
    L2F((short) 0x89, false),
    L2D((short) 0x8A, false),
    F2I((short) 0x8B, false),
    F2L((short) 0x8C, false),
    F2D((short) 0x8D, false),
    D2I((short) 0x8E, false),
    D2L((short) 0x8F, false),
    D2F((short) 0x90, false),
    I2B((short) 0x91, false),
    I2C((short) 0x92, false),
    I2S((short) 0x93, false),
    LCMP((short) 0x94, false),
    FCMPL((short) 0x95, false),
    FCMPG((short) 0x96, false),
    DCMPL((short) 0x97, false),
    DCMPG((short) 0x98, false),
    IFEQ((short) 0x99, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IFNE((short) 0x9A, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IFLT((short) 0x9B, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IFGE((short) 0x9C, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IFGT((short) 0x9D, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IFLE((short) 0x9E, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ICMPEQ((short) 0x9F, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ICMPNE((short) 0xA0, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ICMPLT((short) 0xA1, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ICMPGE((short) 0xA2, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ICMPGT((short) 0xA3, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ICMPLE((short) 0xA4, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ACMPEQ((short) 0xA5, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IF_ACMPNE((short) 0xA6, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    GOTO((short) 0xA7, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    JSR((short) 0xA8, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    RET((short) 0xA9, false, BytecodeExtraByteEnum.INDEX),
    TABLESWITCH((short) 0xAA, true), //TODO
    LOOKUPSWITCH((short) 0xAB, true), //TODO
    IRETURN((short) 0xAC, false),
    LRETURN((short) 0xAD, false),
    FRETURN((short) 0xAE, false),
    DRETURN((short) 0xAF, false),
    ARETURN((short) 0xB0, false),
    RETURN((short) 0xB1, false),
    GETSTATIC((short) 0xB2, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    PUTSTATIC((short) 0xB3, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    GETFIELD((short) 0xB4, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    PUTFIELD((short) 0xB5, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    INVOKEVIRTUAL((short) 0xB6, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    INVOKESPECIAL((short) 0xB7, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    INVOKESTATIC((short) 0xB8, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    INVOKEINTERFACE((short) 0xB9, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2, BytecodeExtraByteEnum.COUNT, BytecodeExtraByteEnum.ZERO),
    INVOKEDYNAMIC((short) 0xBA, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2, BytecodeExtraByteEnum.ZERO, BytecodeExtraByteEnum.ZERO),
    NEW((short) 0xBB, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    NEWARRAY((short) 0xBC, false, BytecodeExtraByteEnum.INDEX),
    ANEWARRAY((short) 0xBD, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    ARRAYLENGTH((short) 0xBE, false),
    ATHROW((short) 0xBF, false),
    CHECKCAST((short) 0xC0, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    INSTANCEOF((short) 0xC1, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    MONITORENTER((short) 0xC2, false),
    MONITOREXIT((short) 0xC3, false),
    WIDE((short) 0xC4, true), //TODO
    MULTIANEWARRAY((short) 0xC5, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2, BytecodeExtraByteEnum.DIMENSIONS),
    IFNULL((short) 0xC6, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    IFNONNULL((short) 0xC7, false, BytecodeExtraByteEnum.INDEXBYTE_1, BytecodeExtraByteEnum.INDEXBYTE_2),
    GOTO_W((short) 0xC8, false, BytecodeExtraByteEnum.BRANCHBYTE_1, BytecodeExtraByteEnum.BRANCHBYTE_2, BytecodeExtraByteEnum.BRANCHBYTE_3, BytecodeExtraByteEnum.BRANCHBYTE_4),
    JSR_W((short) 0xC9, false, BytecodeExtraByteEnum.BRANCHBYTE_1, BytecodeExtraByteEnum.BRANCHBYTE_2, BytecodeExtraByteEnum.BRANCHBYTE_3, BytecodeExtraByteEnum.BRANCHBYTE_4),
    BREAKPOINT((short) 0xCA, false),
    IMPDEP1((short) 0xFE, false),
    IMPDEP2((short) 0xFF, false);

    private short value;
    private BytecodeExtraByteEnum[] nbByteToRead;
    private boolean dynamicBytes, haveExtraBytes;

    BytecodeInstructionEnum(short value, boolean dynamicBytes, BytecodeExtraByteEnum... nbByteToRead) {
        this.value = value;
        this.dynamicBytes = dynamicBytes;
        this.nbByteToRead = nbByteToRead;
        haveExtraBytes = (nbByteToRead != null && nbByteToRead.length > 1);
    }

    public static BytecodeInstructionEnum getBytecodeByValue(short byteValue) {
        BytecodeInstructionEnum value = BytecodeInstructionEnum.UNKNOWN;

        for (BytecodeInstructionEnum bytecodeInstructionEnum : values()) {
            if (bytecodeInstructionEnum.getValue() == byteValue) {
                value = bytecodeInstructionEnum;
                break;
            }
        }
        return value;
    }

    public short getValue() {
        return value;
    }

    public BytecodeExtraByteEnum[] getNbByteToRead() {
        return BaseUtils.safeArrayClone(nbByteToRead);
    }

    public boolean isDynamicBytes() {
        return dynamicBytes;
    }

    public boolean haveExtraBytes() {
        return haveExtraBytes;
    }
}
