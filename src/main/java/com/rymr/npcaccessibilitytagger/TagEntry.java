package com.rymr.npcaccessibilitytagger;

import lombok.Value;

import javax.annotation.Nullable;
import java.awt.Color;

@Value
public class TagEntry {
    String text;
    @Nullable Color color;
}
