package com.deadvikingstudios.norsetown.model.items;

import com.deadvikingstudios.norsetown.model.RegistrationException;
import com.deadvikingstudios.norsetown.model.tiles.Tile;

public class Item
{
    protected final String UNLOCALIZED_NAME;
    protected final int INDEX;

    public Item(int index, String unlocalizedName)
    {
        this.UNLOCALIZED_NAME = "item_" + unlocalizedName;
        this.INDEX = index;
        if(this.INDEX < 0) try
        {
            throw new RegistrationException("Item: " + UNLOCALIZED_NAME + " was initialized with a negative index");
        } catch (RegistrationException e)
        {
            e.printStackTrace();
        }
    }

    public String getUnlocalizedName()
    {
        return UNLOCALIZED_NAME;
    }

    public int getIndex()
    {
        return INDEX;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (INDEX != item.INDEX) return false;
        return UNLOCALIZED_NAME != null ? UNLOCALIZED_NAME.equals(item.UNLOCALIZED_NAME) : item.UNLOCALIZED_NAME == null;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "unlocalizedName='" + UNLOCALIZED_NAME + '\'' +
                ",index=" + INDEX +
                '}';
    }

    @Override
    public int hashCode()
    {
        int result = UNLOCALIZED_NAME != null ? UNLOCALIZED_NAME.hashCode() : 0;
        result = 31 * result + INDEX;
        return result;
    }

    public static class Items
    {
        private static Item[] items = new Item[512];

        public static Item NULL;

        public static Item itemTimberFir;
        public static Item itemLeavesFir; //needles

        public static void init()
        {
            int i = 0;

            register(NULL = new Item(i++, "null"));

            register(itemTimberFir = new Item(i++, "timber_fir"));
            register(itemLeavesFir = new Item(i++, "leaves_fir"));
        }

        public static void register(Item item) { items[item.getIndex()] = item; }

        public static void unregister(Tile tile)
        {
            items[tile.getIndex()] = null;
        }

        public static Item get(int id)
        {
            Item item = items[id];
            return item == null ? NULL : item;
        }
    }
}
