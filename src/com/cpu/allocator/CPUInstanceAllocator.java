package com.cpu.allocator;

import com.cpu.allocator.constants.ResponseConstants;
import com.cpu.allocator.region.DataCenter;
import com.cpu.allocator.region.DefaultRegionalDCImpl;
import com.cpu.logger.Logger;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CPUInstanceAllocator {

    List<DataCenter> dataCenters = new ArrayList<>();

    public CPUInstanceAllocator(JSONObject regionalDataCenterDetails) {
        Iterator<String> iterator = regionalDataCenterDetails.keys();
        while (iterator.hasNext()) {
            String regionName = iterator.next();
            dataCenters.add(new DefaultRegionalDCImpl(regionName, regionalDataCenterDetails.getJSONObject(regionName)));
        }
    }

    /**
     * @param hours
     * @param cpus
     * @param price
     * @return
     */
    public JSONArray get_costs(int hours, int cpus, float price) {
        Logger.log("Started computing possible cpu instances");

        if (hours <= 0) {
            throw new IllegalArgumentException("Hours is mandatory for computation");
        }

        if (cpus <= 0 && price <= 0) {
            throw new IllegalArgumentException("Either number of cpus or price is mandatory");
        }
	
		Map<Float, JSONObject> sortedMap = new TreeMap<>();
        
        if (cpus <= 0) {
            for (DataCenter dataCenter : dataCenters) {
            	JSONObject result = dataCenter.getCost(hours, price);
            	float totalCost = result.getFloat(ResponseConstants.total_cost_unformatted.toString());
				result.remove(ResponseConstants.total_cost_unformatted.toString());
				sortedMap.put(totalCost, result);
            }
        } else if (price <= 0) {
            for (DataCenter dataCenter : dataCenters) {
				JSONObject result = dataCenter.getCost(hours, price);
				float totalCost = result.getFloat(ResponseConstants.total_cost_unformatted.toString());
				result.remove(ResponseConstants.total_cost_unformatted.toString());
				sortedMap.put(totalCost, result);
			}
        } else {
            for (DataCenter dataCenter : dataCenters) {
				JSONObject result = dataCenter.getCost(hours, price);
				float totalCost = result.getFloat(ResponseConstants.total_cost_unformatted.toString());
				result.remove(ResponseConstants.total_cost_unformatted.toString());
				sortedMap.put(totalCost, result);
			}
        }
	
        return new JSONArray(sortedMap.values());
    }
}