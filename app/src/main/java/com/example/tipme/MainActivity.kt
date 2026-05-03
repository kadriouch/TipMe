package com.example.tipme

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipLabel: TextView
    private lateinit var tvAmountTip: TextView
    private lateinit var tvTotalLabel: TextView
    private lateinit var tvTotal: TextView

    private lateinit var tvTipDescription: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipLabel = findViewById(R.id.tvTipLabel)
        tvAmountTip = findViewById(R.id.tvAmountTip)
        tvTotalLabel = findViewById(R.id.tvTotalLabel)
        tvTotal = findViewById(R.id.tvTotal)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = getString(R.string.tip_percent_format, INITIAL_TIP_PERCENT)

        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = getString(R.string.tip_percent_format, progress)
                computeTipAndTotal()

                updateTipDescription(progress)

            }



            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        // Update the color based on the tip percent
        val color = when (tipPercent) {
            in 0..9 -> ContextCompat.getColor(this, R.color.colorTipWorst)
            in 10..14 -> ContextCompat.getColor(this, R.color.colorTipPoor)
            in 15..19 -> ContextCompat.getColor(this, R.color.colorTipGood)
            else -> ContextCompat.getColor(this, R.color.colorTipGreat)
        }
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()) {
            tvAmountTip.text = ""
            tvTotal.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        tvAmountTip.text = "%.2f".format(tipAmount)
        tvTotal.text = "%.2f".format(totalAmount)
    }
}