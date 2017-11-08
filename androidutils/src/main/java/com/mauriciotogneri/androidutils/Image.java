package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.IdRes;

import com.mauriciotogneri.javautils.Record;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Image
{
    private final Bitmap bitmap;

    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
    private static final int DEFAULT_QUALITY = 100;

    public Image(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Bitmap bitmap()
    {
        return bitmap;
    }

    public void recycle()
    {
        bitmap.recycle();
    }

    public int width()
    {
        return bitmap.getWidth();
    }

    public int height()
    {
        return bitmap.getHeight();
    }

    public boolean save(File file, CompressFormat compressFormat, int quality) throws IOException
    {
        return new Record(file).createFile() && write(new FileOutputStream(file), compressFormat, quality);
    }

    public boolean save(File file, CompressFormat compressFormat) throws IOException
    {
        return save(file, compressFormat, DEFAULT_QUALITY);
    }

    public boolean save(File file, int quality) throws IOException
    {
        return save(file, DEFAULT_COMPRESS_FORMAT, quality);
    }

    public boolean save(File file) throws IOException
    {
        return save(file, DEFAULT_COMPRESS_FORMAT, DEFAULT_QUALITY);
    }

    public boolean save(String path, CompressFormat compressFormat, int quality) throws IOException
    {
        return save(new File(path), compressFormat, quality);
    }

    public boolean save(String path, CompressFormat compressFormat) throws IOException
    {
        return save(path, compressFormat, DEFAULT_QUALITY);
    }

    public boolean save(String path, int quality) throws IOException
    {
        return save(path, DEFAULT_COMPRESS_FORMAT, quality);
    }

    public boolean save(String path) throws IOException
    {
        return save(path, DEFAULT_COMPRESS_FORMAT, DEFAULT_QUALITY);
    }

    public boolean write(OutputStream outputStream, CompressFormat compressFormat, int quality)
    {
        return bitmap.compress(compressFormat, quality, outputStream);
    }

    public boolean write(OutputStream outputStream, CompressFormat compressFormat)
    {
        return write(outputStream, compressFormat, DEFAULT_QUALITY);
    }

    public boolean write(OutputStream outputStream, int quality)
    {
        return write(outputStream, DEFAULT_COMPRESS_FORMAT, quality);
    }

    public boolean write(OutputStream outputStream)
    {
        return write(outputStream, DEFAULT_COMPRESS_FORMAT, DEFAULT_QUALITY);
    }

    public Image extract(Rect section)
    {
        Bitmap result = Bitmap.createBitmap(section.width(), section.height(), bitmap.getConfig());

        int[] pixels = new int[section.width() * section.height()];

        bitmap.getPixels(pixels, 0, section.width(), section.left, section.top, section.width(), section.height());
        result.setPixels(pixels, 0, section.width(), 0, 0, section.width(), section.height());

        return new Image(result);
    }

    public Image grayscale()
    {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return new Image(result);
    }

    public byte[] byteArray(CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        write(stream, compressFormat, quality);

        return stream.toByteArray();
    }

    public byte[] byteArray(CompressFormat compressFormat)
    {
        return byteArray(compressFormat, DEFAULT_QUALITY);
    }

    public byte[] byteArray(int quality)
    {
        return byteArray(DEFAULT_COMPRESS_FORMAT, quality);
    }

    public byte[] byteArray()
    {
        return byteArray(DEFAULT_COMPRESS_FORMAT, DEFAULT_QUALITY);
    }

    public Image resize(int width, int height)
    {
        return new Image(Bitmap.createScaledBitmap(bitmap, width, height, false));
    }

    public Image rotate(float degrees)
    {
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        return new Image(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false));
    }

    // ============================================================================================

    public static Image fromFile(File file)
    {
        return new Image(BitmapFactory.decodeFile(file.getAbsolutePath()));
    }

    public static Image fromFile(final File file, int maxWidth, int maxHeight)
    {
        return from(new BitmapDecoder()
        {
            @Override
            public Bitmap decode(Options options)
            {
                return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            }
        }, maxWidth, maxHeight);
    }

    // ============================================================================================

    public static Image fromByteArray(byte[] data)
    {
        return new Image(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    public static Image fromByteArray(final byte[] data, int maxWidth, int maxHeight)
    {
        return from(new BitmapDecoder()
        {
            @Override
            public Bitmap decode(Options options)
            {
                return BitmapFactory.decodeByteArray(data, 0, data.length, options);
            }
        }, maxWidth, maxHeight);
    }

    // ============================================================================================

    public static Image fromResource(Resources resources, @IdRes int resourceId)
    {
        return new Image(BitmapFactory.decodeResource(resources, resourceId));
    }

    public static Image fromResource(final Resources resources, final @IdRes int resourceId, int maxWidth, int maxHeight)
    {
        return from(new BitmapDecoder()
        {
            @Override
            public Bitmap decode(Options options)
            {
                return BitmapFactory.decodeResource(resources, resourceId, options);
            }
        }, maxWidth, maxHeight);
    }

    // ============================================================================================

    public static Image fromInputStream(InputStream stream)
    {
        return new Image(BitmapFactory.decodeStream(stream));
    }

    public static Image fromInputStream(final InputStream stream, int maxWidth, int maxHeight)
    {
        return from(new BitmapDecoder()
        {
            @Override
            public Bitmap decode(Options options)
            {
                return BitmapFactory.decodeStream(stream, null, options);
            }
        }, maxWidth, maxHeight);
    }

    // ============================================================================================

    public static Image fromUri(Context context, Uri uri) throws IOException
    {
        return fromInputStream(context.getContentResolver().openInputStream(uri));
    }

    public static Image fromUri(Context context, Uri uri, int maxWidth, int maxHeight) throws IOException
    {
        return fromInputStream(context.getContentResolver().openInputStream(uri), maxWidth, maxHeight);
    }

    // ============================================================================================

    public static Image from(BitmapDecoder decoder, int maxWidth, int maxHeight)
    {
        // decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decoder.decode(options);

        // calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);

        // decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return new Image(decoder.decode(options));
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight)
    {
        // raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > maxHeight || width > maxWidth)
        {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) > maxHeight && (halfWidth / inSampleSize) > maxWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public interface BitmapDecoder
    {
        Bitmap decode(BitmapFactory.Options options);
    }
}