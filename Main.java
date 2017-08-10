import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.util.shapes.BBox;
import java.util.List;

public class Main {
    private final static String VEHICLE = "car";
    private final static String OUTDIR = "/app/";
    private final static String INDIR = "/app/";

    // preliminary: get the berlin.pbf from
    // http://download.geofabrik.de/europe/germany.html
    public static void main(String[] args) {
        //createGraph(OUTDIR + "cache");
        //runRequest(OUTDIR + "cache");
        //System.out.print("Test finished!\n");
    }

    // run this on a desktop or server
    private static void createGraph(String graphFolder) {
        GraphHopper hopper = new GraphHopperOSM().forServer();
        hopper.setEncodingManager(new EncodingManager(VEHICLE));
        hopper.setDataReaderFile(INDIR + "berlin-latest.osm.pbf");
        hopper.setGraphHopperLocation(graphFolder);
        // this might take a while and puts several files into graphFolder:
        hopper.importOrLoad();
        hopper.close();
    }

    // only this needs to be run in the browser
    // the sout should give something like:
    // distance: 8762.483637025069, time: 710631, points:228
    private static void runRequest(String graphFolder) {
        // Using forDesktop avoid mmap requirement
        GraphHopper hopper = new GraphHopper().forDesktop();
        // Do not allow writes to support HTTP directory
        hopper.setAllowWrites(false);
        hopper.setEncodingManager(new EncodingManager(VEHICLE));
        hopper.load(graphFolder);
        GHResponse rsp = hopper.route(new GHRequest(52.528963, 13.396454, 52.507863, 13.496704).
                setVehicle(VEHICLE));

        PathWrapper arsp = rsp.getBest();
        System.out.println("distance: " + arsp.getDistance() + ", time: "
                + arsp.getTime() + ", points:" + arsp.getPoints().getSize());
        hopper.close();
    }

    public static Object runRequest(String graphFolder, float a, float b, float c, float d) {
        // Using forDesktop avoid mmap requirement
        GraphHopper hopper = new GraphHopper().forDesktop();
        // Do not allow writes to support HTTP directory
        hopper.setAllowWrites(false);
        hopper.setEncodingManager(new EncodingManager(VEHICLE));
        hopper.load(graphFolder);
        GHResponse rsp = hopper.route(new GHRequest(a, b, c, d).
                setVehicle(VEHICLE));

        Object path = newJSPath();
        if (rsp.hasErrors())
        {
            setError(path,rsp.getErrors().toString());
            System.out.println("error: "+rsp.getErrors().toString());
            return path;
        }
            PathWrapper arsp = rsp.getBest();
            hopper.close();

        if (arsp.hasErrors())
        {
            setError(path,arsp.getErrors().toString());
            System.out.println("error: "+arsp.getErrors().toString());
            return path;
        }
        System.out.println("distance: " + arsp.getDistance() + ", time: "
                + arsp.getTime() + ", points:" + arsp.getPoints().getSize());

        for (Double[] coords: arsp.getPoints().toGeoJson())
        {
            addPoint(path,coords[0].floatValue(),coords[1].floatValue());
        }
        List<Double> bbox = arsp.calcRouteBBox(BBox.createInverse(false)).toGeoJson();
        setBBox(path, bbox.get(0).floatValue(), bbox.get(1).floatValue(), bbox.get(2).floatValue(), bbox.get(3).floatValue());
        return path;
    }
    private static native Object newJSPath();
    private static native void addPoint(Object o, float x1, float x2);
    private static native void setBBox(Object o, float x1, float x2, float x3, float x4);
    private static native void setError(Object o, String err);
}

