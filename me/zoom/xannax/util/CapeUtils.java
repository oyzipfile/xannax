// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

public class CapeUtils
{
    List<UUID> uuids;
    
    public CapeUtils() {
        this.uuids = new ArrayList<UUID>();
        try {
            final URL pastebin = new URL("https://pastebin.com/raw/J5KhQ7Cu");
            final BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                this.uuids.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception ex) {}
    }
    
    public boolean hasCape(final UUID id) {
        return this.uuids.contains(id);
    }
}
