package com.example.s8131464assignment.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.s8131464assignment.R
import com.example.s8131464assignment.network.models.DashboardItem
import com.google.android.material.chip.Chip

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val item: DashboardItem? = intent.getParcelableExtra("item", DashboardItem::class.java)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvSubtitle = findViewById<TextView>(R.id.tvSubtitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val chip1 = findViewById<Chip>(R.id.chip1)
        val chip2 = findViewById<Chip>(R.id.chip2)
        val chip3 = findViewById<Chip>(R.id.chip3)

        if (item == null) {
            tvTitle.text = "No data"
            tvSubtitle.visibility = View.GONE
            tvDescription.visibility = View.GONE
            chip1.visibility = View.GONE
            chip2.visibility = View.GONE
            chip3.visibility = View.GONE
            return
        }

        tvTitle.text = item.property1 ?: ""
        tvSubtitle.text = item.property2 ?: ""
        tvDescription.text = item.description ?: ""

        fun Chip.bind(textValue: String?) {
            if (textValue.isNullOrBlank()) {
                visibility = View.GONE
            } else {
                text = textValue
                visibility = View.VISIBLE
            }
        }

        chip1.bind(item.extra1)
        chip2.bind(item.extra2)
        chip3.bind(item.extra3)
    }
}


