package me.johnadept.config;

import me.johnadept.AutoAttackClient;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.Predicate;

public class CustomEditBox extends EditBox {
    public CustomEditBox(EditBox editBox, StringListListEntry.StringListCell stringListCell, Predicate<String> filter) {
        super(Minecraft.getInstance().font, 0, 0, 100, 18, Component.empty());

        this.setFilter(filter);
        this.setMaxLength(Integer.MAX_VALUE);
        this.setBordered(editBox.isBordered());
        this.setValue(editBox.getValue());
        this.moveCursorToStart(false);
        this.setResponder((s) -> {
            this.setTextColor(stringListCell.getPreferredTextColor());
        });
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == GLFW.GLFW_KEY_TAB && (keyEvent.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0) {
            if (AutoAttackClient.handleShiftTabLogic(this)) return true;
        } else if (keyEvent.key() == GLFW.GLFW_KEY_TAB && AutoAttackClient.handleTabLogic(this)) return true;
        return super.keyPressed(keyEvent);
    }
}
