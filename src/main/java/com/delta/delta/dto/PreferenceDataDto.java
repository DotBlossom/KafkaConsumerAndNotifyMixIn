package com.delta.delta.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class PreferenceDataDto {

    private String eventType;

    private List<Long> postIdToPrefer;

}
