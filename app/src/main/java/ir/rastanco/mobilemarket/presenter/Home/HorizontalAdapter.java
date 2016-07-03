package ir.rastanco.mobilemarket.presenter.Home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ir.rastanco.mobilemarket.R;
import ir.rastanco.mobilemarket.presenter.TabbedActivity;

/**
 * Created by ShaisteS on 6/29/2016.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

    private List<String> horizontalList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.recyclerViewItems);

        }
    }


        public HorizontalAdapter(List<String> horizontalList,Context context) {
            this.horizontalList = horizontalList;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_recyclerview_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

//            holder.imageView.setText(horizontalList.get(position));
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TabbedActivity.class);
                    context.startActivity(intent);
                }
            });*/

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,holder.imageView.getText().toString(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, TabbedActivity.class);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
//            return 20;
        }
}