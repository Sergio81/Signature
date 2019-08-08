package com.androidbox.signature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.view.animation.*
import java.io.ByteArrayInputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextButton.setOnClickListener {
            if (captureSignatureView.containSignature) {
                lineView.visibility = View.GONE
                nameText.visibility = View.GONE
                clearWrapper.visibility = View.GONE

                val inputStream = ByteArrayInputStream(captureSignatureView.bytes)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                imageView2.setImageBitmap(bitmap)
                //imageView2.setImageBitmap(captureSignatureView.bitmap)

                ImageContainer.visibility = View.VISIBLE
            }
        }

        ImageContainer.setOnClickListener {
            lineView.visibility = View.VISIBLE
            nameText.visibility = View.VISIBLE
            clearWrapper.visibility = View.VISIBLE
            ImageContainer.visibility = View.GONE
        }

        captureSignatureView.setSignatureListener(object : CaptureSignatureView.SignatureListener {
            override fun onStartDrawing() {

                if (clearText.text == "") {
                    clearButton
                        .apply {
                            rotation = 0f
                            alpha = 0f
                        }
                        .animate()
                        .apply {
                            interpolator = AnticipateOvershootInterpolator()
                            duration = 1000
                            rotation(360f)
                            alpha(1f)
                        }
                        .start()

                    clearText
                        .apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.clear)
                            alpha = 0f
                            x = 50f
                        }
                        .animate()
                        .apply {
                            interpolator = AnticipateOvershootInterpolator()
                            duration = 1000
                            this.translationX(0f)
                            alpha(1f)
                        }
                        .start()
                }
            }
        })

        clearButton.setOnClickListener {
            captureSignatureView.clearCanvas()
        }

    }
}
