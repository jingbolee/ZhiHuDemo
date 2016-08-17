package cc.lijingbo.zhihudemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.text.format.Formatter;

import com.socks.library.KLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import libcore.io.DiskLruCache;

public class CacheUtils {
  private static final String TAG = "CacheUtils";
  Context mContext;
  static volatile CacheUtils INSTANCE;
  int cacheSize = 50 * 1024 * 1024;  //50MB 缓存
  static final int DISK_CACHE_INDEX = 0;
  DiskLruCache mDiskCache;
  LruCache<String, String> mMemoryCache;

  CacheUtils(Context context) {
    mContext = context;
    int maxMemory = (int) (Runtime.getRuntime().maxMemory());
    int cacheMemory = maxMemory / 8;
    mMemoryCache = new LruCache<String, String>(cacheMemory) {
      @Override protected int sizeOf(String key, String value) {
        return super.sizeOf(key, value);
      }
    };
    //if (getSDAvaliableSize(mContext)<= cacheSize
    //    && getRomAvaliableSize(mContext) <= cacheSize) {
    //  throw new IllegalArgumentException("没有足够的sd卡空间或者手机空间用于缓存");
    //}
    File file = getDiskCacheDir(mContext, "webInfo");
    if (!file.exists()) {
      file.mkdirs();
    }
    try {
      mDiskCache = DiskLruCache.open(file, getAppVersion(mContext), 1, cacheSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static CacheUtils build(Context context) {
    if (INSTANCE == null) {
      synchronized (CacheUtils.class) {
        if (INSTANCE == null) {
          INSTANCE = new CacheUtils(context);
        }
      }
    }
    return INSTANCE;
  }

  private File getDiskCacheDir(Context context, String fileName) {
    String path;
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      path = context.getExternalCacheDir().getAbsolutePath();
    } else {
      path = context.getCacheDir().getAbsolutePath();
    }
    return new File(path + File.separator + fileName);
  }

  private int getAppVersion(Context context) {
    int version = 1;
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      version = packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return version;
  }

  private String getSDTotalSize(Context context) {
    File storageDirectory = Environment.getExternalStorageDirectory();
    StatFs statFs = new StatFs(storageDirectory.getPath());
    int blockSize = statFs.getBlockSize();
    int blockCount = statFs.getBlockCount();
    return Formatter.formatFileSize(context, blockSize * blockCount);
  }

  private int getSDAvaliableSize(Context context) {
    File file = Environment.getExternalStorageDirectory();
    StatFs statFs = new StatFs(file.getPath());
    return statFs.getAvailableBlocks() * statFs.getBlockSize();
  }

  private int getRomAvaliableSize(Context context) {
    File file = Environment.getDataDirectory();
    StatFs statFs = new StatFs(file.getPath());
    return statFs.getAvailableBlocks() * statFs.getBlockSize();
  }

  public String loadData(String key) {
    String data = loadDataFromMemCache(key);
    if (data != null) {
      KLog.e("loadDataFromMemCache,id:" + key);
      return data;
    }
    try {
      data = loadDataFromDiskCache(key);
      if (data != null) {
        return data;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  /**
   * 从内存缓存中加载数据
   */
  public String loadDataFromMemCache(String key) {
    return mMemoryCache.get(key);
  }

  public void addDataToMemCache(String key, String data) {
    if (getDataFromMemCache(key) == null) {
      mMemoryCache.put(key, data);
    }
  }

  public String getDataFromMemCache(String key) {
    return mMemoryCache.get(key);
  }

  public String loadDataFromDiskCache(String key) throws IOException {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      KLog.e("不能在UI线程执行耗时操作");
    }
    if (mDiskCache == null) {
      return null;
    }
    String data = null;
    DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
    if (snapshot != null) {
      InputStream inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
      data = stream2String(inputStream);
      if (data != null) {
        addDataToMemCache(key, data);
      }
    }
    return data;
  }

  public String stream2String(InputStream inputStream) throws IOException {
    StringBuffer buffer = new StringBuffer();
    byte[] b = new byte[1024];
    int n;
    try {
      while ((n = inputStream.read(b)) != -1) {
        buffer.append(new String(b, 0, n));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      inputStream.close();
    }
    return buffer.toString();
  }

  public InputStream string2Stream(String data) throws UnsupportedEncodingException {
    return new ByteArrayInputStream(data.getBytes());
  }

  public String addData(String key, String data) throws IOException {
    DiskLruCache.Editor edit = mDiskCache.edit(key);
    if (edit != null) {
      try {
        BufferedOutputStream bufferedOutputStream =
            new BufferedOutputStream(edit.newOutputStream(DISK_CACHE_INDEX));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(string2Stream(data));
        int b;
        while ((b = bufferedInputStream.read()) != -1) {
          bufferedOutputStream.write(b);
        }
        bufferedOutputStream.flush();
        edit.commit();
      } catch (IOException e) {
        e.printStackTrace();
        edit.abort();
      } finally {
        mDiskCache.flush();
      }
    }
    return loadDataFromDiskCache(key);
  }

}
