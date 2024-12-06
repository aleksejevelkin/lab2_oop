import java.util.Objects;

public class CityRecord {
    private final String cityName;
    private final String street;
    private final int house;
    private final int floors;

    public CityRecord(String cityName, String street, int house, int floors) {
        this.cityName = cityName;
        this.street = street;
        this.house = house;
        this.floors = floors;
    }

    public String getCityName() {
        return cityName;
    }

    public String getStreet() {
        return street;
    }

    public int getHouse() {
        return house;
    }

    public int getFloors() {
        return floors;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CityRecord that = (CityRecord) obj;
        return house == that.house &&
                floors == that.floors &&
                Objects.equals(cityName, that.cityName) &&
                Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, street, house, floors);
    }

    @Override
    public String toString() {
        return "City: " + cityName + ", Street: " + street + ", House: " + house + ", Floors: " + floors;
    }
} 