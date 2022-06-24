package at.fhtw.disys.DatraCollectionDispatcher;

public class Station {
    private String id;
    private String station_id;
    private boolean available;
    private float latitude;
    private float longitude;

    public Station(String id, String station_id, boolean available, float latitude, float longitude) {
        this.id = id;
        this.station_id = station_id;
        this.available = available;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStationId() {
        return station_id;
    }

}
