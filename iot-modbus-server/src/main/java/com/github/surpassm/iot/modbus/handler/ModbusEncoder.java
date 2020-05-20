package com.github.surpassm.iot.modbus.handler;

import com.github.surpassm.iot.modbus.pojo.ModbusFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 *
 * @author ares
 */
public class ModbusEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ModbusFrame) {
            ModbusFrame frame = (ModbusFrame) msg;

            ctx.writeAndFlush(frame.encode());
        } else {
            ctx.write(msg);
        }
    }
}
