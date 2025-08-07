package me.johnadept.config;

import me.johnadept.AutoAttackClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        AutoAttackConfig config = AutoAttackConfig.get();
        AutoAttackConfig defaultConfig = new AutoAttackConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Component.translatable("menu.auto_attack.config.title"))
                .setParentScreen(parent);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("menu.auto_attack.config.category.general"));

        BooleanListEntry enable = entryBuilder
                .startBooleanToggle(Component.translatable("menu.auto_attack.config.enableMod"), config.enableMod)
                .setDefaultValue(defaultConfig.enableMod)
                .setSaveConsumer(newValue -> config.enableMod = newValue)
                .build();
        general.addEntry(enable);

        BooleanListEntry attackNonHostile = entryBuilder
                .startBooleanToggle(Component.translatable("menu.auto_attack.config.attackNonHostile"), config.attackNonHostile)
                .setDefaultValue(defaultConfig.attackNonHostile)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.attackNonHostile = newValue)
                .build();
        general.addEntry(attackNonHostile);

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("menu.auto_attack.config.protectTamedMobs"), config.protectTamedMobs)
                .setDefaultValue(defaultConfig.protectTamedMobs)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setRequirement(Requirement.isTrue(attackNonHostile))
                .setSaveConsumer(newValue -> config.protectTamedMobs = newValue)
                .build()
        );

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("menu.auto_attack.config.attackNonLiving"), config.attackNonLiving)
                .setDefaultValue(defaultConfig.attackNonLiving)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setRequirement(Requirement.isTrue(attackNonHostile))
                .setSaveConsumer(newValue -> config.attackNonLiving = newValue)
                .build()
        );

        BooleanListEntry disableOnLowDurability = entryBuilder
                .startBooleanToggle(Component.translatable("menu.auto_attack.config.disableOnLowDurability"), config.disableOnLowDurability)
                .setDefaultValue(defaultConfig.disableOnLowDurability)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.disableOnLowDurability = newValue)
                .build();
        general.addEntry(disableOnLowDurability);

        general.addEntry(entryBuilder
                .startIntField(Component.translatable("menu.auto_attack.config.durabilityThreshold"), config.durabilityThreshold)
                .setTooltip(Component.translatable("menu.auto_attack.config.durabilityThreshold.tooltip"))
                .setMin(1)
                .setDefaultValue(defaultConfig.durabilityThreshold)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setRequirement(Requirement.isTrue(disableOnLowDurability))
                .setSaveConsumer(newValue -> config.durabilityThreshold = newValue)
                .build()
        );

        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("menu.auto_attack.config.rotationAngle"), config.rotationAngle, 0,180)
                .setDefaultValue(defaultConfig.rotationAngle)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.rotationAngle = newValue)
                .build()
        );

        general.addEntry(entryBuilder
                .startIntSlider(Component.translatable("menu.auto_attack.config.rotationSpeed"), config.rotationSpeed,0,20)
                .setDefaultValue(defaultConfig.rotationSpeed)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.rotationSpeed = newValue)
                .build()
        );

        BooleanListEntry autoAlignYaw = entryBuilder
                .startBooleanToggle(Component.translatable("menu.auto_attack.config.autoAlignYaw"), config.autoAlignYaw)
                .setTooltip(Component.translatable("menu.auto_attack.config.autoAlignYaw.tooltip"))
                .setDefaultValue(defaultConfig.autoAlignYaw)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.autoAlignYaw = newValue)
                .build();
        general.addEntry(autoAlignYaw);

        general.addEntry(entryBuilder
                .startFloatField(Component.translatable("menu.auto_attack.config.autoAlignYawOffset"), config.autoAlignYawOffset)
                .setTooltip(Component.translatable("menu.auto_attack.config.autoAlignYawOffset.tooltip"))
                .setDefaultValue(defaultConfig.autoAlignYawOffset)
                .setRequirement(Requirement.isTrue(autoAlignYaw))
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.autoAlignYawOffset = newValue)
                .build()
        );

        general.addEntry(entryBuilder
                .startEnumSelector(
                        Component.translatable("menu.auto_attack.config.display_mode"),
                        MessageDisplayMode.class,
                        config.displayMode
                )
                .setTooltip(Component.translatable("menu.auto_attack.config.display_mode.tooltip"))
                .setDefaultValue(defaultConfig.displayMode)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setSaveConsumer(newValue -> config.displayMode = newValue)
                .setEnumNameProvider(value -> ((MessageDisplayMode)value).getDisplayName())
                .build()
        );

