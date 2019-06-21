package com.androidbox.signature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import android.util.Base64.NO_WRAP
import java.io.ByteArrayInputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clearButton.setOnClickListener {
            captureSignatureView.clearCanvas()
        }

        nextButton.setOnClickListener {
            if(captureSignatureView.containSignature) {
                lineView.visibility = View.GONE
                nameText.visibility = View.GONE

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
            ImageContainer.visibility = View.GONE
        }

    }
}
