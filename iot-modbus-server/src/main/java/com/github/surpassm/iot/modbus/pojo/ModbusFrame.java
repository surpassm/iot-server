package com.github.surpassm.iot.modbus.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ModbusFrame {

    private final ModbusHeader header;
    private final ModbusFunction function;

    public ModbusFrame(ModbusHeader header, ModbusFunction function) {
        this.header = header;
        this.function = function;
    }

    public ModbusHeader getHeader() {
        return header;
    }

    public ModbusFunction getFunction() {
        return function;
    }

    /**
     * 核心解码逻辑
     *
     * @return ByteBuf 传递给下一个channel
     */
    public ByteBuf encode() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(header.encode());
        buf.writeBytes(function.encode());
        return buf;
    }

    @Override
    public String toString() {
        return "ModbusFrame{" + "header=" + header + ", function=" + function + '}';
    }
}
