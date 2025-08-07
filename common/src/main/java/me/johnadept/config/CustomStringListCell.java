package me.johnadept.config;

import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;

import java.util.List;
import java.util.function.Predicate;

public class CustomStringListCell extends StringListListEntry.StringListCell {
    public CustomStringListCell(String value, StringListListEntry listListEntry, List<String> suggestions, Predicate<String> filter) {
        super(value, listListEntry);

        this.widget = new CustomEditBox(widget, this, filter);

        widget.setResponder(text -> {
            suggestions.stream()
                    .filter(id -> id.startsWith(text))
                    .findFirst()
                    .ifPresentOrElse(suggestion -> {
                        if (!text.equals(suggestion)) {
                            widget.setSuggestion(suggestion.replaceFirst(text, ""));
                        } else {
                            widget.setSuggestion(null);
                        }
                    }, () -> widget.setSuggestion(null));
        });
    }
}