// Test Code
/*
// Prepare valid entity IDs
//            Set<String> validEntityIds = BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
//                    .map(entry -> BuiltInRegistries.ENTITY_TYPE.getKey(entry.getValue()))
//                    .filter(Objects::nonNull)
//                    .map(ResourceLocation::toString)
//                    .filter(id -> !id.equals("minecraft:player"))
//                    .sorted()
//                    .collect(Collectors.toCollection(LinkedHashSet::new));
//
//            DropdownMenuBuilder<ResourceLocation> resourceLocationDropdown = entryBuilder.startDropdownMenu(
//                    Component.literal("Entity Selector"),
//                    ResourceLocation.fromNamespaceAndPath("minecraft", "horse"),                        // ResourceLocation
//                    ResourceLocation::tryParse,
//                    rl -> Component.literal(rl.toString())
//            );
//
//            Set<ResourceLocation> validEntityResourceLocations = BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
//                    .map(entry -> BuiltInRegistries.ENTITY_TYPE.getKey(entry.getValue()))  // Get ResourceLocation key for each EntityType
//                    .filter(rl -> !rl.getPath().equals("player"))             // Filter out player if you want
//                    .collect(Collectors.toCollection(TreeSet::new));
//
//            resourceLocationDropdown.setSelections(validEntityResourceLocations)
//                    .setSuggestionMode(true);

//            general.addEntry(resourceLocationDropdown.build());

// 1. Prepare valid entity IDs
        Set<String> validEntityIds = BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                .map(entry -> BuiltInRegistries.ENTITY_TYPE.getKey(entry.getValue()))
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .filter(id -> !id.equals("minecraft:player"))
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

// 2. Clone whitelist or use default
        List<String> initialWhitelist = config.entityWhitelist != null
                ? new ArrayList<>(config.entityWhitelist)
                : new ArrayList<>(List.of("minecraft:zombie"));

// 3. Build the nested list with dropdown entries
        NestedListListEntry<String, DropdownBoxEntry<String>> whitelistEntry = new NestedListListEntry<>(
                Component.translatable("menu.auto_attack.config.whitelist"),
                initialWhitelist,
                false,
                Optional.empty(),
                newList -> config.entityWhitelist = new ArrayList<String>((List)newList), // FIXED: explicit type
                () -> new ArrayList<>(), // FIXED: explicit list
                entryBuilder.getResetButtonKey(),
                true,
                true,
                (currentValue, parentEntry) -> {
                    String current = currentValue != null ? currentValue.toString() : "minecraft:zombie";

                    DropdownMenuBuilder<String> dropdown = entryBuilder
                            .startStringDropdownMenu(
                                    Component.translatable("Entity ID"),
                                    current,
                                    Component::literal,
                                    new DropdownBoxEntry.DefaultSelectionCellCreator<>()
                            );

                    dropdown.setSelections(validEntityIds);
                    dropdown.setSuggestionMode(true);
                    dropdown.setDefaultValue("minecraft:zombie");
                    dropdown.build();

                    return dropdown;
                }
        );
// 2. Clone current config list or use default
        List<ResourceLocation> initialWhitelist = config.entityWhitelist != null
                ? new ArrayList<>(config.entityWhitelist)
                : new ArrayList<>(List.of(ResourceLocation.fromNamespaceAndPath("minecraft", "zombie")));

// 3. Create top cell creator (for dropdown UI)
        Function<ResourceLocation, ModifiedDropdownBoxEntry.SelectionTopCellElement<ResourceLocation>> topCellCreator =
                rl -> ModifiedTopCellElementBuilder.of(
                        rl,
                        ResourceLocation::tryParse,
                        id -> Component.literal(id.toString())
                );

// 4. Build the dropdown list
        DropdownListBuilder<ResourceLocation> dropdownList = DropdownListBuilder.startDropdownList(
                Component.translatable("menu.auto_attack.config.whitelist"),
                initialWhitelist,
                topCellCreator,
                new ModifiedDropdownBoxEntry.DefaultSelectionCellCreator<>()
        );

        dropdownList.setDefaultEntryValue(ResourceLocation.fromNamespaceAndPath("minecraft","horse"));
        dropdownList.setSelections(validEntityIds);

        general.addEntry(dropdownList.build());
*/

        Set<ResourceLocation> entityBlacklistIds = BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                .map(entry -> BuiltInRegistries.ENTITY_TYPE.getKey(entry.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        Set<ResourceLocation> entityWhitelistIds = entityBlacklistIds.stream()
                .filter(id -> AutoAttackClient.ENTITY_ID_WHITELIST_VALIDATOR.apply(id.toString()).isEmpty())
                .collect(Collectors.toCollection(TreeSet::new));

        CustomStringListBuilder whitelistBuilder = new CustomStringListBuilder(entryBuilder, Component.translatable("menu.auto_attack.config.whitelist"), config.entityWhitelist);
        whitelistBuilder
                .setTooltip(Component.translatable("menu.auto_attack.config.whitelist.tooltip"))
                .setDefaultValue(defaultConfig.entityWhitelist)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setInsertInFront(true)
                .setCellErrorSupplier(AutoAttackClient.ENTITY_ID_WHITELIST_VALIDATOR)
                .setCreateNewInstance(entry -> new CustomStringListCell("", entry, entityWhitelistIds.stream().map(ResourceLocation::toString).toList(), AutoAttackClient::isValidResourceLocationText))
                .setSaveConsumer(newList -> config.entityWhitelist = newList);
        general.addEntry(whitelistBuilder.buildCustom(entityWhitelistIds.stream().map(ResourceLocation::toString).collect(Collectors.toList()), AutoAttackClient::isValidResourceLocationText));

        CustomStringListBuilder blackListBuilder = new CustomStringListBuilder(entryBuilder, Component.translatable("menu.auto_attack.config.blacklist"), config.entityBlacklist);
        blackListBuilder
                .setTooltip(Component.translatable("menu.auto_attack.config.blacklist.tooltip"))
                .setDefaultValue(defaultConfig.entityBlacklist)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setInsertInFront(true)
                .setCellErrorSupplier(AutoAttackClient.ENTITY_ID_BLACKLIST_VALIDATOR)
                .setCreateNewInstance(entry -> new CustomStringListCell("", entry, entityBlacklistIds.stream().map(ResourceLocation::toString).toList(), AutoAttackClient::isValidResourceLocationText))
                .setSaveConsumer(newList -> config.entityBlacklist = newList);
        general.addEntry(blackListBuilder.buildCustom(entityBlacklistIds.stream().map(ResourceLocation::toString).collect(Collectors.toList()), AutoAttackClient::isValidResourceLocationText));

// Backup Code
/*
        general.addEntry(entryBuilder
                .startStrList(
                        Component.translatable("menu.auto_attack.config.whitelist"),
                        config.entityWhitelist
                )
                .setTooltip(Component.translatable("menu.auto_attack.config.whitelist.tooltip"))
                .setDefaultValue(defaultConfig.entityWhitelist)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setInsertInFront(true)
                .setCellErrorSupplier(AutoAttackClient.ENTITY_ID_WHITELIST_VALIDATOR)
                .setCreateNewInstance(entry -> new StringListListEntry.StringListCell("", entry) {
                    {
                        widget = new CustomEditBox(widget, this, this::isValidText);
                        widget.setResponder(text -> {
                            entityWhitelistIds.stream()
                                    .filter(id -> id.toString().startsWith(text))
                                    .findFirst()
                                    .ifPresentOrElse(suggestion -> {
                                        if (!text.equals(suggestion.toString())) {
                                            widget.setSuggestion(suggestion.toString().replaceFirst(text, ""));
                                        } else {
                                            widget.setSuggestion(null);
                                        }
                                    }, () -> widget.setSuggestion(null));
                        });
                    }

//                    @Override
//                    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//                        if (AutoAttackClient.handleTabLogic(keyCode, scanCode, modifiers, widget)) return true;
//                        return super.keyPressed(keyCode, scanCode, modifiers);
//                    }

                })
                .setSaveConsumer(newList -> config.entityWhitelist = newList)
                .build()
        );
*/
/*
        general.addEntry(entryBuilder
                .startStrList(
                        Component.translatable("menu.auto_attack.config.blacklist"),
                        config.entityBlacklist
                )
                .setTooltip(Component.translatable("menu.auto_attack.config.blacklist.tooltip"))
                .setDefaultValue(defaultConfig.entityBlacklist)
                .setDisplayRequirement(Requirement.isTrue(enable))
                .setInsertInFront(true)
                .setCellErrorSupplier(AutoAttackClient.ENTITY_ID_BLACKLIST_VALIDATOR)
                .setCreateNewInstance(entry -> new StringListListEntry.StringListCell("", entry) {
                    {
                        widget = new CustomEditBox(widget, this, this::isValidText);
                        widget.setResponder(text -> {
                            entityBlacklistIds.stream()
                                    .filter(id -> id.toString().startsWith(text))
                                    .findFirst()
                                    .ifPresentOrElse(suggestion -> {
                                        if (!text.equals(suggestion.toString())) {
                                            widget.setSuggestion(suggestion.toString().replaceFirst(text, ""));
                                        } else {
                                            widget.setSuggestion(null);
                                        }
                                    }, () -> widget.setSuggestion(null));
                        });
                    }

//                    @Override
//                    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//                        if (AutoAttackClient.handleTabLogic(keyCode, scanCode, modifiers, widget)) return true;
//                        return super.keyPressed(keyCode, scanCode, modifiers);
//                    }

                })
                .setSaveConsumer(newList -> config.entityBlacklist = newList)
                .build()
        );
 */

        builder.setSavingRunnable(AutoAttackConfig::save);

        return builder.build();
    };
}
