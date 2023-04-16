package com.example.fitnesstecnet;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
public class ImcActivity extends AppCompatActivity {
    private EditText editPeso;
    private EditText editAltura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);
        editAltura = findViewById(R.id.edit_imc_altura);
        editPeso = findViewById(R.id.edit_imc_peso);
        Button btncalcula = findViewById(R.id.botao_imc_calcula);
        btncalcula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    Toast.makeText(ImcActivity.this, "Dados Invalidos, favor verificar!", Toast.LENGTH_LONG).show();
                    return;
                }
                String sAltura = editAltura.getText().toString();
                String sPeso = editPeso.getText().toString();
                int altura = Integer.parseInt(sAltura);
                int peso = Integer.parseInt(sPeso);
                double result = calculaImc(altura, peso);
                String msg= imcResposta(result);
                AlertDialog dialog =new AlertDialog.Builder(ImcActivity.this)
                        .setTitle("IMC Calculado:"+String.format("%.2f",result))
                        .setMessage(imcResposta(result))
                        .setNegativeButton(android.R.string.ok,(dialogInterface, which) ->dialogInterface.dismiss())
                        .setPositiveButton("Salvar",(dialogInterface, which) -> {
                                    Bancofit sqlHelper = Bancofit.getInstance(ImcActivity.this);
                                    new Thread(() -> {
                                        long calcId = Bancofit.getInstance(ImcActivity.this).addItem("imc", result);
                                        runOnUiThread(() -> {
                                            if (calcId > 0) {
                                                Toast.makeText(ImcActivity.this, "Registro salvo com sucesso", Toast.LENGTH_LONG).show();
                                                abrelistagem();
                                            }
                                        });
                                    }).start();
                                }).create();
                dialog.show();
                InputMethodManager tcl =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                tcl.hideSoftInputFromWindow(editPeso.getWindowToken(),0);
                tcl.hideSoftInputFromWindow(editAltura.getWindowToken(),0);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_list:
                abrelistagem();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void abrelistagem(){
        Intent intent= new Intent(ImcActivity.this,ListCalcActivity.class);
        intent.putExtra("tipo","imc");
        startActivity(intent);
    }

    private boolean validate() {

        return (!editAltura.getText().toString().startsWith("0")
                && !editPeso.getText().toString().startsWith("0")
                && !editAltura.getText().toString().isEmpty()
                && !editPeso.getText().toString().isEmpty());
    }
    private double calculaImc(int altura, int peso){
        return peso / (((double) altura / 100) * ((double) altura/100));

    }
    private String imcResposta  (double imc){
        if (imc<15)
            return "Severamente abaixo do peso";
        else if(imc<16)
            return "Muito abaixo do   peso";
        else if(imc<18.5)
            return "Abaixo do peso";
        else if(imc<25)
            return "Normal";
        else if(imc<30)
            return "Acima do peso";
        else if(imc<35)
            return "Moderamente obeso";
        else if(imc<40)
            return "Severamente obeso";
        else
            return "Extremamente obeso";

    }

}