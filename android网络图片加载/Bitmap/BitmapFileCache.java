package com.xxy.chinesemedical.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public  class BitmapFileCache {

	/**
	 * SD卡所需剩余空间
	 */
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static final int MB = 1024 * 1024;
	private static final int CACHE_SIZE = 10;

	private static BitmapFileCache mBitmapFileCache = null;
	private BufferedOutputStream bos;

	public static BitmapFileCache getInstance() {
		if(null == mBitmapFileCache) {
			mBitmapFileCache = new BitmapFileCache();
		}
		return mBitmapFileCache;
	}

	private BitmapFileCache() {
		// 清理文件缓存
		removeCache(getFileCacheDirectory());
	}


	/**
	 * 清理缓存
	 *
	 * 计算存储目录下的文件大小，
	 * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	 * 那么删除40%最近没有被使用的文件
	 *
	 * @param dirPath
	 * @param filename
	 */
	private boolean removeCache(String dirPath) {
		final File dir = new File(dirPath);
		final File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}

		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}

		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(Constant.WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}

		if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			final int removeFactor = (int) ((0.4 * files.length) + 1);

			Arrays.sort(files, new FileLastModifSort());

			Logger.i("清理缓存文件");

			for (int i = 0; i < removeFactor; i++) {

				if (files[i].getName().contains(Constant.WHOLESALE_CONV)) {
					files[i].delete();
				}
			}
		}

		if (freeSpaceOnSd() <= CACHE_SIZE) {
			return false;
		}
		return true;
	}

	/**
	 * 根据文件的最后修改时间进行排序
	 */
	private class FileLastModifSort implements Comparator<File> {
		@Override
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 获取图片
	 *
	 * @param url
	 * @return
	 */
	public Bitmap getImage(final String url) {

		final String path = getFileCacheDirectory() + "/" + convertUrlToFileName(url) + Constant.WHOLESALE_CONV;
		final File file = new File(path);
		Bitmap bmp = null;
		if (file.exists()) {
			try {
				bmp = BitmapFactory.decodeFile(path);
				if (bmp == null) {
					file.delete();
				} else {
					updateFileTime(path);
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return bmp;
	}

	/**
	 * 获得缓存目录
	 *
	 * @return
	 */
	private String getFileCacheDirectory() {
		File sdcardDir = Environment.getExternalStorageDirectory();
		String fileDirectory = "/mnt/sdcard";
		if(null != sdcardDir) {
			fileDirectory = sdcardDir.getAbsolutePath() + "/" + Constant.IMAGE_CACHE_DIR;
		}
		return fileDirectory;
	}


	/**
	 * 存储图片到本地
	 * @param url
	 * @param inputStream
	 * @return
	 */
	public void saveToLocal(String url,Bitmap bitmap) {

		// 1.判断是否存在sdcard
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			String fileDirectory = getFileCacheDirectory();

			Util.log("fileDirectory === " + fileDirectory);

			File dir = new File(fileDirectory);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			Util.log("path.exists() === " + dir.exists());

			final String fileName = convertUrlToFileName(url);
			File file = new File(fileDirectory + "/" + fileName + Constant.WHOLESALE_CONV);
			if (!file.exists()) {
				try {
					file.createNewFile();
					bos = new BufferedOutputStream(new FileOutputStream(file));
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					bos.flush();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					file.delete();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					file.delete();
				} finally {
					try {
						if(bos != null){
							bos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 将url转成文件名
	 *
	 * @param url
	 * @return
	 */
	private String convertUrlToFileName(String url) {
		String fileName = "";
		try {
			fileName = Util.MD5(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 修改文件的最后修改时间 
	 *
	 * @param path
	 */
	public void updateFileTime(String path) {
		final File file = new File(path);
		final long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}


	/**
	 * 保存图片到SD卡
	 * @param bm
	 * @param url
	 */
	public void saveInputStreamToLocal(final Bitmap bitmap,final String url) {
		if (bitmap == null || null == url || "".equals(url) ) {
			return;
		}
		// SD卡少于10MB就不存图片
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return;
		}
		new Thread(){

			@Override
			public void run() {
				saveToLocal(url, bitmap);
			}
		}.start();

	}

	/**
	 * 计算sdcard上的剩余空间
	 *
	 * @return
	 */
	private int freeSpaceOnSd() {
		final StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		final double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize())/ MB;
		return (int) sdFreeMB;
	}

	/**
	 * 删除缓存的文件
	 * @param url
	 * @return
	 */
	public boolean deleteImage(final String url) {
		final String path = getFileCacheDirectory() + "/" + convertUrlToFileName(url) + Constant.WHOLESALE_CONV;
		final File file = new File(path);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
}
