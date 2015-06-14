/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.8.2
 */

package sumimakito.android.quickkv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataProcessor
{
	public static class Persistable
	{
		public static Object dePrefix(String k) throws JSONException
		{
				if (k.startsWith("String_"))
				{
					return k.substring("String_".length());
				}
				else if (k.startsWith("Boolean_"))
				{
					return Boolean.parseBoolean(k.substring("Boolean_".length()));
				}
				else if (k.startsWith("Integer_"))
				{
					return Integer.parseInt(k.substring("Integer_".length()));
				}
				else if (k.startsWith("Float_"))
				{
					return Float.parseFloat(k.substring("Float_".length()));
				}
				else if (k.startsWith("Double_"))
				{
					return Double.parseDouble(k.substring("Double_".length()));
				}
				else if (k.startsWith("Long_"))
				{
					return Long.parseLong(k.substring("Long_".length()));
				}
				else if (k.startsWith("JSONArray_"))
				{
					return new JSONArray(k.substring("JSONArray_".length()));
				}
				else if (k.startsWith("JSONObject_"))
				{
					return new JSONObject(k.substring("JSONObject_".length()));
				}
				else
				{
					return null;
				}
		}

		public static boolean isValidDataType(Object obj)
		{
			if (obj instanceof String
				|| obj instanceof Integer
				|| obj instanceof Boolean
				|| obj instanceof Long
				|| obj instanceof Float
				|| obj instanceof Double
				|| obj instanceof JSONObject
				|| obj instanceof JSONArray)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		public static String addPrefix(Object obj)
		{
			if (obj instanceof String)
			{
				return "String_" + obj.toString();
			}
			else if (obj instanceof Integer)
			{
				return "Integer_" + obj.toString();
			}
			else if (obj instanceof Boolean)
			{
				return "Boolean_" + obj.toString();
			}
			else if (obj instanceof Long)
			{
				return "Long_" + obj.toString();
			}
			else if (obj instanceof Float)
			{
				return "Float_" + obj.toString();
			}
			else if (obj instanceof Double)
			{
				return "Double_" + obj.toString();
			}
			else if (obj instanceof org.json.JSONObject)
			{
				return "JSONObject_" + obj.toString();
			}
			else if (obj instanceof org.json.JSONArray)
			{
				return "JSONArray_" + obj.toString();
			}
			else
			{
				return obj.toString();
			}
		}
	} 
}
