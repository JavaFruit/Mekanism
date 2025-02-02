package mekanism.common.lib.security;

import javax.annotation.Nonnull;
import mekanism.api.IIncrementalEnum;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import net.minecraft.network.chat.Component;

public enum SecurityMode implements IIncrementalEnum<SecurityMode>, IHasTextComponent {
    PUBLIC(MekanismLang.PUBLIC, EnumColor.BRIGHT_GREEN),
    PRIVATE(MekanismLang.PRIVATE, EnumColor.RED),
    TRUSTED(MekanismLang.TRUSTED, EnumColor.INDIGO);

    private static final SecurityMode[] MODES = values();

    private final ILangEntry langEntry;
    private final EnumColor color;

    SecurityMode(ILangEntry langEntry, EnumColor color) {
        this.langEntry = langEntry;
        this.color = color;
    }

    @Override
    public Component getTextComponent() {
        return langEntry.translateColored(color);
    }

    @Nonnull
    @Override
    public SecurityMode byIndex(int index) {
        return byIndexStatic(index);
    }

    public static SecurityMode byIndexStatic(int index) {
        return MathUtils.getByIndexMod(MODES, index);
    }
}