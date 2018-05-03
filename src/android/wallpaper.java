package fc.fcstudio;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import org.apache.cordova.PluginResult;
import java.io.IOException;
import java.io.InputStream;

public class wallpaper extends CordovaPlugin
{
	private static final boolean IS_AT_LEAST_LOLLIPOP = Build.VERSION.SDK_INT >= 21;
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException
	{
		final Context context = IS_AT_LEAST_LOLLIPOP ? cordova.getActivity().getWindow().getContext() : cordova.getActivity().getApplicationContext();
	
		if (action.equals("start"))
		{
			final String imgSrc = args.getString(0);
			final Boolean base64 = args.getBoolean(1);
			cordova.getThreadPool().execute(new Runnable() {
			    public void run() {
					try
					{
						AssetManager assetManager = context.getAssets();
						Bitmap bitmap;
						if(base64) //Base64 encoded
						{
							byte[] decoded = android.util.Base64.decode(imgSrc, android.util.Base64.DEFAULT);
							bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
						}
						else //normal path
						{
							InputStream instr = assetManager.open("www/" + imgSrc);
							bitmap = BitmapFactory.decodeStream(instr);
						}
						WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
						myWallpaperManager.setBitmap(bitmap);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					callbackContext.success();
			    }
			});
			return true;
		}
		callbackContext.error("Set wallpaper is not a supported.");
		return false;
	}
}
