package java.ua.kpi.charity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.ua.kpi.charity.entity.Campaign;
import java.util.List;
import java.util.Locale;

public class CampaignAdapter extends ArrayAdapter<Campaign> {
    private final Context context;

    public CampaignAdapter(@NonNull Context context, @NonNull List<Campaign> campaigns) {
        super(context, R.layout.campaign_item, campaigns);
        this.context = context;
    }

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvProgress;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Campaign campaign = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);

            convertView = inflater.inflate(R.layout.campaign_item, parent, false);

            viewHolder.tvTitle = convertView.findViewById(R.id.tvCampaignTitle);
            viewHolder.tvProgress = convertView.findViewById(R.id.tvCampaignProgress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (campaign != null) {
            viewHolder.tvTitle.setText(campaign.getName());
            var progress = String.format(Locale.getDefault(),"Зібрано %,.2f з %,.2f грн",
                                                                    campaign.getCurrentAmount(),
                                                                    campaign.getTargetAmount());
            viewHolder.tvProgress.setText(progress);
        }

        return convertView;
    }
}