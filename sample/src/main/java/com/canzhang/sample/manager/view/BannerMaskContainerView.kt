package com.canzhang.sample.manager.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.base.utils.ScreenUtil

class BannerMaskContainerView : FrameLayout {


    private val mBaseDp = ScreenUtil.dip2px(context, 1f)
    private val mMaskHeight = 10 * mBaseDp

    private val mLineWidth = mBaseDp
    private val mBoldLineWidth = 3 * mBaseDp

    private val mSideLength = 18 * mBaseDp
    private val mSmallSideLength = 14 * mBaseDp


    private var mIndicatorCount = 3
    private var mIsNormalStyle = false
    private var mCoverMaskPath: Path? = null
    private var trianglePath: Path? = null
    private var totalWidth = 0f
    private var startPoint = 0f
    private var paint = Paint()

    constructor(context: Context) : super(context) {
        initViewData()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViewData()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        initViewData()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initViewData()
    }


    fun setIndicatorCount(count: Int) {
        mIndicatorCount = count
    }

    fun setNormalStyle(isNormalStyle: Boolean) {
        mIsNormalStyle = isNormalStyle
    }


    private fun initViewData() {
        totalWidth = (mIndicatorCount - 1) * ScreenUtil.dip2px(context,
            4f) +
                ScreenUtil.dip2px(context,
                    8f) +
                (mIndicatorCount) * ScreenUtil.dip2px(context,
            8f)


        paint.style = Paint.Style.STROKE
        paint.color = Color.GREEN
        paint.isAntiAlias = true
        paint.strokeWidth = mLineWidth


    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!mIsNormalStyle) {
            if (mCoverMaskPath == null && mIndicatorCount > 1) {
                //这个减去的值表示偏移量 （- mMaskHeight）
                startPoint = (width - totalWidth) / 2 - mMaskHeight

                mCoverMaskPath = Path()
                mCoverMaskPath?.apply {

                    //起点位置，截取个三角
                    moveTo(mSideLength, 0f)
                    //斜角终点
                    lineTo(0f, mSideLength)
                    //到底竖线
                    lineTo(0f, height.toFloat())
                    //横线
                    lineTo(startPoint, height.toFloat())
                    //斜线1
                    lineTo(startPoint + mMaskHeight, height - mMaskHeight)
                    //横线
                    lineTo(width - startPoint - mMaskHeight, height - mMaskHeight)
                    //斜线2
                    lineTo(width - startPoint, height.toFloat())
                    //横线
                    lineTo(width.toFloat(), height.toFloat())
                    //竖线
                    lineTo(width.toFloat(), 0f)
                    //横线
                    lineTo(0f, 0f)

                }
            }

            paint.strokeWidth = mLineWidth
            //裁剪之前绘制，不然就绘制不上去了
            canvas.drawLine(0f, 0f, mSmallSideLength, 0f, paint)
            paint.strokeWidth = mLineWidth / 2
            canvas.drawLine(mSmallSideLength, 0f, 0f, mSmallSideLength, paint)
            paint.strokeWidth = mLineWidth
            canvas.drawLine(0f, mSmallSideLength, 0f, 0f, paint)

            mCoverMaskPath?.let {
                canvas.clipPath(it)
            }



            super.dispatchDraw(canvas)

            paint.strokeWidth = mLineWidth
            //先画个基础的细线
            mCoverMaskPath?.let {
                canvas.drawPath(it, paint)
            }
            paint.strokeWidth = mBoldLineWidth
            //局部画粗线
            canvas.drawLine(mSideLength + 30*mBaseDp, 0f, mSideLength, 0f, paint)
            canvas.drawLine(mSideLength, 0f, 0f, mSideLength, paint)
            canvas.drawLine(0f, mSideLength, 0f, height.toFloat(), paint)
            canvas.drawLine(0f, mSideLength, 0f, height.toFloat(), paint)
            canvas.drawLine(0f, height.toFloat(), 30*mBaseDp, height.toFloat(), paint)
            //靠近斜线的一段粗线
            canvas.drawLine(startPoint - 30*mBaseDp,
                height.toFloat(),
                startPoint,
                height.toFloat(),
                paint)
            //斜线1
            canvas.drawLine(startPoint,
                height.toFloat(),
                startPoint + mMaskHeight,
                height - mMaskHeight,
                paint)
            //小横粗线1
            canvas.drawLine(startPoint + mMaskHeight - mBaseDp,
                height - mMaskHeight,
                startPoint + mMaskHeight + ScreenUtil.dip2px(context, 10f),
                height - mMaskHeight, paint)


            //小横粗线2
            canvas.drawLine(width - startPoint - mMaskHeight - ScreenUtil.dip2px(context, 10f),
                height - mMaskHeight,
                width - startPoint - mMaskHeight + mBaseDp,
                height - mMaskHeight, paint)


            //斜线2
            canvas.drawLine(width - startPoint - mMaskHeight,
                height - mMaskHeight,
                width - startPoint,
                height.toFloat(),
                paint)


            //小横小3
            canvas.drawLine(width - startPoint,
                height.toFloat(),
                width - startPoint + 30*mBaseDp,
                height.toFloat(),
                paint)

            //拐角前横线
            canvas.drawLine(width - 30*mBaseDp,
                height.toFloat(),
                width.toFloat(),
                height.toFloat(),
                paint)

            //竖线
            canvas.drawLine(width.toFloat(),
                height.toFloat(),
                width.toFloat(),
                0f,
                paint)

            //顶部拐角横线
            canvas.drawLine(width.toFloat(),
                0f,
                width.toFloat() - 40*mBaseDp,
                0f,
                paint)


        }


    }


}