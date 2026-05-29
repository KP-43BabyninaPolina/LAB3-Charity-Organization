package java.ua.kpi.charity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import java.ua.kpi.charity.utils.IntentAttributes;
import java.util.ArrayList;
import java.util.List;

import java.ua.kpi.charity.entity.Campaign;
import java.ua.kpi.charity.repository.CampaignRepository;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvAnalyticsData;
    private Button btnCreateCampaign;
    private ListView lvCampaigns;

    private CampaignRepository repository;
    private List<Campaign> campaignList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        repository = new CampaignRepository(getApplication());

        tvAnalyticsData = findViewById(R.id.tvAnalyticsData);
        btnCreateCampaign = findViewById(R.id.btnCreateCampaign);
        lvCampaigns = findViewById(R.id.lvCampaigns);

        btnCreateCampaign.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CampaignActivity.class);
            intent.putExtra(IntentAttributes.CAMPAIGN_ID, -1);
            intent.putExtra(IntentAttributes.MODE, IntentAttributes.Modes.CREATE);
            startActivity(intent);
        });

        lvCampaigns.setOnItemClickListener((parent, view, position, id) -> {
            Campaign selectedCampaign = campaignList.get(position);
            Intent intent = new Intent(MainActivity.this, CampaignActivity.class);
            intent.putExtra(IntentAttributes.CAMPAIGN_ID, selectedCampaign.getId());
            intent.putExtra(IntentAttributes.MODE, IntentAttributes.Modes.EDIT);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
        loadCampaigns();
    }

    private void loadStatistics() {
        repository.getTotalCollected(collected -> {
            repository.getTotalTarget(target -> {
                repository.getTotalCampaignsCount(totalCount -> {
                    repository.getReachedCampaignsCount(reachedCount -> {

                        String statsText = String.format(Locale.getDefault()
                                ,"Загалом зібрано %,.2f з %,.2f грн." +
                                        "\nЦіль досягнуто для %d з %d кампаній.",
                                collected, target, reachedCount, totalCount
                        );
                        tvAnalyticsData.setText(statsText);
                    });
                });
            });
        });
    }

    private void loadCampaigns() {
        repository.getAllCampaigns(campaigns -> {
            this.campaignList = campaigns;

            var adapter = new CampaignAdapter(MainActivity.this, campaignList);

            lvCampaigns.setAdapter(adapter);
        });
    }
}