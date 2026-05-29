package java.ua.kpi.charity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.ua.kpi.charity.entity.Campaign;

@Dao
public interface CampaignDao {

    @Insert
    void insert(Campaign campaign);

    @Query("SELECT * FROM campaigns")
    List<Campaign> getAllCampaigns();

    @Query("SELECT * FROM campaigns WHERE id = :campaignId")
    Campaign getCampaignById(int campaignId);

    @Update
    void update(Campaign campaign);

    @Query("DELETE FROM campaigns WHERE id = :campaignId AND current_amount = 0")
    int deleteCampaignIfEmpty(int campaignId);

    @Query("SELECT IFNULL(SUM(current_amount), 0.0) FROM campaigns")
    Double getTotalCollected();

    @Query("SELECT IFNULL(SUM(target_amount), 0.0) FROM campaigns")
    Double getTotalTarget();

    @Query("SELECT COUNT(*) FROM campaigns")
    int getTotalCampaignsCount();

    @Query("SELECT COUNT(*) FROM campaigns WHERE is_target_reached = 1")
    int getReachedCampaignsCount();
}