package com.deadvikingstudios.bygul.model.items;

public class ItemStack
{
    /**
     * The ID of the item
     */
    private int item;
    /**
     * The amount of items in the stack
     * unsigned
     */
    private byte amount;
    public static final int MAX_AMOUNT = 255;

    //TODO: extra data


    public ItemStack(Item item, int amount)
    {
        this.item = item.getIndex();
        this.amount = (byte)(amount - 128);
    }

    public Item getItem()
    {
        return Item.Items.get(item);
    }

    public int getAmount()
    {
        return amount + 128;
    }

    public void setItem(Item item)
    {
        this.item = item.getIndex();
    }

    public int setAmount(int amount)
    {
        return this.amount = (byte) (amount - 128);
    }

    public void adjustAmount(int amount)
    {
        this.amount += (byte) amount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ItemStack)) return false;

        ItemStack itemStack = (ItemStack) o;

        if (getItem() != itemStack.getItem()) return false;
        return getAmount() == itemStack.getAmount();
    }

    @Override
    public int hashCode()
    {
        int result = item;
        result = 31 * result + getAmount();
        return result;
    }

    @Override
    public String toString()
    {
        return "ItemStack{" +
                "item=" + this.getItem().toString() +
                ",amount=" + this.getAmount() +
                '}';
    }
}
