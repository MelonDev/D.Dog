package th.ac.up.se.takingbear.Tools

import android.content.Context
import android.view.animation.*
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills

class CardAnimation(var context: Context) {

    private var animationSet :AnimationSet = AnimationSet(true)

    fun horizontalAnimation() :LayoutAnimationController{
        animationSet.apply {
            addAnimation(fadeIn(300))
            addAnimation(slideUp(300))
        }
        return LayoutAnimationController(animationSet,0.5f)
    }

    fun verticalAnimation() :LayoutAnimationController{
        animationSet.apply {
            addAnimation(fadeIn(50))
            addAnimation(slideUp(50))
        }
        return LayoutAnimationController(animationSet,0.3f)
    }

    private fun fadeIn(duration :Int) :AlphaAnimation{
        var fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = duration.toLong()
        fadeIn.fillAfter = true
        return fadeIn
    }

    private fun slideUp(duration :Int) :TranslateAnimation{
        val slideUp = TranslateAnimation(0f, 0f, DeviceUtills(this.context).getScreenHeight().toFloat(), 0f)
        slideUp.interpolator = DecelerateInterpolator(4f)
        slideUp.duration = duration.toLong()
        return slideUp
    }

    fun fastHorizontalAnimation() :LayoutAnimationController{
        animationSet.apply {
            addAnimation(fadeIn(150))
            addAnimation(slideUp(150))

        }
        return LayoutAnimationController(animationSet,0.5f)
    }

}