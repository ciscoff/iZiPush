package s.yarlykov.izipush

import android.view.Gravity
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.DrawableRes
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import s.yarlykov.pushsdk.utils.dpToInt

class FabAnimationHelper(
    private val fab: ExtendedFloatingActionButton,
    @DrawableRes private val drawableNormal: Int,
    @DrawableRes private val drawableShrunken: Int
) {

    companion object {
        private const val ICON_PADDING = 16f
        private const val MULTIPLIER = 5
        private const val DURATION = 240L * MULTIPLIER
        private const val ANGLE = 360f * MULTIPLIER
    }

    lateinit var animator: ViewPropertyAnimator

    private var isContinue = false

    private val onChangedCallback = object : ExtendedFloatingActionButton.OnChangedCallback() {
        override fun onExtended(extendedFab: ExtendedFloatingActionButton) {
            extendedFab.apply {
                iconPadding = context.dpToInt(ICON_PADDING)
                setIconResource(drawableNormal)
                gravity = Gravity.CENTER
            }
        }

        override fun onShrunken(extendedFab: ExtendedFloatingActionButton) {
            extendedFab.apply {
                iconPadding = 0
                setIconResource(drawableShrunken)
            }
            rotateShrunken()
        }
    }

    /**
     * Сжать и запустить анимацию прогресса
     */
    fun shrinkAndRotate() {
        isContinue = true
        fab.shrink(onChangedCallback)
    }

    fun cancel() {
        isContinue = false
    }

    fun cancelImmediately() {
        isContinue = false
        resetAnimation()
        restore()
    }

    /**
     * Остановить анимацию прогресса и вернуться в состояние Extended
     */
    private fun restore() {
        fab.extend(onChangedCallback)
    }

    private fun rotateShrunken() {
        resetAnimation()

        if (isContinue) {
            animator = fab.animate()
                .rotation(ANGLE)
                .setDuration(DURATION)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction(::rotateShrunken)
        } else {
            restore()
        }
    }

    private fun resetAnimation() {
        if (::animator.isInitialized) {
            animator.cancel()
            fab.rotation = 0f
        }
    }
}