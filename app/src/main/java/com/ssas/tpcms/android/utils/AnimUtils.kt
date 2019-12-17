package com.ssas.tpcms.android.utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

object AnimUtils {
     fun expand(v: View) {
        /*view.visibility = View.VISIBLE
        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthSpec, heightSpec)
        val mAnimator = slideAnimator(0, view.getMeasuredHeight(), view)
        mAnimator.start()*/

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targtetHeight = v.getMeasuredHeight()
        v.getLayoutParams().height = 0
        v.setVisibility(View.VISIBLE)
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.getLayoutParams().height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targtetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.duration = ((targtetHeight / v.getContext().getResources().getDisplayMetrics().density)).toLong()
        v.startAnimation(a)
    }

     fun collapse(v: View) {
        val initialHeight = v.getMeasuredHeight()
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.setVisibility(View.GONE)
                } else {
                    v.getLayoutParams().height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }
            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = ((initialHeight / v.getContext().getResources().getDisplayMetrics().density)).toLong()
        v.startAnimation(a)

        /*val finalHeight = view.getMeasuredHeight()
        val mAnimator = slideAnimator(finalHeight, 0, view)
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(p0: Animator?) {
                view.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })
        mAnimator.start()*/
    }

}