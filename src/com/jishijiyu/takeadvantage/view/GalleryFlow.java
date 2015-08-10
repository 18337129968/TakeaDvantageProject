package com.jishijiyu.takeadvantage.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {

	private Camera mCamera = new Camera();
	private int mMaxRotationAngle = 0;
	private int mMaxZoom = 0;
	private int mCoveflowCenter;

	@SuppressLint("NewApi")
	public GalleryFlow(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	@SuppressLint("NewApi")
	public GalleryFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	@SuppressLint("NewApi")
	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom() {
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();
		int rotationAngle = 0;

		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (childCenter == mCoveflowCenter) {
			transformImageBitmap((ImageView) child, t, 0);
		} else {
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
						: mMaxRotationAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle);
		}

		return true;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@SuppressLint("WrongCall")
	private void transformImageBitmap(ImageView child, Transformation t,
			int rotationAngle) {
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		mCamera.translate(0.0f, 0.0f, 60.0f);

		// As the angle of the view gets less, zoom in
		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
		}

		mCamera.rotateY(-rotationAngle);
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();

		if (changed) { // 保证在滑动的时候不会恢复坐标
			int[] location = new int[2];
			child.getLocationOnScreen(location);
			int x = location[0];
			int y = location[1];

			left_move_x = x - imageWidth / 4;
			left_move_y = y - (imageHeight / 8);
			left_line_x_01 = left_move_x * 2;
			left_line_y_01 = left_move_y + 20;
			left_line_x_02 = left_line_x_01;
			left_line_y_02 = left_line_y_01 + (imageHeight / 3);
			left_line_x_03 = left_move_x;
			left_line_y_03 = left_line_y_02 + 20;

			right_move_x = x + (imageWidth + imageWidth / 3);
			right_move_y = y - (imageHeight / 8);

			right_line_x_01 = right_move_x - imageWidth / 2;
			right_line_y_01 = left_move_y + 20;

			right_line_x_02 = right_line_x_01;
			right_line_y_02 = right_line_y_01 + (imageHeight / 3);

			right_line_x_03 = right_move_x;
			right_line_y_03 = right_line_y_02 + 20;

			moveLeftX = left_move_x - 10;
			moveRightX = right_move_x + 10;
			moveLineLeftX = left_move_x;
			moveLineRightX = right_move_x;
			onDraw(new Canvas());
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			eventX = event.getX();
			changed = false;
			break;
		case MotionEvent.ACTION_UP:
			changed = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getX() > eventX) { // to right
				if (left_move_x > moveLeftX) {
					--left_move_x;
					--left_line_x_03;

					--right_move_x;
					--right_line_x_03;
				}
				if (left_line_x_01 > moveLineLeftX) {
					--left_line_x_01;
					--left_line_x_02;
					--right_line_x_01;
					--right_line_x_02;
				}
			} else if (event.getX() < eventX) { // to left
				if (right_move_x < moveRightX) {
					++left_move_x;
					++left_line_x_03;

					++right_move_x;
					++right_line_x_03;
				}
				if (right_line_x_01 < moveLineRightX) {
					++left_line_x_01;
					++left_line_x_02;
					++right_line_x_01;
					++right_line_x_02;
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private int left_move_x, left_move_y, left_line_x_01, left_line_x_02,
			left_line_x_03, left_line_y_01, left_line_y_02, left_line_y_03;

	private int right_move_x, right_move_y, right_line_x_01, right_line_x_02,
			right_line_x_03, right_line_y_01, right_line_y_02, right_line_y_03;
	private float eventX;
	private int moveLeftX, moveRightX, moveLineLeftX, moveLineRightX;
	private boolean changed = true;

}
