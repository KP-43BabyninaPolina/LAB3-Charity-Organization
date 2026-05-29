package java.ua.kpi.charity.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.ua.kpi.charity.dao.CampaignDao;
import java.ua.kpi.charity.database.CharityDatabase;
import java.ua.kpi.charity.entity.Campaign;

public class CampaignRepository {

    private final CampaignDao campaignDao;
    private final ExecutorService executorService;
    private final Handler mainThreadHandler;

    public CampaignRepository(Application application) {
        CharityDatabase db = CharityDatabase.getInstance(application);
        this.campaignDao = db.campaignDao();

        this.executorService = Executors.newFixedThreadPool(4);

        this.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public interface OperationCallback {
        void onResult(boolean isSuccess);
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);
    }

    public void insert(Campaign campaign) {
        executorService.execute(() -> campaignDao.insert(campaign));
    }

    public void update(Campaign campaign) {
        executorService.execute(() -> campaignDao.update(campaign));
    }

    public void deleteSafely(int campaignId, OperationCallback callback) {
        executorService.execute(() -> {
            int deletedRows = campaignDao.deleteCampaignIfEmpty(campaignId);
            boolean success = deletedRows > 0;

            mainThreadHandler.post(() -> callback.onResult(success));
        });
    }

    public void getAllCampaigns(DataCallback<List<Campaign>> callback) {
        executorService.execute(() -> {
            List<Campaign> campaigns = campaignDao.getAllCampaigns();
            mainThreadHandler.post(() -> callback.onDataLoaded(campaigns));
        });
    }

    public void getCampaignById(int campaignId, DataCallback<Campaign> callback) {
        executorService.execute(() -> {
            Campaign campaign = campaignDao.getCampaignById(campaignId);
            mainThreadHandler.post(() -> callback.onDataLoaded(campaign));
        });
    }

    public void getTotalCollected(DataCallback<Double> callback) {
        executorService.execute(() -> {
            double total = campaignDao.getTotalCollected();
            mainThreadHandler.post(() -> callback.onDataLoaded(total));
        });
    }

    public void getTotalTarget(DataCallback<Double> callback) {
        executorService.execute(() -> {
            double target = campaignDao.getTotalTarget();
            mainThreadHandler.post(() -> callback.onDataLoaded(target));
        });
    }

    public void getTotalCampaignsCount(DataCallback<Integer> callback) {
        executorService.execute(() -> {
            int count = campaignDao.getTotalCampaignsCount();
            mainThreadHandler.post(() -> callback.onDataLoaded(count));
        });
    }

    public void getReachedCampaignsCount(DataCallback<Integer> callback) {
        executorService.execute(() -> {
            int count = campaignDao.getReachedCampaignsCount();
            mainThreadHandler.post(() -> callback.onDataLoaded(count));
        });
    }
}