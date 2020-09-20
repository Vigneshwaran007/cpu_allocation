package com.cpu.allocator.region;

import org.json.JSONObject;

public interface DataCenter {
    String getRegionName();

    JSONObject getCost(int hours, int cpus, float price);

    JSONObject getCost(int hours, float price);

    JSONObject getCost(int hours, int cpus);
}
