/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.8.2
 */

package sumimakito.android.quickkv.database;

import android.content.*;

import org.json.JSONObject;

import java.io.*;
import java.util.*;
import sumimakito.android.quickkv.*;
import sumimakito.android.quickkv.security.*;

public class KeyValueDatabase implements QKVDatabase
{
	private HashMap<Object, Object> dMap;
	private Context pContext;
	private String dbAlias;
	private String pKey;

	public KeyValueDatabase(Context context)
	{
		this.pContext = context;
		this.dbAlias = QKVConfig.KVDB_FILE_NAME;
		this.dMap = new HashMap<Object, Object>();
		this.sync(false);
		QKVLogger.log("i", "KVDB Initialized!");
	}

	public KeyValueDatabase(Context context, String dbAlias)
	{
		this.pContext = context;
		this.dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ?dbAlias: dbAlias + QKVConfig.KVDB_EXT;
		this.dMap = new HashMap<Object, Object>();
		this.sync(false);
		QKVLogger.log("i", "KVDB Initialized!");
	}

	public KeyValueDatabase(Context context, String dbAlias, String key)
	{
		this.pKey = key;
		this.pContext = context;
		this.dbAlias = dbAlias.endsWith(QKVConfig.KVDB_EXT) ?dbAlias: dbAlias + QKVConfig.KVDB_EXT;
		this.dMap = new HashMap<Object, Object>();
		this.sync(false);
		QKVLogger.log("i", "KVDB Initialized!");
	}

	@Override
	public <K extends Object, V extends Object> boolean put(K k, V v)
	{
		if (k == null || v == null)
		{
			return false;
		}
		this.dMap.put(k, v);
		return true;
	}

	@Override
	public <K extends Object> Object get(K k)
	{
		if (k == null)
		{
			return null;
		}
		if (this.dMap.containsKey(k)) return this.dMap.get(k);
		else return null;
	}

	@Override
	public <K extends Object> boolean containsKey(K k)
	{
		if (this.dMap.containsKey(k)) return true;
		else return false;
	}

	@Override
	public <V extends Object> boolean containsValue(V v)
	{
		if (this.dMap.containsValue(v)) return true;
		else return false;
	}

	@Override
	public <K extends Object> boolean remove(K k)
	{
		if (k == null)
		{
			return false;
		}
		if (this.dMap.containsKey(k)) 
		{
			this.dMap.remove(k);
			return true;
		}
		else return false;
	}

	@Override
	public <K extends Object> boolean remove(K[] k)
	{
		if (k == null || k.length == 0)
		{
			return false;
		}
		int r = 0;
		for (K key:k)
		{
			if (this.dMap.containsKey(key))
			{
				this.dMap.remove(key);
				r++;
			}
		}
		if (r < k.length) return false;
		else return true;
	}

	@Override
	public void clear()
	{
		this.dMap.clear();
	}

	@Override
	public int size()
	{
		return this.dMap.size();
	}

	public List<Object> getKeys()
	{
		List<Object> list = new ArrayList<Object>();
		if (this.dMap.size() > 0)
		{
			Iterator iter = dMap.entrySet().iterator(); 
			while (iter.hasNext())
			{ 
				Map.Entry entry = (Map.Entry) iter.next(); 
				Object key = entry.getKey(); 
				list.add(key);
			} 
		}
		return list;
	}

	public List<Object> getValues()
	{
		List<Object> list = new ArrayList<Object>();
		if (this.dMap.size() > 0)
		{
			Iterator iter = dMap.entrySet().iterator(); 
			while (iter.hasNext())
			{ 
				Map.Entry entry = (Map.Entry) iter.next(); 
				Object value = entry.getValue(); 
				list.add(value);
			} 
		}
		return list;
	}

