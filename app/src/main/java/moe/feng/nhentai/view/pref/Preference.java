package moe.feng.nhentai.view.pref;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;

import moe.feng.nhentai.R;

public class Preference extends android.preference.Preference {

	private boolean _isInitialized = false;

	protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		setLayoutResource(R.layout.custom_preference);
	}

	@TargetApi(VERSION_CODES.LOLLIPOP)
	public Preference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		if (!_isInitialized) {
			_isInitialized = true;
			init(context, attrs, defStyleAttr, defStyleRes);
		}
	}

	public Preference(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!_isInitialized) {
			_isInitialized = true;
			init(context, attrs, defStyleAttr, 0);
		}
	}

	public Preference(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!_isInitialized) {
			_isInitialized = true;
			init(context, attrs, 0, 0);
		}
	}

	public Preference(Context context) {
		super(context);
		if (!_isInitialized) {
			_isInitialized = true;
			init(context, null, 0, 0);
		}
	}

}