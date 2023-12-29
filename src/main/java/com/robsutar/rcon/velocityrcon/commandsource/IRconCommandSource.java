package com.robsutar.rcon.velocityrcon.commandsource;

import com.robsutar.rcon.velocityrcon.utils.Utils;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class IRconCommandSource  {

    private final StringBuffer buffer = new StringBuffer();
    private boolean permissionFunction = true;

    private  org.bukkit.Server server;

    public IRconCommandSource( org.bukkit.Server server) {
        this.server = server;
    }

    private void addToBuffer(Component message) {
        String txt = LegacyComponentSerializer.legacySection().serialize(message);
        txt = Utils.stripMcColor(txt);
        if (buffer.length() != 0)
            buffer.append("\n");
        buffer.append(txt);
    }


    public String flush() {
        String result = buffer.toString();
        buffer.setLength(0);
        return result;
    }
}