	public boolean persist()
	{
		if (this.dMap.size() > 0)
		{
			try
			{
				JSONObject treeRoot = new JSONObject();
				treeRoot.put("kv_prop", new JSONObject());
				JSONObject propRoot = (JSONObject) treeRoot.get("kv_prop");
				propRoot.put("strc_ver", "0.8@3");
				propRoot.put("enc_enabled", (this.pKey != null && this.pKey.length() > 0));
				treeRoot.put("kv_data", new JSONObject());
				JSONObject dataRoot = (JSONObject) treeRoot.get("kv_data");
				Iterator iter = this.dMap.entrySet().iterator(); 
				while (iter.hasNext())
				{ 
					Map.Entry entry = (Map.Entry) iter.next(); 
					Object key = entry.getKey(); 
					Object val = entry.getValue(); 
					if (DataProcessor.Persistable.isValidDataType(key)
						&& DataProcessor.Persistable.isValidDataType(val))
					{
						if (this.pKey != null && this.pKey.length() > 0) dataRoot.put(AES256.encode(this.pKey, DataProcessor.Persistable.addPrefix(key)), AES256.encode(this.pKey, DataProcessor.Persistable.addPrefix(val)));
						else dataRoot.put(DataProcessor.Persistable.addPrefix(key), DataProcessor.Persistable.addPrefix(val));
					}
				} 
				FileOutputStream kvdbFos = pContext.openFileOutput(dbAlias == null ?QKVConfig.KVDB_FILE_NAME: dbAlias, Context.MODE_PRIVATE);
				kvdbFos.write(treeRoot.toString().getBytes());
				kvdbFos.close();
				return true;
			}
			catch (Exception e)
			{
				QKVLogger.ex(e);
				return false;
			}
		}
		else return true;
	}

	public void persist(final Callback callback)
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					synchronized (dMap)
					{
						if (persist()) callback.onSuccess();
						else callback.onFailed();
					}
				}
			}).start();
	}

	public boolean sync()
	{
		return this.sync(true);
	}

	public boolean sync(boolean merge)
	{
		try
		{
			if (!merge)
			{
				this.dMap.clear();
			}
			File kvdbFile = new File(pContext.getFilesDir(), dbAlias == null ? QKVConfig.KVDB_FILE_NAME: dbAlias);
			String rawData = QKVFSReader.readFileBFD(kvdbFile.getAbsolutePath());
			if (rawData.length() > 0)
			{
				JSONObject treeRoot = new JSONObject(rawData);
				if (parseKVJS((JSONObject) treeRoot.get("kv_data"))) return true;
				else return false;
			}
			return true;
		}
		catch (Exception e)
		{
			QKVLogger.ex(e);
			return false;
		}
	}
	
	public void sync(final Callback callback)
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					synchronized (dMap)
					{
						if (sync()) callback.onSuccess();
						else callback.onFailed();
					}
				}
			}).start();
	}
	
	public void sync(final boolean merge, final Callback callback)
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					synchronized (dMap)
					{
						if (sync(merge)) callback.onSuccess();
						else callback.onFailed();
					}
				}
			}).start();
	}

	public boolean enableEncryption(String key)
	{
		if (key != null && key.length() > 0)
		{
			this.pKey = key;
			persist();
			return true;
		}
		else return false;
	}

	public void disableEncryption()
	{
		this.pKey = null;
		persist();
	}

	private boolean parseKVJS(JSONObject json)
	{
		try
		{
			Iterator<String> keys = json.keys();
			while (keys.hasNext())
			{
				String key = keys.next();
				String val = json.get(key).toString();
				Object k,v;
				if (this.pKey != null && this.pKey.length() > 0)
				{
					k = DataProcessor.Persistable.dePrefix(AES256.decode(this.pKey, key));
					v = DataProcessor.Persistable.dePrefix(AES256.decode(this.pKey, val));
				}
				else
				{
					k = DataProcessor.Persistable.dePrefix(key);
					v = DataProcessor.Persistable.dePrefix(val);
				}
				this.dMap.put(k, v);
			}
			return true;
		}
		catch (Exception e)
		{
			QKVLogger.ex(e);
			return false;
		}
	}

	public interface Callback
	{
		public void onSuccess();
		public void onFailed();
	}
}
