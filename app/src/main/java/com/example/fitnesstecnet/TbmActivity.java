package com.example.fitnesstecnet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class TbmActivity extends AppCompatActivity {
    private EditText editPeso;
    private EditText editAltura;
    private EditText editIdade;
    private RadioButton masc;
    private RadioButton fem;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbm);
        editAltura = findViewById(R.id.edit_imc_altura);
        editPeso = findViewById(R.id.edit_imc_peso);
        editIdade = findViewById(R.id.edit_idade);
        fem=findViewById(R.id.edit_Fe);
        masc=findViewById(R.id.edit_Mas);


        Button btncalcula = findViewById(R.id.botao_Tbm_calcula);
        btncalcula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    Toast.makeText(TbmActivity.this, "Dados Invalidos, favor verificar!", Toast.LENGTH_LONG).show();
                    return;
                }
                String sAltura = editAltura.getText().toString();
                String sPeso = editPeso.getText().toString();
                String sIdade = editIdade.getText().toString();
                int altura = Integer.parseInt(sAltura);
                int peso = Integer.parseInt(sPeso);
                int Idade = Integer.parseInt(sIdade);

                double result = calculaTbm(altura, peso, Idade);
                String msg = TbmResposta(result);
                AlertDialog dialog = new AlertDialog.Builder(TbmActivity.this)
                        .setTitle("TBM Calculado:" + String.format("%.2f", result))
                        .setMessage(TbmResposta(result))
                        .setNegativeButton(android.R.string.ok, (dialogInterface, which) -> dialogInterface.dismiss())
                        .setPositiveButton("Salvar", (dialogInterface, which) -> {
                            Bancofit sqlHelper = Bancofit.getInstance(TbmActivity.this);
                            new Thread(() -> {
                                long calcId = Bancofit.getInstance(TbmActivity.this).addItem("tbm", result);
                                runOnUiThread(() -> {
                                    if (calcId > 0) {
                                        Toast.makeText(TbmActivity.this, "Registro salvo com sucesso", Toast.LENGTH_LONG).show();
                                        abrelistagem();
                                    }
                                });
                            }).start();
                        }).create();
                dialog.show();
                InputMethodManager tcl = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                tcl.hideSoftInputFromWindow(editPeso.getWindowToken(), 0);
                tcl.hideSoftInputFromWindow(editIdade.getWindowToken(), 0);
                tcl.hideSoftInputFromWindow(editAltura.getWindowToken(), 0);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list:
                abrelistagem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrelistagem() {
        Intent intent = new Intent(TbmActivity.this, ListTbm.class);
        intent.putExtra("tipo", "tbm");
        startActivity(intent);
    }

    private boolean validate() {

        return (!editAltura.getText().toString().startsWith("0")
                && !editPeso.getText().toString().startsWith("0")
                && !editIdade.getText().toString().startsWith("0")
                && !editIdade.getText().toString().startsWith("0")
                && !editAltura.getText().toString().isEmpty()
                && !editIdade.getText().toString().isEmpty()
                && !editPeso.getText().toString().isEmpty());
    }

    private double calculaTbm(int altura, int peso, int idade) {
            if(masc.isChecked()){
                return 66 + (13.8 * peso) + (5 * altura) - (6.8 * idade);
            }
            else if(fem.isChecked()){
                return 655+(9.6*peso)+(1.8*altura)-(4.7*idade);
            }

            return 0;

    }


    private String TbmResposta  (double tbm){
       if (tbm<1.2){
           return "pouco ou nenhum exercício";

       }
       else if (tbm<1.375){
            return "exercício leve 1 a 3 dias por semana";

        }
       else if (tbm<1.55){
           return "exercício moderado, faz esportes 3 a 5 dias por semana";

       }
       else if (tbm<1.725){
           return "exercício pesado de 5 a 6 dias por semana";

       }
       else if (tbm<1.9){
           return "exercício pesado diariamente e até 2 vezes por dia";

       }

        return null;
    }

}

