package com.bytter.biz.netbank.dto;

import com.bytter.biz.netbank.entity.NisCorpBus;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NisCorpBusAddVO extends NisCorpBus implements Serializable {
    List<String> busIds;
}
