/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.8.2
 */

package sumimakito.android.quickkv;

import android.content.Context;

import java.util.HashMap;

import sumimakito.android.quickkv.database.KeyValueDatabase;

public class QuickKV
{
	private Context pContext;
	private HashMap<String, KeyValueDatabase> sKVDB;

	public QuickKV(Context context)
	{
		this.pContext = context;
		this.sKVDB = new HashMap<String, KeyValueDatabase>();
	}

	public KeyValueDatabase getDatabase()
	{
		if (!this.sKVDB.containsKey(QKVConfig.KVDB_FILE_NAME))
		{
			this.sKVDB.put(QKVConfig.KVDB_FILE_NAME, new KeyValueDatabase(pContext));
		}
		return this.sKVDB.get(QKVConfig.KVDB_FILE_NAME);
	}

	public KeyValueDatabase getDatabase(String dbAlias)
	{
		if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME))
		{
			return getDatabase();
		}
		if (dbAlias == null)
		{
			return null;
		}
		else
		{
			if (dbAlias.length() == 0)
			{
				dbAlias = QKVConfig.KVDB_FILE_NAME;
			}
			dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ?dbAlias: dbAlias + QKVConfig.KVDB_EXT;
		}
		if (!this.sKVDB.containsKey(dbAlias))
		{
			this.sKVDB.put(dbAlias, new KeyValueDatabase(pContext, dbAlias));
		}

		return this.sKVDB.get(dbAlias);
	}

	public KeyValueDatabase getDatabase(String dbAlias, String key)
	{
		if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME))
		{
			return getDatabase();
		}
		if (dbAlias == null)
		{
			return null;
		}
		else
		{
			if (dbAlias.length() == 0)
			{
				dbAlias = QKVConfig.KVDB_FILE_NAME;
			}
			dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ?dbAlias: dbAlias + QKVConfig.KVDB_EXT;
		}
		if (!this.sKVDB.containsKey(dbAlias))
		{
			this.sKVDB.put(dbAlias, new KeyValueDatabase(pContext, dbAlias, key));
		}

		return this.sKVDB.get(dbAlias);
	}

	public boolean isDatabaseOpened()
	{
		return this.sKVDB.containsKey(QKVConfig.KVDB_FILE_NAME);
	}

	public boolean isDatabaseOpened(String dbAlias)
	{
		if (dbAlias.equals(QKVConfig.KVDB_NAME) || dbAlias.equals(QKVConfig.KVDB_FILE_NAME))
		{
			return isDatabaseOpened();
		}
		if (dbAlias == null)
		{
			return false;
		}
		else
		{
			if (dbAlias.length() == 0)
			{
				dbAlias = QKVConfig.KVDB_FILE_NAME;
			}
			dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ?dbAlias: dbAlias + QKVConfig.KVDB_EXT;
		}
		return this.sKVDB.containsKey(dbAlias);
	}

	public boolean releaseDatabase()
	{
		if (isDatabaseOpened())
		{
			this.sKVDB.remove(QKVConfig.KVDB_FILE_NAME);
			return true;
		}
		return false;
	}

	public boolean releaseDatabase(String dbAlias)
	{
		if (isDatabaseOpened(dbAlias))
		{
			dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ?dbAlias: dbAlias + QKVConfig.KVDB_EXT;
			this.sKVDB.remove(dbAlias);
			return true;
		}
		return false;
	}

	public void releaseAllDatabases()
	{
		this.sKVDB.clear();
	}
}
