package com.bignerdranch.android.sokoban

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bignerdranch.android.sokoban.controls.OnSwipeTouchListener
import com.bignerdranch.android.sokoban.logic.HighScore
import com.bignerdranch.android.sokoban.logic.Pos
import com.bignerdranch.android.sokoban.logic.Sprite
import com.bignerdranch.android.sokoban.level.Level
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

class GameView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private var size = min(width, height) / 10
    private val dimension = Rect(0, 0, size, size)
    private val textPaint = Paint().apply { color = Color.BLACK }
    private var screenDelta = Pos(0, 0)
    private var winMess: Dialog? = null
    private lateinit var gameModel: GameModel

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val fragment = findFragment<Fragment>()
        gameModel = ViewModelProvider(fragment).get(GameModel::class.java)
        gameModel.levelLive.observe(
            fragment,
            Observer { level -> updateLevel(level) }
        )
        updateLevel(gameModel.level())
        gameModel.wonLive.observe(
            fragment,
            Observer {
                if (it == true) {
                    fragment.lifecycleScope.launch(Dispatchers.IO) {
                        withContext(Dispatchers.Main) {
                            showWinDialog()
                        }
                    }
                    gameModel.sendHighScore()
                }
            }
        )
        setOnTouchListener(OnSwipeTouchListener(context, gameModel))
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent) = when (keyCode) {
        KeyEvent.KEYCODE_DPAD_RIGHT -> {
            gameModel.onMoveRight()
            true
        }
        KeyEvent.KEYCODE_DPAD_LEFT -> {
            gameModel.onMoveLeft()
            true
        }
        KeyEvent.KEYCODE_DPAD_DOWN -> {
            gameModel.onMoveDown()
            true
        }
        KeyEvent.KEYCODE_DPAD_UP -> {
            gameModel.onMoveUp()
            true
        }
        KeyEvent.KEYCODE_R -> {
            gameModel.onResetLevel()
            true
        }
        KeyEvent.KEYCODE_Z -> {
            gameModel.onUndoMove()
            true
        }
        else -> super.onKeyUp(keyCode, event)
    }

    private fun updateLevel(level: Level?) {
        if (level == null) return

        size = min(width / level.width, height / level.height())
        textPaint.textSize = size.toFloat()
        screenDelta = Pos(
            (height - level.height() * size) / 2,
            (width - level.width * size) / 2
        )
        invalidate()
    }

    private fun setDrawingDimension(pos: Pos) {
        val (y, x) = pos
        dimension.set(
            screenDelta.x + x * size, screenDelta.y + y * size,
            screenDelta.x + (x + 1) * size, screenDelta.y + (y + 1) * size
        )
    }

    private fun drawHero(canvas: Canvas) {
        setDrawingDimension(gameModel.player())
        val bitmap = gameModel.lastMove()?.heroTexture() ?: Textures.playerD
        canvas.drawBitmap(bitmap, null, dimension, null)
    }

    private fun drawTiles(canvas: Canvas) {
        val level = gameModel.level()
        for (pos in level.positions()) {
            setDrawingDimension(pos)
            if (!Sprite.isGrass(level.tile(pos))) {
                canvas.drawBitmap(level.texture(pos), null, dimension, null)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!::gameModel.isInitialized) return
        drawTiles(canvas)
        drawHero(canvas)
        updateLevel(gameModel.level())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        winMess?.dismiss()
    }

    private fun showWinDialog() {
        val msg = String.format(
            resources.getString(R.string.win_msg)
        )
        winMess = AlertDialog.Builder(context)
            .setTitle(String.format(resources.getString(R.string.win_title), gameModel.levelNumber()))
            .setMessage(msg)
            .setPositiveButton(resources.getString(R.string.move_next)) { _, _ -> gameModel.nextLevel() }
            .setNegativeButton(resources.getString(R.string.win_negative)) { _, _ -> gameModel.onResetLevel() }
            .setOnCancelListener { gameModel.nextLevel() }
            .setIcon(R.drawable.win)
            .create()
        winMess?.show()
    }
}
