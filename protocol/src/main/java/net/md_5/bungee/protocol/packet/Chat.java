package net.md_5.bungee.protocol.packet;

import io.github.waterfallmc.travertine.protocol.MultiVersionPacketV17;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Chat extends MultiVersionPacketV17
{

    private String message;
    private byte position;

    public Chat(String message)
    {
        this( message, (byte) 0 );
    }

    // Travertine start
    @Override
    public void v17Read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        message = readString( buf );
    }
    // Travertine end

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        // Waterfall start
        if (direction == ProtocolConstants.Direction.TO_CLIENT) {
            this.message = readString(buf, Short.MAX_VALUE * 8 + 8);
        } else
        // Waterfall end
        message = readString( buf );
        if ( direction == ProtocolConstants.Direction.TO_CLIENT )
        {
            position = buf.readByte();
        }
    }

    // Travertine start
    @Override
    public void v17Write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        writeString( message, buf );
    }
    // Travertine end

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        // Waterfall start
        if (direction == ProtocolConstants.Direction.TO_CLIENT) {
            writeString(this.message, Short.MAX_VALUE * 8 + 8, buf);
        } else
        // Waterfall end
        writeString( message, buf );
        if ( direction == ProtocolConstants.Direction.TO_CLIENT )
        {
            buf.writeByte( position );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
