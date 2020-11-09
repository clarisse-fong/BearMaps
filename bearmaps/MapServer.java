package bearmaps;


import bearmaps.server.handler.APIRouteHandler;
import bearmaps.server.handler.APIRouteHandlerFactory;
import bearmaps.server.handler.impl.RasterAPIHandler;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * This code is using BearMaps skeleton code version 4.0.
 * @author Alan Yao, Josh Hug
 */
public class MapServer {


    /**
     * This is where the MapServer is started.
     * @param args
     */
    public static void main(String[] args) {
        MapServerInitializer.initializeServer(APIRouteHandlerFactory.handlerMap);
    }
}
