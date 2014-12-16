package net.mobctrl.downloadprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @author Zheng Haibo 下载进度条
 * 
 */
public class DownloadProgressView extends View {

	private int right;
	private int height;

	/** 当前进度 */
	private int progress = 30;

	/** 最大进度 */
	private int max = 100;

	/** 进度的文本颜色值 */
	private int progressTextColor = 0xff000000;

	/** 文本的大小 */
	private int textSize = 36;

	/** 内边距 */
	private int paddingRight = 10;

	private int paddingLeft = 10;

	/** 进度条的颜色 */
	private int progressColor = 0xff22ff22;

	/** 进度条颜色是否开启渐变 */
	private boolean gradientEnable = false;

	/** 进度条颜色渐变1 */
	private int progressBottomColor = 0xffccffcc;

	/** 进度条颜色渐变2 */
	private int progressTopColor = 0xff22ff22;

	/** 进度条的背景颜色 */
	private int progressBackgroundColor = 0xffebebeb;

	/** 文件的最大值 */
	private int maxFileLenght = 25;

	private int fileTextColor;

	public DownloadProgressView(Context context) {
		super(context);
		init(context, null);
	}

	public DownloadProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public DownloadProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	/**
	 * 获取配置的xml属性
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		if (null == attrs) {
			return;
		}
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.DownloadProgressView);
		this.max = typedArray.getInteger(R.styleable.DownloadProgressView_max,
				100);
		this.progress = typedArray.getInteger(
				R.styleable.DownloadProgressView_progress, 0);

		this.progressTextColor = typedArray.getColor(
				R.styleable.DownloadProgressView_progressTextColor, 0xff000000);
		this.textSize = typedArray.getDimensionPixelSize(
				R.styleable.DownloadProgressView_textSize, 36);
		this.fileTextColor = typedArray.getColor(
				R.styleable.DownloadProgressView_fileTextColor, 0xff000000);

		this.paddingRight = typedArray.getDimensionPixelSize(
				R.styleable.DownloadProgressView_paddingRight, 0);
		this.paddingLeft = typedArray.getDimensionPixelSize(
				R.styleable.DownloadProgressView_paddingLeft, 0);

		this.progressColor = typedArray.getColor(
				R.styleable.DownloadProgressView_progressColor, 0xff22ff22);
		this.progressBackgroundColor = typedArray.getColor(
				R.styleable.DownloadProgressView_progressBackgroundColor,
				0xffebebeb);

		this.gradientEnable = typedArray.getBoolean(
				R.styleable.DownloadProgressView_gradientEnable, false);
		this.progressBottomColor = typedArray.getColor(
				R.styleable.DownloadProgressView_progressBottomColor,
				0xffccffcc);
		this.progressTopColor = typedArray.getColor(
				R.styleable.DownloadProgressView_progressTopColor, 0xff22ff22);

		typedArray.recycle();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		height = getHeight();
		right = (int) ((getWidth() - height / 2 + paddingLeft) * progress / max);

		// 绘制背景
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(progressBackgroundColor);
		paint.setShadowLayer(15, 10, 10, Color.BLACK);
		canvas.drawCircle(paddingLeft + height / 2, height / 2, height / 2,
				paint);
		canvas.drawRect(new Rect(paddingLeft + height / 2, 0, getWidth()
				- height / 2, height), paint);
		canvas.drawCircle(getWidth() - height / 2, height / 2, height / 2,
				paint);

		// 绘制下载的文件大小
		paint.clearShadowLayer();
		paint.setTextSize(textSize);
		paint.setColor(progressTextColor);
		int yCenterPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint
				.ascent()) / 2));// 计算在Y
		String fileLengthStr = (progress * maxFileLenght / max) + "Mb/"
				+ maxFileLenght + "Mb";
		int startX = getWidth()
				- (int) paint.measureText(fileLengthStr, 0,
						fileLengthStr.length()) - paddingRight;
		canvas.drawText(fileLengthStr, startX, yCenterPos, paint);

		// 显示进度条
		Shader beforeShader = paint.getShader();
		if (gradientEnable) {
			Shader shader = new LinearGradient(0, 0, 0, height,
					progressBottomColor, progressTopColor,
					Shader.TileMode.MIRROR);
			paint.setShader(shader);
		} else {
			paint.setColor(progressColor);
		}
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		if (progress > 0) {
			canvas.drawCircle(paddingLeft + height / 2, height / 2, height / 2,
					paint);
			if (right > paddingLeft + height / 2) {
				canvas.drawRect(new Rect(paddingLeft + height / 2, 0, right,
						height), paint);
				canvas.drawCircle(right, height / 2, height / 2, paint);
			}
		}

		// 绘制进度的百分比文字
		paint.setShader(beforeShader);
		String progressText = (int) (progress * 100 / max) + "%";
		startX = right
				- (int) paint.measureText(progressText, 0,
						progressText.length());
		paint.setColor(fileTextColor);
		canvas.drawText(progressText, startX, yCenterPos, paint);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public void setProgress(int progress) {
		if (progress <= 0) {
			progress = 0;
		} else if (progress >= max) {
			progress = max;
		}
		this.progress = progress;
		postInvalidate();
	}

	/**
	 * 获取最大的进度
	 * 
	 * @return
	 */
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMaxFileLenght() {
		return maxFileLenght;
	}

	public void setMaxFileLenght(int maxFileLenght) {
		this.maxFileLenght = maxFileLenght;
	}

	public int getTextColor() {
		return progressTextColor;
	}

	public void setTextColor(int textColor) {
		this.progressTextColor = textColor;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getProgressColor() {
		return progressColor;
	}

	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
	}

	public boolean isGradientEnable() {
		return gradientEnable;
	}

	public void setGradientEnable(boolean gradientEnable) {
		this.gradientEnable = gradientEnable;
	}

	public int getProgressBottomColor() {
		return progressBottomColor;
	}

	public void setProgressBottomColor(int progressBottomColor) {
		this.progressBottomColor = progressBottomColor;
	}

	public int getProgressTopColor() {
		return progressTopColor;
	}

	public void setProgressTopColor(int progressTopColor) {
		this.progressTopColor = progressTopColor;
	}

	public int getProgressBackgroundColor() {
		return progressBackgroundColor;
	}

	public void setProgressBackgroundColor(int progressBackgroundColor) {
		this.progressBackgroundColor = progressBackgroundColor;
	}

	public int getProgress() {
		return progress;
	}

}
