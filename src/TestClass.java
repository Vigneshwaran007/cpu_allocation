import com.cpu.allocator.CPUInstanceAllocator;
import com.cpu.logger.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestClass {
    public static void main(String[] args) throws Exception {
        File jsonInput = new File("C:\\Users\\arun-5874\\Downloads\\CPUAllocation\\src\\input.json");
        InputStream inputStream = new FileInputStream(jsonInput);

        // Creating JSONObject from input file
        JSONObject input = new JSONObject(new JSONTokener(inputStream));

        CPUInstanceAllocator cpuInstanceAllocator = new CPUInstanceAllocator(input);
        
		Logger.log(cpuInstanceAllocator.get_costs(4, 20, 0.001f).toString());
		Logger.log(cpuInstanceAllocator.get_costs(4, -1, 100).toString());
		Logger.log(cpuInstanceAllocator.get_costs(4, 20, -1).toString());
    }
}
