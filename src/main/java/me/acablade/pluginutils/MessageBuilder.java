package me.acablade.pluginutils;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.logging.Level;

import static me.acablade.pluginutils.Colorize.format;

public class MessageBuilder {

    private TextComponent message;


    public MessageBuilder(String message){
        this.message = new TextComponent(format(message));
    }

    public MessageBuilder(){
    }

    /**
     * Sets the text to a different one
     * @param text text you want to set
     * @return MessageBuilder class
     */
    public MessageBuilder setText(String text){
        if(message==null) message = new TextComponent(format(text));
        else this.message.setText(text);
        return this;
    }

    /**
     * Adds new text
     * @param text text you want to add
     * @return MessageBuilder class
     */
    public MessageBuilder addText(String text){
        if(this.message==null) return this;
        this.message.addExtra(format(text));
        return this;
    }

    /**
     * Adds new component
     * @param component component you want to add
     * @return MessageBuilder class
     */
    public MessageBuilder addComponent(BaseComponent component){
        if(this.message==null) return this;
        this.message.addExtra(component);
        return this;
    }

    /**
     * Adds new messagebuilder
     * @param messageBuilder messagebuilder you want to add
     * @return MessageBuilder class
     */
    public MessageBuilder addMessageBuilder(MessageBuilder messageBuilder){
        if(this.message==null) return this;
        this.message.addExtra(messageBuilder.toTextComponent());
        return this;
    }

    /**
     * Sets the hover event of message
     * @param action Hover action
     * @param text Text you want to show
     * @return MessageBuilder class
     */
    public MessageBuilder setHoverEvent(HoverEvent.Action action, String text){
        if(this.message==null) return this;
        this.message.setHoverEvent(new HoverEvent(action,new Text(format(text))));
        return this;
    }

    /**
     * Sets the hover event of message
     * @param action Hover action
     * @param itemStack Item you want to show
     * @return MessageBuilder class
     */
    public MessageBuilder setHoverEvent(HoverEvent.Action action, ItemStack itemStack) {
        if(this.message==null) return this;
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(convertItemStackToJson(itemStack)) // The only element of the hover events basecomponents is the item json
        };
        this.message.setHoverEvent(new HoverEvent(action,hoverEventComponents));
        return this;
    }

    /**
     * Converts an {@link org.bukkit.inventory.ItemStack} to a Json string
     * for sending with {@link net.md_5.bungee.api.chat.BaseComponent}'s.
     *
     * @param itemStack the item to convert
     * @return the Json string representation of the item
     */
    private String convertItemStackToJson(ItemStack itemStack) {
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to serialize itemstack to nms item", t);
            return null;
        }

        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }

    /**
     * Sets the click event of message
     * @param action Action
     * @param text Text you want to show
     * @return MessageBuilder class
     */
    public MessageBuilder setClickEvent(ClickEvent.Action action, String text){
        if(this.message==null) return this;
        this.message.setClickEvent(new ClickEvent(action, format(text)));
        return this;
    }

    /**
     * Sends player the message
     * @param player Player to send message
     * @return is the operation successfully completed
     */
    public boolean send(Player player){
        if(this.message==null) return false;
        player.spigot().sendMessage(this.message);
        return true;
    }

    public TextComponent toTextComponent(){
        return this.message;
    }

}
