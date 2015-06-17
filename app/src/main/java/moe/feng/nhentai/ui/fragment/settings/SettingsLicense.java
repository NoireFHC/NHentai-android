package moe.feng.nhentai.ui.fragment.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import moe.feng.nhentai.R;

public class SettingsLicense extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
		WebView webView = new WebView(inflater.getContext());
		webView.loadUrl("file:///android_asset/licenses.html");

		getActivity().setTitle(R.string.settings_license);

		return webView;
	}

}
