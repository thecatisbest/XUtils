package dev.pixelstudios.xutils.item;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.pixelstudios.xutils.ReflectionUtil;
import dev.pixelstudios.xutils.XUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public interface TagHandler {

    ItemStack set(ItemStack item, String key, String value);
    String get(ItemStack item, String key);

    static TagHandler modern() {
        return new TagHandler() {

            @Override
            public ItemStack set(ItemStack item, String key, String value) {
                ItemMeta meta = item.getItemMeta();
                NamespacedKey namespacedKey = new NamespacedKey(XUtils.getPlugin(), key);

                meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
                item.setItemMeta(meta);

                return item;
            }

            @Override
            public String get(ItemStack item, String key) {
                NamespacedKey namespacedKey = new NamespacedKey(XUtils.getPlugin(), key);
                return item.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            }

        };
    }

    static TagHandler legacy() {
        return new TagHandler() {

            @Override
            public ItemStack set(ItemStack item, String key, String value) {
                NBT.modify(item, nbt -> {
                    nbt.setString(key, value);
                });
                return item;
            }

            @Override
            public String get(ItemStack item, String key) {
                return NBT.get(item, nbt -> {
                    return nbt.getString(key);
                });
            }

        };
    }

    static TagHandler handler() {
        if (XUtils.getCustomTagHandler() != null) {
            return XUtils.getCustomTagHandler();
        } else {
            return ReflectionUtil.supports(14) ? modern() : legacy();
        }
    }

}
