package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = Double.NaN;
    private DecimalFormat df = new DecimalFormat("###.#######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        setNumericListeners();
        setOperatorListeners();
        setSpecialListeners();

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            currentInput = "";
            firstOperand = Double.NaN;
            operator = "";
            display.setText("0");
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (!currentInput.isEmpty()) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                display.setText(currentInput.isEmpty() ? "0" : currentInput);
            }
        });

        findViewById(R.id.btn_equals).setOnClickListener(v -> compute());
        findViewById(R.id.btn_dot).setOnClickListener(v -> {
            if (!currentInput.contains(".")) {
                if (currentInput.isEmpty()) {
                    currentInput = "0.";
                } else {
                    currentInput += ".";
                }
                display.setText(currentInput);
            }
        });
    }

    private void setNumericListeners() {
        int[] numericIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        };

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            currentInput += b.getText().toString();
            display.setText(currentInput);
        };

        for (int id : numericIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorListeners() {
        int[] operatorIds = {R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide, R.id.btn_pow};

        View.OnClickListener listener = v -> {
            try {
                if (!currentInput.isEmpty()) {
                    if (!Double.isNaN(firstOperand)) {
                        compute();
                    } else {
                        firstOperand = Double.parseDouble(currentInput);
                    }
                    operator = ((Button) v).getText().toString();
                    display.setText(df.format(firstOperand) + " " + operator);
                    currentInput = "";
                } else if (!Double.isNaN(firstOperand)) {
                    operator = ((Button) v).getText().toString();
                    display.setText(df.format(firstOperand) + " " + operator);
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        };

        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setSpecialListeners() {
        findViewById(R.id.btn_sqrt).setOnClickListener(v -> {
            try {
                double val;
                if (!currentInput.isEmpty()) {
                    val = Double.parseDouble(currentInput);
                } else if (!Double.isNaN(firstOperand)) {
                    val = firstOperand;
                } else {
                    return;
                }
                double result = Math.sqrt(val);
                display.setText(df.format(result));
                currentInput = String.valueOf(result);
                firstOperand = Double.NaN;
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        });
        findViewById(R.id.btn_pi).setOnClickListener(v -> {
            currentInput = String.valueOf(Math.PI);
            display.setText(df.format(Math.PI));
        });
        findViewById(R.id.btn_percent).setOnClickListener(v -> {
            try {
                if (!currentInput.isEmpty()) {
                    double val = Double.parseDouble(currentInput);
                    double result = val / 100;
                    currentInput = String.valueOf(result);
                    display.setText(df.format(result));
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        });
    }

    private void compute() {
        if (!Double.isNaN(firstOperand) && !currentInput.isEmpty()) {
            try {
                double secondOperand = Double.parseDouble(currentInput);
                double result = 0;

                switch (operator) {
                    case "+": result = firstOperand + secondOperand; break;
                    case "-": result = firstOperand - secondOperand; break;
                    case "*": result = firstOperand * secondOperand; break;
                    case "/": 
                        if (secondOperand != 0) {
                            result = firstOperand / secondOperand;
                        } else {
                            display.setText("Error");
                            firstOperand = Double.NaN;
                            currentInput = "";
                            return;
                        }
                        break;
                    case "^": result = Math.pow(firstOperand, secondOperand); break;
                    default: result = secondOperand;
                }

                display.setText(df.format(result));
                firstOperand = result;
                currentInput = "";
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }
}