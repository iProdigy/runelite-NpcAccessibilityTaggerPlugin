package com.rymr.npcaccessibilitytagger;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.Text;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

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

    @Getter
    private final Map<Integer, TagEntry> entries = new HashMap<>();

    public void clear() {
        this.entries.clear();
    }

    /**
     * Parses the input config into a list of StandardEntry objects
     */
    public void parse(NpcAccessibilityTaggerConfig config) {
        String input = config.endUserConfig();
        if (input == null || input.isBlank())
            return;

        for (String entry : Text.fromCSV(input)) {
            try {
                String[] parts = entry.split(":");
                if (parts.length < 2) {
                    log.warn("Skipping incomplete entry: '{}'", entry);
                    continue;
                }

                int id = Integer.parseInt(parts[0].trim());
                String text = parts[1].trim();
                Color color = parts.length == 3 ? Color.decode(appendMissingPound(parts[2].trim())) : null;

                TagEntry old = entries.putIfAbsent(id, new TagEntry(text, color));
                if (old != null) {
                    log.warn("Skipping duplicate tag entry: '{}'", entry);
                }
            } catch (Exception e) {
                log.warn("Parse exception: \"{}\"", entry, e);
            }
        }
    }

    /**
     * Appends a missing pound sign to the beginning of the input string
     *
     * @param input
     * @return
     */
    private String appendMissingPound(String input) {
        if (!input.startsWith("#")) {
            return "#" + input;
        }
        return input;
    }

    public static NpcAccessibilityTaggerParser getInstance() {
        return INSTANCE;
    }
}
