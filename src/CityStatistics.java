import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CityStatistics {
    private final List<CityRecord> records;

    public CityStatistics(List<CityRecord> records) {
        this.records = records;
    }

    public void printStatistics() {
        printDuplicates();
        printFloorStatisticsByCity();
    }

    private void printDuplicates() {
        System.out.println("Дублирующиеся записи:");
        Map<CityRecord, Long> duplicates = records.stream()
                .collect(Collectors.groupingBy(record -> record, Collectors.counting()));
        duplicates.forEach((record, count) -> {
            if (count > 1) {
                System.out.println(record + " - Повторений: " + count);
            }
        });
    }

    private void printFloorStatisticsByCity() {
        System.out.println("Статистика по этажности для каждого города:");
        Map<String, List<CityRecord>> recordsByCity = records.stream()
                .collect(Collectors.groupingBy(CityRecord::getCityName));

        for (Map.Entry<String, List<CityRecord>> entry : recordsByCity.entrySet()) {
            String city = entry.getKey();
            List<CityRecord> cityRecords = entry.getValue();
            Map<Integer, Long> floorStats = cityRecords.stream()
                    .collect(Collectors.groupingBy(CityRecord::getFloors, Collectors.counting()));

            if (floorStats.isEmpty()) {
                System.out.println("Нет данных по этажам для города: " + city);
            } else {
                System.out.println("Город: " + city);
                floorStats.forEach((floors, count) ->
                        System.out.println(floors + "-этажных зданий: " + count));
            }
        }
    }
} 