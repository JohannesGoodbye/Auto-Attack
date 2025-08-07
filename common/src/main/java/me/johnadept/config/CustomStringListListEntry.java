package me.johnadept.config;

import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomStringListListEntry extends StringListListEntry {

    public CustomStringListListEntry(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront);
    }

    public void reinitializeCells(StringListListEntry stringListListEntry, List<String> suggestions, Predicate<String> filter) {
        List<String> value = this.getValue();
        this.widgets.removeAll(this.cells);
        this.cells.clear();
        for (String s : value) {
            CustomStringListCell cell = new CustomStringListCell(s, stringListListEntry, suggestions, filter);
            this.cells.add(cell);
        }
        this.widgets.addAll(this.cells);
    }
}
