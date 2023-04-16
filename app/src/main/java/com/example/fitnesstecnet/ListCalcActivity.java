package com.example.fitnesstecnet;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);
        Bundle extras = getIntent().getExtras();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_list);
        if (extras != null) {
            String tipo =extras.getString("tipo");
            List<Registro> registros=Bancofit.getInstance(this).getRegidtersBy(tipo);
            Log.d("Teste",registros.toString());
            ListCalcAdapter activity = new ListCalcAdapter(registros);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(activity);
        }
    }
    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder>{
        private List<Registro> datas;
        private AdapterView.OnItemClickListener listener;
        public ListCalcAdapter(List<Registro>datas){
            this.datas=datas;
        }
        @NonNull
        @Override
        public ListCalcViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
            return new ListCalcViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1,parent,false));

        }
        @Override
        public void onBindViewHolder(@NonNull ListCalcViewHolder holder, int position){
            Registro data =datas.get(position);
            holder.bind(data);
        }
        @Override
        public int getItemCount(){
            return datas.size();
        }
        private class ListCalcViewHolder extends RecyclerView.ViewHolder{
            public ListCalcViewHolder(@NonNull View itemView){
                super(itemView);
            }
            public void bind(Registro data){
                String dtbr, imc,tbm;
                imc=String.format("%.2f",data.resultado);
                tbm=String.format("%.2f",data.resultado);
                dtbr="";
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt","BR"));
                    Date dateSaved=sdf.parse(data.dtcriacao);
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt","BR") );
                    dtbr=dateFormat.format(dateSaved);
                }catch (ParseException e){

                }
                ((TextView)itemView).setText("Imc " +imc+ "" +dtbr);
                ((TextView)itemView).setText("Imc " +tbm+ "" +dtbr);
            }
        }
    }
}