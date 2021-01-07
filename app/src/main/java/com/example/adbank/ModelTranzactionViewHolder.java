package com.example.adbank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ModelTranzactionViewHolder extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    private TextView quantityTranzactionHistory;
    private TextView uidAccountHistory;
    private TextView rejectCount;
    private  TextView waitCount;
    private  TextView succesCount;
    private LinearLayout itemLayout;
    private TextView dataReq;
    private View statusColorTranza;
    private int succes=0,wait=0,reject=0;
    public ModelTranzactionViewHolder(View itemView) {
        super(itemView);
        itemLayout=(LinearLayout)itemView.findViewById(R.id.itemLayoutTranzaction) ;
        appIcon = (ImageView) itemView.findViewById(R.id.icon);
        appName = (TextView) itemView.findViewById(R.id.title);
        quantityTranzactionHistory = (TextView) itemView.findViewById(R.id.quantityTransferValueListTranz);
        uidAccountHistory = (TextView) itemView.findViewById(R.id.uid_AccountHistory);
        dataReq=(TextView)itemView.findViewById(R.id.dateRequestTranzValue);
        statusColorTranza=(View)itemView.findViewById(R.id.statusColorTranzaction);

//
//        waitCount= R.layout.activity_list_tranzactions.findViewById(R.id.transactionCountWait);
//        rejectCount=(TextView)itemView.findViewById(R.id.refusedTranzaction);
//        succesCount=(TextView)itemView.findViewById(R.id.transactionCountAccepted);

    }

    public void bindTo(ModelTranzaction modelTranzaction) {
           // itemLayout.setBackgroundColor(Color.rgb(0x8b,0xc3,0x4a));
            //appIcon.setImageDrawable(modelTranzaction.getAppIcon());
            appName.setText(modelTranzaction.getUid_application());
            uidAccountHistory.setText(modelTranzaction.getUid_account());
            quantityTranzactionHistory.setText(modelTranzaction.getQuantityTranzaction());
            dataReq.setText(modelTranzaction.getDateRequest());
            String setColor=modelTranzaction.getStatusTranzaction();
            if(setColor.equals("0"))
            {
                statusColorTranza.setBackgroundColor(itemView.getResources().getColor(R.color.waitingColor));
                wait++;
            }else if (setColor.equals("1"))
            {
                statusColorTranza.setBackgroundColor(itemView.getResources().getColor(R.color.frontColor1));
                succes++;
            }else{
                statusColorTranza.setBackgroundColor(itemView.getResources().getColor(R.color.rejectColor));
                reject++;
            }

             //waitCount.setText(String.valueOf("jkj"));
//            rejectCount.setText(String.valueOf(reject));
//            succesCount.setText(String.valueOf(succes));

    }
}
