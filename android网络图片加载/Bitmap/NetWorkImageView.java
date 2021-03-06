package com.xxy.chinesemedical.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xxy.chinesemedical.R;
import com.xxy.chinesemedical.utils.BitmapLoader.OnBitmapLoadFinishedListener;

public class NetWorkImageView extends ImageView {
	private int xRadius = 5; //默认圆角的宽高是5dip
	private int yRadius = 5; //默认圆角的宽高是5dip
	private static BitmapLoader mBitmapLoader ;
	private String lastUrl = "";
	private String alreadySetBitmapUrl = "";
	private boolean isShowRoundCorner=true;//是否显示圆角

	public NetWorkImageView(Context context) {
		super(context);
		init(context,null);
	}

	public NetWorkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public NetWorkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	private void init(Context context,AttributeSet attrs) {
		mBitmapLoader = BitmapLoader.getInstance(context);
		if(attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NetWorkImageView);
			xRadius = typedArray.getDimensionPixelSize(R.styleable.NetWorkImageView_roundWidth, xRadius);
			yRadius = typedArray.getDimensionPixelSize(R.styleable.NetWorkImageView_roundHeight, yRadius);
			isShowRoundCorner = typedArray.getBoolean(R.styleable.NetWorkImageView_isShowRoundCorner, false);
			typedArray.recycle();
		}else {
			float density = context.getResources().getDisplayMetrics().density;
			xRadius = (int) (xRadius * density);
			yRadius = (int) (yRadius * density);
			isShowRoundCorner = false;
		}
	}

	/**
	 *
	 * @param url 下载图片的地址
	 * @param defaultResId 默认的图片ID(如果下载不在图片的话就显示默认的图片)
	 * @param isForceLoad 是否强制下载
	 */
	public void loadBitmap(String url,int defaultResId,boolean isForceLoad) {
		if (TextUtils.isEmpty(url)) {
			setImageResource(defaultResId);
			return;
		}
		this.lastUrl = url;
		if(isForceLoad ? true : !lastUrl.equals(alreadySetBitmapUrl)) {
			Bitmap bitmap = mBitmapLoader.loadBitmap(lastUrl, new OnBitmapLoadFinishedListener() {
				@Override
				public void onBitmapLoadFinished(Bitmap imageBitmap,String imageUrl) {
					if(imageUrl.equals(lastUrl)) {
						setImageBitmap(imageBitmap);
						alreadySetBitmapUrl = imageUrl;
					}
				}
			});

			if (bitmap != null) {
				setImageBitmap(bitmap);
				alreadySetBitmapUrl = lastUrl;
				return;
			} else {
				setImageResource(defaultResId);
			}
		}
	}

	public void loadBitmap(String url,int defaultResId) {
		loadBitmap(url,defaultResId,false);
	}

	public static void stopLoadFromNetWork() {
		BitmapLoader.isCanLoadFromNet = false;
	}

	public static void startLoadFromNetWork() {
		BitmapLoader.isCanLoadFromNet = true;
	}

	@Override
	public void draw(Canvas canvas) {
		//显示圆角
		if(isShowRoundCorner){
			BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
			//有可能出现的情况，是因为没有设置imageResource.所以需要加判断
			if(bitmapDrawable != null){
				BitmapShader shader = new BitmapShader(bitmapDrawable.getBitmap(),Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
				//设置映射否则图片显示不全
				RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
				int width = bitmapDrawable.getBitmap().getWidth();
				int height = bitmapDrawable.getBitmap().getHeight();
				RectF src = new RectF(0.0f, 0.0f, width, height);
				Matrix matrix = new Matrix();
				matrix.setRectToRect(src, rect, Matrix.ScaleToFit.CENTER);
				shader.setLocalMatrix(matrix);
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setShader(shader);
				canvas.drawRoundRect(rect, xRadius, yRadius, paint);
			}else{
				super.draw(canvas);
			}
		}else{//不显示圆角
			super.draw(canvas);
		}
	}

}
