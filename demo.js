function createMap(divId) {
    var osmAttr = '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors';

    var omniscale = L.tileLayer.wms('https://maps.omniscale.net/v1/ghexamples-3646a190/tile', {
        layers: 'osm',
        attribution: osmAttr + ', &copy; <a href="http://maps.omniscale.com/">Omniscale</a>'
    });

    var osm = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: osmAttr
    });

    var map = L.map(divId, {layers: [omniscale]});
    L.control.layers({
        "Omniscale": omniscale,
        "OpenStreetMap": osm
    }).addTo(map);
    return map;
}

var iconObject = L.icon({
    iconUrl: './img/marker-icon.png',
    shadowSize: [50, 64],
    shadowAnchor: [4, 62],
    iconAnchor: [12, 40]
});

var map = createMap("map");
// a bit inconsistent: latitude,longitude here but in GeoJSON it is lon,lat:
map.setView([39.8, -105], 11);

var routingLayer = L.geoJson().addTo(map);
routingLayer.options = {
    style: {color: "#00cc33", "weight": 5, "opacity": 0.6}
};

var queryPoints = [];

map.on('click', function (e) {

    if (queryPoints.length > 1) {
        queryPoints = [];
        routingLayer.clearLayers();
    }

    L.marker(e.latlng, {icon: iconObject}).addTo(routingLayer);
    queryPoints.push([e.latlng]);


    if (queryPoints.length > 1) {

        // ******************
        //  TODO Calculate route! 
        // ******************
        // Input: queryPoints
        // Output: path with points and bbox
        // get points from ghresponse.getBest().getPoints() (gives you a list of latitude & longitude & elevation)
        // and with 'var elevation=true' you get the bbox from ghresponse.getBest().calculateBBox(BBox.create(elevation))

        var path = {
            points: [[-104.99404, 39.75621], [-105, 39.9], [-104.9, 39.8]],
            bbox: [-105, 39.7, -104.9, 39.9],
            errors: "test"
        };
        

        if (path.bbox) {
            var minLon = path.bbox[0];
            var minLat = path.bbox[1];
            var maxLon = path.bbox[2];
            var maxLat = path.bbox[3];
            var tmpB = new L.LatLngBounds(new L.LatLng(minLat, minLon), new L.LatLng(maxLat, maxLon));
            map.fitBounds(tmpB);
        }
        routingLayer.addData({
            "type": "Feature",
            "geometry": {
                "type": "LineString",
                "coordinates": path.points
            }
        });
        
        if(path.errors) {
            document.getElementById("messages").innerText = "path calculation caused errors: " + path.errors;
        }
    }
});
