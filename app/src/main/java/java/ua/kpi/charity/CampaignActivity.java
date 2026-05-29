package java.ua.kpi.charity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.ua.kpi.charity.entity.Campaign;
import java.ua.kpi.charity.repository.CampaignRepository;
import java.ua.kpi.charity.utils.ErrorMessages;
import java.ua.kpi.charity.utils.IntentAttributes;
import java.ua.kpi.charity.utils.Messages;
import java.util.Date;
import java.util.Locale;

public class CampaignActivity extends AppCompatActivity {

    private EditText etName, etOrganizerName, etStartDate, etEndDate, etTargetAmount,
            etDonationAmount, etGoalDescription;
    private Spinner spinnerOrganization;
    private LinearLayout layoutDonation;
    private Button btnSave, btnDelete;

    private CampaignRepository repository;
    private String currentMode;
    private int campaignId = -1;
    private Campaign currentCampaign;

    private final Calendar calendarStart = Calendar.getInstance();
    private final Calendar calendarEnd = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",
                                                                        Locale.getDefault());

    private final String[] organizations = {
            "НКО \"Промінь\" філіал на вул. Хрещатик 5",
            "НКО \"Промінь\" філіал на вул. Шулявська 2"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_activity);

        repository = new CampaignRepository(getApplication());

        etName = findViewById(R.id.etCampaignName);
        etOrganizerName = findViewById(R.id.etOrganizerName);
        spinnerOrganization = findViewById(R.id.spinnerOrganization);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etTargetAmount = findViewById(R.id.etTargetAmount);
        etDonationAmount = findViewById(R.id.etDonationAmount);
        etGoalDescription = findViewById(R.id.etGoalDescription);
        layoutDonation = findViewById(R.id.layoutDonation);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, organizations);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrganization.setAdapter(spinnerAdapter);

        etStartDate.setOnClickListener(v -> showDatePicker(calendarStart, etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(calendarEnd, etEndDate));

        Intent intent = getIntent();
        if (intent != null) {
            currentMode = intent.getStringExtra(IntentAttributes.MODE);
            campaignId = intent.getIntExtra(IntentAttributes.CAMPAIGN_ID, -1);
        }

        if (currentMode.equals(IntentAttributes.Modes.EDIT) && campaignId != -1) {
            layoutDonation.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            loadCampaignData();
        } else if (currentMode.equals(IntentAttributes.Modes.CREATE)) {
            layoutDonation.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, ErrorMessages.UNKNOWN_MODE_ERROR, Toast.LENGTH_LONG).show();
            finish();
        }

        btnSave.setOnClickListener(v -> handleSaveAction());
        btnDelete.setOnClickListener(v -> handleDeleteAction());
    }

    private void showDatePicker(Calendar calendar, EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener =
                (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(dateFormat.format(calendar.getTime()));
        };

        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadCampaignData() {
        repository.getCampaignById(campaignId, campaign -> {
            if (campaign != null) {
                currentCampaign = campaign;
                etName.setText(campaign.getName());
                etOrganizerName.setText(campaign.getOrganizerName());
                etStartDate.setText(campaign.getStartDate());
                etEndDate.setText(campaign.getEndDate());
                etTargetAmount.setText(String.valueOf(campaign.getTargetAmount()));
                etGoalDescription.setText(campaign.getGoalDescription());

                try {
                    Date start = dateFormat.parse(campaign.getStartDate());
                    Date end = dateFormat.parse(campaign.getEndDate());
                    if (start != null) calendarStart.setTime(start);
                    if (end != null) calendarEnd.setTime(end);
                } catch (ParseException ignored) {}

                for (int i = 0; i < organizations.length; i++) {
                    if (organizations[i].equals(campaign.getOrganizationName())) {
                        spinnerOrganization.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void handleSaveAction() {
        String name = etName.getText().toString().trim();
        String organizer = etOrganizerName.getText().toString().trim();
        String organization = spinnerOrganization.getSelectedItem().toString();
        String startStr = etStartDate.getText().toString().trim();
        String endStr = etEndDate.getText().toString().trim();
        String targetStr = etTargetAmount.getText().toString().trim();
        String description = etGoalDescription.getText().toString().trim();

        if (anyIsEmpty(name, organizer, startStr, endStr, targetStr, description) ||
                invalidName(name) || invalidOrganizerName(organizer) ||
                invalidDates() || !validAmount(targetStr, etTargetAmount)) return;

        double targetAmount = Double.parseDouble(targetStr);

        if (currentMode.equals(IntentAttributes.Modes.CREATE)) {
            Campaign newCampaign = new Campaign(name, organizer, organization,
                                                startStr, endStr, targetAmount, description);
            repository.insert(newCampaign);
            Toast.makeText(this, Messages.CREATE_CAMPAIGN_SUCCESS,
                    Toast.LENGTH_SHORT).show();

        } else if (currentMode.equals(IntentAttributes.Modes.EDIT) && currentCampaign != null) {

            String donationStr = etDonationAmount.getText().toString().trim();

            if (validAmount(donationStr,etDonationAmount)) {
                double donation = Double.parseDouble(donationStr);
                currentCampaign.setCurrentAmount(currentCampaign.getCurrentAmount() + donation);
            }
            else return;

            currentCampaign.setName(name);
            currentCampaign.setOrganizerName(organizer);
            currentCampaign.setOrganizationName(organization);
            currentCampaign.setStartDate(startStr);
            currentCampaign.setEndDate(endStr);
            currentCampaign.setTargetAmount(targetAmount);
            currentCampaign.setGoalDescription(description);

            repository.update(currentCampaign);

            Toast.makeText(this, Messages.UPDATE_CAMPAIGN_SUCCESS,
                    Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, ErrorMessages.UNKNOWN_MODE_ERROR,
                    Toast.LENGTH_LONG).show();
        }

        finish();
    }

    private void handleDeleteAction() {
        if (currentCampaign != null) {
            repository.deleteSafely(campaignId, isSuccess -> {
                if (isSuccess) {
                    Toast.makeText(this, Messages.DELETE_CAMPAIGN_SUCCESS,
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, ErrorMessages.DELETE_CAMPAIGN_ERROR,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validAmount(String amountStr, EditText field) {
        if (!amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    field.setError(ErrorMessages.AMOUNT_ERROR);
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                field.setError(ErrorMessages.AMOUNT_FORMAT_ERROR);
                return false;
            }
        }
        return false;
    }

    private boolean invalidDates() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);

        calendarEnd.set(Calendar.HOUR_OF_DAY, 0);
        calendarEnd.set(Calendar.MINUTE, 0);
        calendarEnd.set(Calendar.SECOND, 0);
        calendarEnd.set(Calendar.MILLISECOND, 0);

        if (calendarStart.before(today)) {
            etStartDate.setError(ErrorMessages.START_DATE_TOO_EARLY_ERROR);
            Toast.makeText(this, ErrorMessages.START_DATE_TOO_EARLY_TOAST,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!calendarEnd.after(calendarStart)) {
            etEndDate.setError(ErrorMessages.END_DATE_EARLIER_THEN_START_ERROR);
            Toast.makeText(this, ErrorMessages.END_DATE_EARLIER_THEN_START_TOAST,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean invalidOrganizerName(String organizer) {
        if (organizer.length() < 8) {
            etOrganizerName.setError(ErrorMessages.ORGANIZER_NAME_TOO_SHORT_ERROR);
            return true;
        }
        String[] words = organizer.split("\\s+");
        if (words.length < 3) {
            etOrganizerName.setError(ErrorMessages.ORGANIZER_NAME_FORMAT_ERROR);
            return true;
        }
        return false;
    }

    private boolean invalidName(String name) {
        if (name.length() < 10) {
            etName.setError(ErrorMessages.CAMPAIGN_NAME_TOO_SHORT_ERROR);
            return true;
        }
        return false;
    }

    private boolean anyIsEmpty(String name, String organizer, String startStr,
                               String endStr, String targetStr, String description) {
        if (name.isEmpty() || organizer.isEmpty() || startStr.isEmpty()
                || endStr.isEmpty() || targetStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, ErrorMessages.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}