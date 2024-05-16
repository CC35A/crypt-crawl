import java.lang.management.ManagementFactory;
import com.sun.management.HotSpotDiagnosticMXBean;

public class HeapDump {
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";
    private static volatile HotSpotDiagnosticMXBean hotspotMBean;

    public static void dumpHeap(String filePath, boolean live) {
        if (hotspotMBean == null) {
            synchronized (HeapDump.class) {
                if (hotspotMBean == null) {
                    hotspotMBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
                }
            }
        }
        try {
            hotspotMBean.dumpHeap(filePath, live);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}