package com.mauriciotogneri.androidutils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Image
{
    private final Bitmap bitmap;

    public Image(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public boolean save(File file) throws IOException
    {
        if (file.createNewFile())
        {
            save(new FileOutputStream(file));

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean save(String path) throws IOException
    {
        return save(new File(path));
    }

    public boolean save(OutputStream outputStream)
    {
        return bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
    }

    public Bitmap extract(Rect section)
    {
        Bitmap cutBitmap = Bitmap.createBitmap(section.width(), section.height(), Bitmap.Config.ARGB_8888);

        int[] pixels = new int[section.width() * section.height()];

        bitmap.getPixels(pixels, 0, section.width(), section.left, section.top, section.width(), section.height());
        cutBitmap.setPixels(pixels, 0, section.width(), 0, 0, section.width(), section.height());

        return cutBitmap;
    }

    public Bitmap bitmap()
    {
        return bitmap;
    }

    public Bitmap grayscale()
    {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        Bitmap grayScale = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(grayScale);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return grayScale;
    }

    public byte[] byteArray()
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    public Image resize(int width, int height)
    {
        Bitmap result = Bitmap.createScaledBitmap(bitmap, width, height, false);

        bitmap.recycle();

        return new Image(result);
    }

    public Image rotate(float degrees)
    {
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        bitmap.recycle();

        return new Image(result);
    }

    public void recycle()
    {
        bitmap.recycle();
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

    private static Image from(BitmapDecoder decoder, int maxWidth, int maxHeight)
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