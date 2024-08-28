package com.delta.delta.repository;

import com.delta.delta.dto.PreferenceDataDto;

import java.util.HashMap;

public interface DataAggToPreferenceRepository {

    boolean save(HashMap<Long, Long> hashMap);
    PreferenceDataDto sendPreferData();
}
