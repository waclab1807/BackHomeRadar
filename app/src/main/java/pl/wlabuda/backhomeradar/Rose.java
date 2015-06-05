package pl.wlabuda.backhomeradar;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

public class Rose extends ImageView {
	int direction = 0;
	int pitch = 0;
	int roll = 0;

	public Rose(Context context) {
		super(context);
		//this.setBackgroundResource(R.drawable.tlo);
		this.setImageResource(R.drawable.kompas2);
	}

	@Override
	public void onDraw(Canvas canvas) {
		int height = this.getHeight();
		int width = this.getWidth();
		canvas.translate(-pitch, roll);
		canvas.rotate(-direction, width / 2, height / 2);
		super.onDraw(canvas);
	}

	public void setDirection(int direction) {
		this.direction = direction;
		this.invalidate();
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
		this.invalidate();
		// TODO Auto-generated method stub

	}

	public void setRoll(int roll) {
		this.roll = roll;
		this.invalidate();
		// TODO Auto-generated method stub

	}

}
