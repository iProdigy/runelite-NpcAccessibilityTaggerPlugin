package com.rymr.npcaccessibilitytagger;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.Text;

import java.awt.*;
import java.util.*;

/*
 * Copyright (c) 2023, R-Y-M-R
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
@Slf4j
public class NpcAccessibilityTaggerParser {

    private static final NpcAccessibilityTaggerParser INSTANCE = new NpcAccessibilityTaggerParser();
    private final Map<Integer, StandardEntry> entries = new HashMap<>(10);

    public Map<Integer, StandardEntry> getEntries() {
        return entries;
    }

    /**
     * Parses the input config into a list of StandardEntry objects
     *
     * @param config
     * @return List of StandardEntry objects
     */
    public Map<Integer, StandardEntry> parse(NpcAccessibilityTaggerConfig config) {
        String input = config.endUserConfig();
        if (input == null || input.isEmpty())
            return null;

        for (String entry : Text.fromCSV(input)) {
            try {
                String[] parts = entry.split(":");
//                log.info("Entry: "+entry+"\nParts: " + parts.length);
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    entries.put(id, new StandardEntry(id, parts[1].trim()));
                } else if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0].trim());
                    entries.put(id, new ExtendedEntry(id, parts[1].trim(), Color.decode(appendMissingPound(parts[2].trim()))));
                }
            } catch (Exception e) {
                log.warn("Parse exception: \"" + entry + "\"\n" + e.getMessage());
            }
        }
        return entries;
    }

    /**
     * Appends a missing pound sign to the beginning of the input string
     *
     * @param input
     * @return
     */
    private String appendMissingPound(String input) {
        if (!input.startsWith("#")) {
            input = "#" + input;
        }
        return input;
    }

    public static NpcAccessibilityTaggerParser getInstance() {
        return INSTANCE;
    }
}
