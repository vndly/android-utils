package com.mauriciotogneri.androidutils.fileprovider;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

import java.util.Arrays;

import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class LegacyCompatCursorWrapper extends CursorWrapper
{
    private final int fakeDataColumn;
    private final int fakeMimeTypeColumn;
    private final String mimeType;
    private final Uri uriForDataColumn;

    public LegacyCompatCursorWrapper(Cursor cursor)
    {
        this(cursor, null);
    }

    public LegacyCompatCursorWrapper(Cursor cursor, String mimeType)
    {
        this(cursor, mimeType, null);
    }

    public LegacyCompatCursorWrapper(Cursor cursor, String mimeType,
                                     Uri uriForDataColumn)
    {
        super(cursor);

        this.uriForDataColumn = uriForDataColumn;

        if (cursor.getColumnIndex(DATA) >= 0)
        {
            fakeDataColumn = -1;
        }
        else
        {
            fakeDataColumn = cursor.getColumnCount();
        }

        if (cursor.getColumnIndex(MIME_TYPE) >= 0)
        {
            fakeMimeTypeColumn = -1;
        }
        else if (fakeDataColumn == -1)
        {
            fakeMimeTypeColumn = cursor.getColumnCount();
        }
        else
        {
            fakeMimeTypeColumn = fakeDataColumn + 1;
        }

        this.mimeType = mimeType;
    }

    @Override
    public int getColumnCount()
    {
        int count = super.getColumnCount();

        if (!cursorHasDataColumn())
        {
            count += 1;
        }

        if (!cursorHasMimeTypeColumn())
        {
            count += 1;
        }

        return (count);
    }

    @Override
    public int getColumnIndex(String columnName)
    {
        if (!cursorHasDataColumn() && DATA.equalsIgnoreCase(
                columnName))
        {
            return (fakeDataColumn);
        }

        if (!cursorHasMimeTypeColumn() && MIME_TYPE.equalsIgnoreCase(
                columnName))
        {
            return (fakeMimeTypeColumn);
        }

        return (super.getColumnIndex(columnName));
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        if (columnIndex == fakeDataColumn)
        {
            return (DATA);
        }

        if (columnIndex == fakeMimeTypeColumn)
        {
            return (MIME_TYPE);
        }

        return (super.getColumnName(columnIndex));
    }

    @Override
    public String[] getColumnNames()
    {
        if (cursorHasDataColumn() && cursorHasMimeTypeColumn())
        {
            return (super.getColumnNames());
        }

        String[] orig = super.getColumnNames();
        String[] result = Arrays.copyOf(orig, getColumnCount());

        if (!cursorHasDataColumn())
        {
            result[fakeDataColumn] = DATA;
        }

        if (!cursorHasMimeTypeColumn())
        {
            result[fakeMimeTypeColumn] = MIME_TYPE;
        }

        return (result);
    }

    @Override
    public String getString(int columnIndex)
    {
        if (!cursorHasDataColumn() && columnIndex == fakeDataColumn)
        {
            if (uriForDataColumn != null)
            {
                return (uriForDataColumn.toString());
            }

            return (null);
        }

        if (!cursorHasMimeTypeColumn() && columnIndex == fakeMimeTypeColumn)
        {
            return (mimeType);
        }

        return (super.getString(columnIndex));
    }

    @Override
    public int getType(int columnIndex)
    {
        if (!cursorHasDataColumn() && columnIndex == fakeDataColumn)
        {
            return (Cursor.FIELD_TYPE_STRING);
        }

        if (!cursorHasMimeTypeColumn() && columnIndex == fakeMimeTypeColumn)
        {
            return (Cursor.FIELD_TYPE_STRING);
        }

        return (super.getType(columnIndex));
    }

    private boolean cursorHasDataColumn()
    {
        return (fakeDataColumn == -1);
    }

    private boolean cursorHasMimeTypeColumn()
    {
        return (fakeMimeTypeColumn == -1);
    }
}