package org.mineacademy.fo.remain.nbt;

/**
 * {@link NBTListCompound} implementation for NBTLists
 *
 * @author tr7zw
 *
 */
public class NBTCompoundList extends NBTList<ReadWriteNBT> implements ReadWriteNBTCompoundList {

	protected NBTCompoundList(NBTCompound owner, String name, NBTType type, Object list) {
		super(owner, name, type, list);
	}

	/**
	 * Adds a new Compound to the end of the List and returns it.
	 *
	 * @return The added {@link NBTListCompound}
	 */
	@Override
	public NBTListCompound addCompound() {
		return (NBTListCompound) this.addCompound(null);
	}

	/**
	 * Adds a copy of the Compound to the end of the List and returns it. When null
	 * is given, a new Compound will be created
	 *
	 * @param comp
	 * @return
	 */
	public NBTCompound addCompound(NBTCompound comp) {
		if (this.getParent().isReadOnly())
			throw new NbtApiException("Tried setting data in read only mode!");
		try {
			final Object compound = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
			if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId())
				ReflectionMethod.LIST_ADD.run(this.listObject, this.size(), compound);
			else
				ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, compound);
			this.getParent().saveCompound();
			final NBTListCompound listcomp = new NBTListCompound(this, compound);
			if (comp != null)
				listcomp.mergeCompound(comp);
			return listcomp;
		} catch (final Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public ReadWriteNBT addCompound(ReadableNBT comp) {
		if (comp instanceof NBTCompound)
			return this.addCompound((NBTCompound) comp);
		return null;
	}

	/**
	 * Adds a new Compound to the end of the List.
	 *
	 *
	 * @deprecated Please use addCompound!
	 * @param empty
	 * @return True, if compound was added
	 */
	@Override
	@Deprecated
	public boolean add(ReadWriteNBT empty) {
		return this.addCompound(empty) != null;
	}

	@Override
	public void add(int index, ReadWriteNBT element) {
		if (element != null)
			throw new NbtApiException("You need to pass null! ListCompounds from other lists won't work.");
		if (this.getParent().isReadOnly())
			throw new NbtApiException("Tried setting data in read only mode!");
		try {
			final Object compound = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
			if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId())
				ReflectionMethod.LIST_ADD.run(this.listObject, index, compound);
			else
				ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, compound);
			super.getParent().saveCompound();
		} catch (final Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public NBTListCompound get(int index) {
		try {
			final Object compound = ReflectionMethod.LIST_GET_COMPOUND.run(this.listObject, index);
			return new NBTListCompound(this, compound);
		} catch (final Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public NBTListCompound set(int index, ReadWriteNBT element) {
		throw new NbtApiException("This method doesn't work in the ListCompound context.");
	}

	@Override
	protected Object asTag(ReadWriteNBT object) {
		return null;
	}

}
