package moe.feng.nhentai.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("InlinedApi")
public final class FullScreenHelper implements View.OnSystemUiVisibilityChangeListener {

	private static final String TAG = FullScreenHelper.class.getSimpleName();

	private final int NOT_FULL_SCREEN_JELLY_BEAN =
			View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

	private final int FULL_SCREEN_JELLY_BEAN =
			NOT_FULL_SCREEN_JELLY_BEAN |
					View.SYSTEM_UI_FLAG_LOW_PROFILE |
					View.SYSTEM_UI_FLAG_FULLSCREEN;

	private final int FULL_SCREEN_FLAG_JELLY_BEAN =
			View.SYSTEM_UI_FLAG_LOW_PROFILE |
					View.SYSTEM_UI_FLAG_FULLSCREEN;

	private final int NOT_FULL_SCREEN_KITKAT =
			NOT_FULL_SCREEN_JELLY_BEAN |
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

	private final int FULL_SCREEN_KITKAT =
			NOT_FULL_SCREEN_KITKAT |
					View.SYSTEM_UI_FLAG_LOW_PROFILE |
					View.SYSTEM_UI_FLAG_FULLSCREEN |
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

	private boolean mFullScreen;
	private final Activity mActivity;
	private final Window mWindow;
	private final View mDecorView;

	private boolean mHasNavBar;

	private OnFullScreenBrokenListener mListener;

	private final int TEST_SDK = Build.VERSION.SDK_INT;

	public interface OnFullScreenBrokenListener {

		/**
		 * FullScreen state should be fullScreen or not,
		 * but user or system broke it
		 *
		 * @param fullScreen support to be
		 */
		public void onFullScreenBroken(boolean fullScreen);

	}

	public FullScreenHelper(Activity activity) {
		mFullScreen = false;
		mActivity = activity;
		mWindow = mActivity.getWindow();
		mDecorView = mWindow.getDecorView();
		mDecorView.setOnSystemUiVisibilityChangeListener(this);

		String mainKey = Utility.getSystemProperties("qemu.hw.mainkeys");
		int resourceId = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
		if (resourceId != 0) {
			mHasNavBar = activity.getResources().getBoolean(resourceId);
			// check override flag (see static block)
			if ("1".equals(mainKey)) {
				mHasNavBar = false;
			} else if ("0".equals(mainKey)) {
				mHasNavBar = true;
			}
		} else {
			mHasNavBar = !ViewConfiguration.get(activity).hasPermanentMenuKey();
		}
	}

	public boolean willHideNavBar() {
		if (TEST_SDK >= Build.VERSION_CODES.KITKAT && mHasNavBar)
			return true;
		else
			return false;
	}

	@Override
	public void onSystemUiVisibilityChange(int visibility) {
		if (TEST_SDK >= Build.VERSION_CODES.JELLY_BEAN &&
				TEST_SDK < Build.VERSION_CODES.KITKAT) {
			if ((mFullScreen && visibility != FULL_SCREEN_FLAG_JELLY_BEAN) ||
					(!mFullScreen && visibility != 0)) {
				// User or system change visibility
				if (mListener != null)
					mListener.onFullScreenBroken(mFullScreen);
			}
		}
	}

	public void setOnFullScreenBrokenListener(OnFullScreenBrokenListener l) {
		mListener = l;
	}

	public boolean getFullScreen() {
		return mFullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		mFullScreen = fullScreen;
		if (fullScreen) {
			if (TEST_SDK < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				// Empty
			} else if (TEST_SDK < Build.VERSION_CODES.JELLY_BEAN) {
				mWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);

			} else if (TEST_SDK < Build.VERSION_CODES.KITKAT) {
				mDecorView.setSystemUiVisibility(FULL_SCREEN_JELLY_BEAN);

			} else {
				mDecorView.setSystemUiVisibility(FULL_SCREEN_KITKAT);
			}
		} else {
			if (TEST_SDK < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				// Empty
			} else if (TEST_SDK < Build.VERSION_CODES.JELLY_BEAN) {
				mWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			} else if (TEST_SDK < Build.VERSION_CODES.KITKAT) {
				mDecorView.setSystemUiVisibility(NOT_FULL_SCREEN_JELLY_BEAN);

			} else {
				mDecorView.setSystemUiVisibility(NOT_FULL_SCREEN_KITKAT);
			}
		}
	}

}