package s.yarlykov.izipush

import android.view.Gravity
import androidx.annotation.DrawableRes
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import s.yarlykov.pushsdk.utils.dpToInt

class FabAnimationHelper(
    private val fab: ExtendedFloatingActionButton,
    @DrawableRes private val drawableNormal: Int,
    @DrawableRes private val drawableShrunken: Int
) {

    companion object {
        const val ICON_PADDING = 16f
    }

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

    /**
     * Остановить анимацию прогресса и вернуться в состояние Extended
     */
    private fun restore() {
        fab.extend(onChangedCallback)
    }

    private fun rotateShrunken() {
        resetAnimation()

        if(isContinue) {
            fab.animate()
                .rotation(360f)
                .setDuration(240)
                .withEndAction(::rotateShrunken)
                .start()
        } else {
            restore()
        }
    }

    private fun resetAnimation() {
        fab.animation?.apply{
            cancel()
            reset()
        }
        fab.rotation = 0f
    }
}