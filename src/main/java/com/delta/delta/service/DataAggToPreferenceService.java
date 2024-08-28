package com.delta.delta.service;

import com.delta.delta.dto.NotificationsDto;
import com.delta.delta.dto.PreferenceDataDto;

public interface DataAggToPreferenceService {

    void consumeAgg(NotificationsDto notificationsDto);
    PreferenceDataDto sendPreferData();
}
