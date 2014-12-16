package net.mobctrl.downloadprogress;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author Zheng Haibo
 * @web http://www.mobctrl.net
 *
 */
public class MainActivity extends Activity implements Runnable {

	private DownloadProgressView downloadProgressView1;
	private DownloadProgressView downloadProgressView2;
	private DownloadProgressView downloadProgressView3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		downloadProgressView1 = (DownloadProgressView) findViewById(R.id.wpv_download_1);
		downloadProgressView1.setMax(100);
		downloadProgressView1.setMaxFileLenght(20);

		downloadProgressView2 = (DownloadProgressView) findViewById(R.id.wpv_download_2);
		downloadProgressView2.setMax(100);
		downloadProgressView2.setMaxFileLenght(8);
		
		downloadProgressView3 = (DownloadProgressView) findViewById(R.id.wpv_download_3);
		downloadProgressView3.setMax(100);
		downloadProgressView3.setMaxFileLenght(19);

		new Thread(this).start();
	}

	@Override
	public void run() {

		for (int i = 0; i <= 100; i++) {
			downloadProgressView1.setProgress(i);
			downloadProgressView2.setProgress(i);
			downloadProgressView3.setProgress(i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
