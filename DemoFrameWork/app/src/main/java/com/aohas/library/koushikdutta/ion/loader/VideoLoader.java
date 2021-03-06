package com.aohas.library.koushikdutta.ion.loader;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;

import com.aohas.library.koushikdutta.async.future.Future;
import com.aohas.library.koushikdutta.async.future.SimpleFuture;
import com.aohas.library.koushikdutta.ion.Ion;
import com.aohas.library.koushikdutta.ion.bitmap.BitmapInfo;

import java.io.File;
import java.net.URI;

/**
 * Created by koush on 11/6/13.
 */
public class VideoLoader extends SimpleLoader {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public static Bitmap createVideoThumbnail(String filePath) throws Exception {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);
        return retriever.getFrameAtTime();
    }

    static boolean mustUseThumbnailUtils() {
        // http://developer.samsung.com/forum/thread/mediametadataretriever-getframeattime-to-retrieve-video-frame-fails/77/202945
        // https://codereview.chromium.org/107523005
        return Build.MANUFACTURER.toLowerCase().contains("samsung");
    }

    @Override
    public Future<BitmapInfo> loadBitmap(Context context, Ion ion, final String key, final String uri, int resizeWidth, int resizeHeight, boolean animateGif) {
        if (!uri.startsWith(ContentResolver.SCHEME_FILE))
            return null;

        final MediaFile.MediaFileType type = MediaFile.getFileType(uri);
        if (type == null || !MediaFile.isVideoFileType(type.fileType))
            return null;

        final SimpleFuture<BitmapInfo> ret = new SimpleFuture<BitmapInfo>();
        Ion.getBitmapLoadExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                final File file = new File(URI.create(uri));
                if (ret.isCancelled()) {
//                    Log.d("VideoLoader", "Bitmap load cancelled (no longer needed)");
                    return;
                }
                try {
                    Bitmap bmp;

                    if (mustUseThumbnailUtils() || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1)
                        bmp = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                    else
                        bmp = createVideoThumbnail(file.getAbsolutePath());
                    if (bmp == null)
                        throw new Exception("video bitmap failed to load");
                    BitmapInfo info = new BitmapInfo(key, type.mimeType, new Bitmap[] { bmp }, new Point(bmp.getWidth(), bmp.getHeight()));
                    info.loadedFrom = LoaderEmitter.LOADED_FROM_CACHE;
                    ret.setComplete(info);
                } catch (Exception e) {
                    ret.setComplete(e);
                }
            }
        });
        return ret;
    }
}
