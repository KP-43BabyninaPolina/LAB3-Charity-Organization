package java.ua.kpi.charity.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "campaigns")
public class Campaign {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "organizer_name")
    private String organizerName;

    @ColumnInfo(name = "organization_name")
    private String organizationName;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;

    @ColumnInfo(name = "target_amount")
    private double targetAmount;

    @ColumnInfo(name = "current_amount")
    private double currentAmount;

    @ColumnInfo(name = "goal_description")
    private String goalDescription;

    @ColumnInfo(name = "is_target_reached")
    private boolean isTargetReached;

    public Campaign(String name, String organizerName, String organizationName,
                    String startDate, String endDate, double targetAmount,
                    String goalDescription) {
        this.name = name;
        this.organizerName = organizerName;
        this.organizationName = organizationName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetAmount = targetAmount;
        this.goalDescription = goalDescription;

        this.currentAmount = 0.0;
        this.isTargetReached = false;
    }

    private void recalculateTargetStatus() {
        this.isTargetReached = this.currentAmount >= this.targetAmount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
        recalculateTargetStatus(); // Автоматично оновлюємо статус, якщо змінили ціль
    }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
        recalculateTargetStatus();
    }

    public String getGoalDescription() { return goalDescription; }
    public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }

    public boolean isTargetReached() { return isTargetReached; }
    public void setTargetReached(boolean targetReached) { this.isTargetReached = targetReached; }
}