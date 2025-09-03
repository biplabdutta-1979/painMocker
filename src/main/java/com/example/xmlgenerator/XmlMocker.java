package com.example.xmlgenerator;

import org.apache.commons.lang3.RandomStringUtils;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.List;
import java.util.Arrays;

public class XmlMocker {
    private static final Random random = new Random();
    private static final List<String> CURRENCIES = Arrays.asList("USD", "EUR", "GBP", "JPY", "CAD");
    private static final List<String> SERVICES = Arrays.asList(
        "Web Design", "Consulting", "Software Development", "IT Support", "Training",
        "Maintenance", "Cloud Services", "Security Audit", "Mobile App Development", "Data Analysis"
    );
    private static final String[] FIRST_NAMES = {"James", "Mary", "Robert", "Patricia", "John", "Jennifer", 
        "Michael", "Linda", "David", "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", 
        "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson"};
    private static final String[] COMPANY_TYPES = {"Corp.", "Inc.", "LLC", "Ltd.", "GmbH", "AG", "Group", "Partners"};
    private static final String[] STREET_TYPES = {"St.", "Ave.", "Blvd.", "Rd.", "Ln.", "Dr.", "Ct.", "Pl."};
    private static final String[] CITIES = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", 
        "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
    private static final String[] STATES = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", 
        "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", 
        "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", 
        "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

    private static String generatePersonName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)] + " " + 
               LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    private static String generateCompanyName() {
        return RandomStringUtils.randomAlphabetic(5, 15) + " " + 
               COMPANY_TYPES[random.nextInt(COMPANY_TYPES.length)];
    }

    private static String generateStreetAddress() {
        return RandomStringUtils.randomNumeric(3) + " " + 
               RandomStringUtils.randomAlphabetic(5, 10).toLowerCase() + " " +
               STREET_TYPES[random.nextInt(STREET_TYPES.length)];
    }

    private static String generateCityStateZip() {
        return CITIES[random.nextInt(CITIES.length)] + ", " + 
               STATES[random.nextInt(STATES.length)] + " " + 
               RandomStringUtils.randomNumeric(5);
    }

    private static String generatePhoneNumber() {
        return String.format("+1-%03d-%03d-%04d",
            random.nextInt(1000),
            random.nextInt(1000),
            random.nextInt(10000));
    }

    private static String generateEmail(String name) {
        return name.toLowerCase().replace(" ", ".") + "@example.com";
    }

    private static String generateIBAN(String countryCode) {
        return countryCode + RandomStringUtils.randomNumeric(2) + 
               RandomStringUtils.randomAlphanumeric(1, 30); // Simplified IBAN
    }

    private static String generateBIC() {
        return RandomStringUtils.randomAlphabetic(4).toUpperCase() + 
               RandomStringUtils.randomAlphanumeric(2).toUpperCase() + "XX";
    }

    private static String randomAmount() {
        return String.format("%.2f", 100 + random.nextDouble() * 9900);
    }

    public static void generateMocks(String sampleXmlResource, String outputFolder, int count, int startIdx) {
        try {
            InputStream sampleStream = XmlMocker.class.getClassLoader().getResourceAsStream(sampleXmlResource);
            if (sampleStream == null) {
                System.out.println("Sample XML file not found: " + sampleXmlResource);
                return;
            }
            String sampleContent = new String(sampleStream.readAllBytes());
            Files.createDirectories(Paths.get(outputFolder));
            
            for (int i = 0; i < count; i++) {
                String msgId = "MSG" + RandomStringUtils.randomNumeric(10);
                String pmtInfId = "PMT" + RandomStringUtils.randomNumeric(10);
                String endToEndId = "E2E" + RandomStringUtils.randomAlphanumeric(10);
                String amount = randomAmount();
                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String execDate = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
                
                String mockContent = sampleContent
                    // Message and Payment IDs
                    .replaceAll(">MSG\\d+<", ">" + msgId + "<")
                    .replaceAll(">PMT\\d+<", ">" + pmtInfId + "<")
                    .replaceAll(">NOTPROVIDED<", ">" + endToEndId + "<")
                    
                    // Dates and times
                    .replaceAll(">\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}<", ">" + dateTime + "<")
                    .replaceAll(">\\d{4}-\\d{2}-\\d{2}<", ">" + execDate + "<")
                    
                    // Amounts and control sums
                    .replaceAll(">\\d+\\.\\d{2}<", ">" + amount + "<")
                    .replaceAll("Ccy=\"[A-Z]{3}\"", "Ccy=\"" + CURRENCIES.get(random.nextInt(CURRENCIES.size())) + "\"")
                    
                    // Debtor information
                    .replaceAll(">ABC Corporation<", ">" + generateCompanyName() + "<")
                    .replaceAll(">John Doe<", ">" + generatePersonName() + "<")
                    .replaceAll(">123 Main St<", ">" + generateStreetAddress() + "<")
                    .replaceAll(">New York, NY 10001<", ">" + generateCityStateZip() + "<")
                    .replaceAll(">\\+1-\\d{3}-\\d{3}-\\d{4}<", ">" + generatePhoneNumber() + "<")
                    .replaceAll(">[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}<", ">" + 
                        generateEmail(generatePersonName()) + "<")
                    
                    // Account and BIC numbers
                    .replaceAll(">DE\\d{20}<", ">" + generateIBAN("DE") + "<")
                    .replaceAll(">DEUTDEFF<", ">" + generateBIC() + "<")
                    .replaceAll(">CHASUS33<", ">" + generateBIC() + "<")
                    .replaceAll(">US\\d{20}<", ">" + generateIBAN("US") + "<")
                    
                    // Creditor information
                    .replaceAll(">Jane Smith<", ">" + generatePersonName() + "<")
                    .replaceAll(">456 Oak Ave<", ">" + generateStreetAddress() + "<")
                    .replaceAll(">Los Angeles, CA 90001<", ">" + generateCityStateZip() + "<")
                    
                    // Remittance info
                    .replaceAll(">Invoice \\d+ - .+?<", ">Invoice " + 
                        RandomStringUtils.randomNumeric(5) + " - " + 
                        SERVICES.get(random.nextInt(SERVICES.size())) + " Services<");
                
                String timestamp = String.valueOf(System.currentTimeMillis());
                Path outPath = Paths.get(outputFolder, "mock_" + (startIdx + i) + "_" + timestamp + ".xml");
                Files.write(outPath, mockContent.getBytes());
                System.out.println("Generated: " + outPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
