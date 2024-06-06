package ru.diplomnaya.skilllcinema.presentation.actors

import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ActorFaceBinding
import kotlin.math.sqrt


class ActorFaceFragment : Fragment(),OnTouchListener {
    private val TAG = "Touch"
    var matrix: Matrix = Matrix()
    var savedMatrix: Matrix = Matrix()

    val NONE = 0
    val DRAG = 1
    val ZOOM = 2
    var mode = NONE

    var start = PointF()
    var mid = PointF()
    var oldDist = 1f

    private var _binding: ActorFaceBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActorFaceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg: ActorFaceFragmentArgs by navArgs()
        binding.actorFaceOfPoster.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.actorFaceOfPoster.setOnTouchListener(this)
        Picasso
            .with(requireContext())
            .load(arg.actorsPosterUrl)
            .placeholder(R.drawable.gradient2)
            .into(binding.actorFaceOfPoster)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        //  _binding = null

    }
    override fun onTouch(image: View?, event: MotionEvent?): Boolean {
        val view = image as ImageView
        view.scaleType = ImageView.ScaleType.MATRIX
        val scale: kotlin.Float
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(matrix)
                start[event.x] = event.y
                android.util.Log.d(TAG, "mode=DRAG")
                mode = DRAG
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                android.util.Log.d(TAG, "mode=NONE")
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist =
                    spacing(event)
                android.util.Log.d(TAG, "oldDist=$oldDist")
                if (oldDist > 5f) {
                    savedMatrix.set(matrix)
                    midPoint(
                        mid,
                        event
                    )
                    mode = ZOOM
                    android.util.Log.d(TAG, "mode=ZOOM")
                }
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                matrix.set(savedMatrix)
                if (view.left >= -392) {
                    matrix.postTranslate(event.x - start.x, event.y - start.y)
                }
            } else if (mode == ZOOM) {
                val newDist: kotlin.Float = spacing(event)
                android.util.Log.d(TAG, "newDist=$newDist")
                if (newDist > 5f) {
                    matrix.set(savedMatrix)
                    scale = newDist / oldDist
                    matrix.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }
        view.imageMatrix = matrix

        return true
    }
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2,y / 2)
    }
}