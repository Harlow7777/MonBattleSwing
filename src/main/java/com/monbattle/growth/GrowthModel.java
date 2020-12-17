package com.monbattle.growth;

import lombok.Data;

import java.io.Serializable;

@Data
public class GrowthModel implements Serializable {
    protected double HP = 0.45;
    protected double attack = 0.45;
    protected double defense = 0.45;
    protected double speed = 0.45;
    protected double accuracy = 0.0;
}
