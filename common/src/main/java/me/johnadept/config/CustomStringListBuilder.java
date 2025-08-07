package me.johnadept.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BaseListEntry;
import me.shedaniel.clothconfig2.impl.builders.StringListBuilder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomStringListBuilder extends StringListBuilder {
    public CustomStringListBuilder(ConfigEntryBuilder entryBuilder, Component fieldNameKey, List<String> value) {
        super(entryBuilder.getResetButtonKey(), fieldNameKey, value);
    }

    public @NotNull CustomStringListListEntry buildCustom(List<String> suggestions, Predicate<String> filter) {
        CustomStringListListEntry entry = new CustomStringListListEntry(this.getFieldNameKey(), (List)this.value, this.isExpanded(), (Supplier)null, this.getSaveConsumer(), this.defaultValue, this.getResetButtonKey(), this.isRequireRestart(), this.isDeleteButtonEnabled(), this.isInsertInFront());
        BaseListEntry baseListEntry = this.build();
        if (baseListEntry.getCreateNewInstance() != null) {
            entry.setCreateNewInstance(baseListEntry.getCreateNewInstance());
            entry.reinitializeCells(entry, suggestions, filter);
        }

        entry.setInsertButtonEnabled(this.isInsertButtonEnabled());
        entry.setCellErrorSupplier(this.cellErrorSupplier);
        entry.setTooltipSupplier(() -> {
            return (Optional)this.getTooltipSupplier().apply(entry.getValue());
        });
        entry.setAddTooltip(this.getAddTooltip());
        entry.setRemoveTooltip(this.getRemoveTooltip());
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> {
                return (Optional)this.errorSupplier.apply(entry.getValue());
            });
        }

        return (CustomStringListListEntry)this.finishBuilding(entry);
    }
}
