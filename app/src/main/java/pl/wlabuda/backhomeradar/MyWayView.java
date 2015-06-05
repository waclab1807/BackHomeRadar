package pl.wlabuda.backhomeradar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

public class MyWayView extends View {

	private float direction = 0;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint11 = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint1c = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint1z = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint1zz = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean firstDraw;

	public MyWayView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyWayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyWayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		paint3.setStyle(Paint.Style.STROKE);
		paint3.setStrokeWidth(30);
		paint3.setColor(Color.RED);
		paint3.setTextSize(30);

		paint2.setStyle(Paint.Style.STROKE);
		paint2.setStrokeWidth(3);
		paint2.setColor(Color.BLUE);
		paint2.setTextSize(30);

		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(2);
		paint.setColor(Color.BLACK);

		paint1.setStyle(Paint.Style.FILL);
		paint1.setStrokeWidth(2);
		paint1.setColor(Color.rgb(0, 102, 0));

		paint11.setStyle(Paint.Style.STROKE);
		paint11.setStrokeWidth(2);
		paint11.setColor(Color.rgb(0, 102, 0));

		paint1c.setStyle(Paint.Style.FILL);
		paint1c.setStrokeWidth(10);
		paint1c.setColor(Color.RED);

		paint1z.setStyle(Paint.Style.FILL);
		paint1z.setStrokeWidth(10);
		paint1z.setColor(Color.YELLOW);

		paint1zz.setStyle(Paint.Style.FILL);
		paint1zz.setStrokeWidth(10);
		paint1zz.setColor(Color.GREEN);

        firstDraw = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		int cxCompass = getMeasuredWidth() / 2;
		int cyCompass = getMeasuredHeight() / 2;
		float radiusCompass;

		if (cxCompass > cyCompass) {
			radiusCompass = (float) (cyCompass * 0.9);
		} else {
			radiusCompass = (float) (cxCompass * 0.9);
		}
		canvas.drawCircle(cxCompass, cyCompass, radiusCompass, paint);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.95), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.9), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.85), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.8), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.7), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.6), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float)(radiusCompass*0.4), paint11);
		canvas.drawCircle(cxCompass, cyCompass, (float) (radiusCompass * 0.2), paint11);

		if (!firstDraw) {
			switch (Global.wybor) {
			case 1:
				// strzalka w gore czerwona
				canvas.save();
				canvas.rotate(-Global.angel, cxCompass, cyCompass);
				canvas.drawLine(cxCompass, cyCompass, (float) (cxCompass),
						(float) (cyCompass - radiusCompass), paint1c);
				canvas.drawLine(cxCompass - 2, cyCompass - radiusCompass + 2,
						cxCompass + 20, cyCompass - radiusCompass + 20, paint1c);
				canvas.drawLine(cxCompass + 2, cyCompass - radiusCompass + 2,
						cxCompass - 20, cyCompass - radiusCompass + 20, paint1c);
				canvas.restore();
				break;
			case 2:
				// strzalka w gore zolta
				canvas.save();
				canvas.rotate(-Global.angel, cxCompass, cyCompass);
				canvas.drawLine(cxCompass, cyCompass, (float) (cxCompass),
						(float) (cyCompass - radiusCompass), paint1z);
				canvas.drawLine(cxCompass - 2, cyCompass - radiusCompass + 2,
						cxCompass + 20, cyCompass - radiusCompass + 20, paint1z);
				canvas.drawLine(cxCompass + 2, cyCompass - radiusCompass + 2,
						cxCompass - 20, cyCompass - radiusCompass + 20, paint1z);
				canvas.restore();
				break;
			case 3:
				// strzalka w gore zielona
				canvas.save();
				canvas.rotate(-Global.angel, cxCompass, cyCompass);
				canvas.drawLine(cxCompass, cyCompass, (float) (cxCompass),
						(float) (cyCompass - radiusCompass), paint1zz);
				canvas.drawLine(cxCompass - 2, cyCompass - radiusCompass + 2,
						cxCompass + 20, cyCompass - radiusCompass + 20,
						paint1zz);
				canvas.drawLine(cxCompass + 2, cyCompass - radiusCompass + 2,
						cxCompass - 20, cyCompass - radiusCompass + 20,
						paint1zz);
				canvas.restore();
				break;
			case 4:
				canvas.save();
				canvas.drawCircle((cxCompass), (cyCompass), 90, paint1zz);
				canvas.restore();
				break;
            case 5:
                    canvas.save();
                    //canvas.drawCircle((cxCompass), (cyCompass), 90, paint1zz);
                    canvas.restore();
                    break;
			default:

			}
			canvas.drawCircle((cxCompass), (cyCompass), 6, paint3);
		}

	}

	public void updateDirection(float dir) {
		firstDraw = false;
		direction = dir;
		invalidate();
	}
}
