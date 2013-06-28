package edu.kit.iti.algo2.pse2013.walkaround.client.model.tile;

import android.graphics.Bitmap;


public class TimestampedBitmap implements Comparable<TimestampedBitmap> {
	private long timestamp;
	private Bitmap bitmap;

	public TimestampedBitmap(long timestamp, Bitmap bitmap) {
		super();
		this.timestamp = timestamp;
		this.bitmap = bitmap;
	}

	@Override
	public int compareTo(TimestampedBitmap another) {
		if (timestamp - another.getTimestamp() > 0) {
			return 1;
		} else if (timestamp - another.getTimestamp() < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public final long getTimestamp() {
		return timestamp;
	}

	public final Bitmap getBitmap() {
		return bitmap;
	}


}
