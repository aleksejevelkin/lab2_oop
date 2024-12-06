import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CityStatsApp {

    public static void main(String[] args) {
        CityStatsApp app = new CityStatsApp();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к файлу (или 'exit' для выхода):");

        while (true) {
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Выход из программы.");
                break;
            }

            Path filePath = Paths.get(input);
            if (!Files.exists(filePath)) {
                System.out.println("Файл не найден. Попробуйте снова.");
                continue;
            }

            try {
                long startTime = System.currentTimeMillis();
                List<CityRecord> records;

                if (filePath.toString().endsWith(".csv")) {
                    records = parseCsvFile(filePath);
                } else if (filePath.toString().endsWith(".xml")) {
                    records = parseXmlFile(filePath);
                } else {
                    System.out.println("Поддерживаются только файлы CSV и XML.");
                    continue;
                }

                CityStatistics stats = new CityStatistics(records);
                stats.printStatistics();
                long endTime = System.currentTimeMillis();
                System.out.println("Время обработки файла: " + (endTime - startTime) + " мс");
            } catch (Exception e) {
                System.out.println("Ошибка при обработке файла: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private List<CityRecord> parseCsvFile(Path filePath) throws IOException {
        List<CityRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            reader.readLine(); // Пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length < 4) continue;
                String cityName = parts[0].trim();
                String street = parts[1].trim();
                int house = Integer.parseInt(parts[2].trim());
                try {
                    int floors = Integer.parseInt(parts[3].trim());
                    records.add(new CityRecord(cityName, street, house, floors));
                } catch (NumberFormatException e) {
                    System.out.println("Некорректные данные в строке: " + Arrays.toString(parts));
                }
            }
        }
        return records;
    }

    private List<CityRecord> parseXmlFile(Path filePath) throws Exception {
        List<CityRecord> records = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(filePath.toFile());

        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("item");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String cityName = element.getAttribute("city");
                String street = element.getAttribute("street");
                int house = Integer.parseInt(element.getAttribute("house"));
                int floors = Integer.parseInt(element.getAttribute("floor"));

                records.add(new CityRecord(cityName, street, house, floors));
            }
        }

        return records;
    }

    private String[] parseCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                parts.add(currentPart.toString().trim());
                currentPart.setLength(0);
            } else {
                currentPart.append(c);
            }
        }
        parts.add(currentPart.toString().trim());
        return parts.toArray(new String[0]);
    }
}
