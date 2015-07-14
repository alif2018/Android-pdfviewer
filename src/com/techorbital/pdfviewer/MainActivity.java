package com.techorbital.pdfviewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;



public class MainActivity extends Activity{
	private WebView webView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webView = (WebView)findViewById(R.id.webView1);
		
		   final String interalPDFName="sample.pdf";
		 
		
		    WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);
			getInternalPDFURL(interalPDFName);
			
			settings.setAllowFileAccessFromFileURLs(true);
			settings.setAllowUniversalAccessFromFileURLs(true);
			settings.setBuiltInZoomControls(true);
			webView.setWebChromeClient(new WebChromeClient());
			//webView.loadUrl("file:///android_asset/pdfviewer/index.html");
			try{
			webView.loadUrl("file:///android_asset/pdfviewer/index.html?pdf=" + interalPDFName);
			}catch(Exception e)
			{
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "PDf Reader Doesn't work properly!", Toast.LENGTH_LONG).show();
			}
		   
		
		finally{
			CopyReadAssets(interalPDFName); // pdf view using asset manager 
			
		}
		
	
	}

	public String getInternalPDFURL(String interalPDFName){
		    return "file:///android_asset/pdfviewer/index.html?pdf=" + interalPDFName + ".pdf";
		 }

//	reload on resume
@Override
protected void onResume() {
	super.onResume();
	webView.loadUrl( "javascript:window.location.reload( true )" );

}

//clear cache to ensure we have good reload
@Override
protected void onPause() {
	super.onPause();
	webView.clearCache(true);

}

private void CopyReadAssets(String pdfname)
{
    AssetManager assetManager = getAssets();
    InputStream in = null;
    OutputStream out = null;
    File file = new File(getFilesDir(), pdfname);
    try
    {
        in = assetManager.open(pdfname);
        out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
        copyFile(in, out);
        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    } catch (Exception e)
    {
        Toast.makeText(getApplicationContext(), "Pdf Viewer not installed", Toast.LENGTH_SHORT).show();
    }
    try
    {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(
            Uri.parse("file://" + getFilesDir() + "/"+pdfname),
            "application/pdf");

    startActivity(intent);
    }catch (Exception e) {
        // TODO: handle exception
        Toast.makeText(getApplicationContext(), "Pdf Viewer not installed" ,Toast.LENGTH_SHORT).show();
    }
}

private void copyFile(InputStream in, OutputStream out) throws IOException
{
    byte[] buffer = new byte[1024];
    int read;
    while ((read = in.read(buffer)) != -1)
    {
        out.write(buffer, 0, read);
    }

}

}