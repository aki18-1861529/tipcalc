package edu.us.ischool.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var textEdit : EditText
    lateinit var btn : Button
    lateinit var spinner : Spinner
    var tipAmount : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textEdit = findViewById<EditText>(R.id.editTextNumberDecimal)
        btn = findViewById<Button>(R.id.button)
        spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            this,
            R.array.tip_amt_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(1)

        btn.isEnabled = false
        btn.isClickable = false

        textEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                btn.isEnabled = textEdit.text.toString().trim().isNotEmpty()
                btn.isClickable = textEdit.text.toString().trim().isNotEmpty()

                if (textEdit.text.toString() == "$0.00") {
                    btn.isEnabled = false
                    btn.isClickable = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textEdit.removeTextChangedListener(this)
                if (p0 != null) {
                    val clean = p0.replace("[$,.]".toRegex(), "")
                    var curr = NumberFormat.getCurrencyInstance().format(clean.toDouble() / 100)
                    textEdit.setText(curr)
                    textEdit.setSelection(curr.length)
                }
                textEdit.addTextChangedListener(this)
            }
        })

        btn.setOnClickListener {
            val input : Double = textEdit.text.toString().replace("[$,.]".toRegex(), "").toDouble()
            val tip : String = NumberFormat.getCurrencyInstance().format(input * tipAmount / 100)
            Toast.makeText(this, tip, Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        var amt : String = parent?.getItemAtPosition(pos).toString().replace("[%]".toRegex(), "")
        tipAmount = amt.toDouble() / 100
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}