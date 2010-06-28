package org.loon.framework.android.game.extend.db;

public interface Serializer {

	public byte[] getBytes(final Object o);

	public Object getObject(final byte[] b);

	public int getType();

}
