package me.acablade.pluginutils;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemBuilder {

    private ItemStack itemStack;


    public ItemBuilder(Material material){
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount){
        this.itemStack = new ItemStack(material, amount);
    }

    /**
     * Set display name
     * @param name the name you want to give to itemstack
     * @return ItemBuilder class
     */
    public ItemBuilder withDisplayName(String name){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Colorize.format(name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder withLore(String... lore){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreString = new ArrayList<>();
        Arrays.stream(lore).filter(Objects::nonNull).forEach(line -> loreString.add(Colorize.format(line)));
        itemMeta.setLore(loreString);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder withAmount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level, boolean ignoreLevelCap){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(enchant,level,ignoreLevelCap);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder withFlags(ItemFlag... flags){
        ItemMeta itemMeta = itemStack.getItemMeta();
        Arrays.stream(flags).filter(Objects::nonNull).forEach(itemMeta::addItemFlags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder withAttribute(Attribute attr, AttributeModifier attributeModifier){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addAttributeModifier(attr, attributeModifier);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public boolean give(Player player){
        if(this.itemStack==null) return false;
        player.getInventory().addItem(this.itemStack);
        return true;
    }


    public ItemStack getItemStack(){
        return this.itemStack;
    }

}
