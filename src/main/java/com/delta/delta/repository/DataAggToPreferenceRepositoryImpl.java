package com.delta.delta.repository;

import com.delta.delta.dto.PreferenceDataDto;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DataAggToPreferenceRepositoryImpl implements DataAggToPreferenceRepository{

    public PreferenceDataDto prefer;

    public boolean save(HashMap<Long, Long> hashMap){

        Map<Long, Long> top20 = hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        prefer = new PreferenceDataDto();
        prefer.setPostIdToPrefer(new ArrayList<>(top20.keySet()));
        prefer.setEventType("prefer");
        hashMap.clear();

        return true;

    }

    public PreferenceDataDto sendPreferData(){
        return prefer;
    }
}
