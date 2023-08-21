
package contacts;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static PhoneBook phoneBook;
    private static String filename;

    public static void main(String[] args) {
        if (args.length > 0) {
            filename = args[0];
            phoneBook = PhoneBook.loadFromFile(filename);
        } else {
            phoneBook = new PhoneBook();
        }

        while (true) {
            System.out.println("[menu] Enter action (add, list, search, count, exit): ");
            String action = scanner.nextLine().trim();
            System.out.println();

            if (isNumeric(action)) {
                int index = Integer.parseInt(action) - 1;
                if (index >= 0 && index < phoneBook.getSize()) {
                    System.out.println(phoneBook.get(index).toString());
                } else {
                    System.out.println("Invalid record number.");
                }
                System.out.println();
                continue;
            }

            switch (action) {
                case "add" -> addContact();
                case "remove" -> removeContact();
                case "edit" -> editContact();
                case "count" -> {
                    System.out.println("The Phone Book has " + phoneBook.getSize() + " records.");
                    System.out.println();
                }
                case "list" -> listContacts();
                case "search" -> searchContacts();
                case "exit" -> {
                    if (filename != null) {
                        phoneBook.saveToFile(filename);
                    }
                    return;
                }
                default -> {
                    System.out.println("Unknown action. Please choose from: add, list, search, count, exit");
                    System.out.println();
                }
            }
        }
    }


    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void addContact() {
        System.out.print("Enter the type (person, organization): ");
        String type = scanner.nextLine().trim();

        if ("person".equalsIgnoreCase(type)) {
            System.out.print("Enter the name: ");
            String name = scanner.nextLine();

            System.out.print("Enter the surname: ");
            String surname = scanner.nextLine();

            System.out.print("Enter the birth date (or press Enter to skip): ");
            String birthDate = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (!birthDate.isEmpty()) {
                try {
                    LocalDateTime.parse(birthDate, formatter);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.print("Incorrect birth date format! Enter again (or press Enter to skip): ");
                    birthDate = scanner.nextLine();
                }
            }

            System.out.print("Enter the gender (M, F) or press Enter to skip: ");
            String gender = scanner.nextLine();
            while (!gender.isEmpty()) {
                if ("M".equalsIgnoreCase(gender) || "F".equalsIgnoreCase(gender)) {
                    break;
                } else {
                    System.out.print("Incorrect gender! Enter M or F (or press Enter to skip): ");
                    gender = scanner.nextLine();
                }
            }

            System.out.print("Enter the number: ");
            String number = scanner.nextLine();
            if (Record.isInvalidPhoneNumber(number)) {
                System.out.println("Wrong number format!");
                number = "";
            }

            Person person = new Person(name, surname, number);
            person.setBirthDate(birthDate);
            person.setGender(gender);

            phoneBook.add(person);

        } else if ("organization".equalsIgnoreCase(type)) {
            System.out.print("Enter the organization name: ");
            String orgName = scanner.nextLine();

            System.out.print("Enter the address: ");
            String address = scanner.nextLine();

            System.out.print("Enter the number: ");
            String number = scanner.nextLine();

            if (Record.isInvalidPhoneNumber(number)) {
                System.out.println("Wrong number format!");
                number = "";
            }

            Organization organization = new Organization(orgName, address, number);
            phoneBook.add(organization);
        } else {
            System.out.println("Unknown type.");
        }

        if (filename != null) {
            phoneBook.saveToFile(filename);
        }
        System.out.println("The record added.");
    }


    private static void removeContact() {
        if (phoneBook.getSize() == 0) {
            System.out.println("No records to remove!");
            return;
        }

        listContacts();
        System.out.print("Select a record: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        phoneBook.remove(index);
        System.out.println("The record removed!");
        if (filename != null) {
            phoneBook.saveToFile(filename);
        }
    }

    private static void editContact() {
        if (phoneBook.getSize() == 0) {
            System.out.println("No records to edit!");
            return;
        }

        listContacts();
        System.out.print("Select a record to edit by number: ");
        String input = scanner.nextLine().trim();
        int index;

        if (!isNumeric(input)) {
            System.out.println("Invalid input. Please enter a valid record number.");
            return;
        }

        index = Integer.parseInt(input) - 1;

        if (index < 0 || index >= phoneBook.getSize()) {
            System.out.println("Invalid record number.");
            return;
        }

        Record record = phoneBook.get(index);
        while (true) {
            System.out.print("Select a field (" + String.join(", ", record.getFieldNames()) + "): ");
            String field = scanner.nextLine().trim();

            if (record.getFieldNames().contains(field)) {
                System.out.print("Enter " + field + ": ");
                String value = scanner.nextLine();
                record.setFieldValue(field, value);
                record.updateLastEditTime();
                System.out.println("Saved");
                System.out.println(record);
                break;
            } else {
                System.out.println("Invalid field! Please choose from: " + String.join(", ", record.getFieldNames()));
            }
        }
    }

    private static void listContacts() {
        List<Record> allRecords = phoneBook.getAllRecords();
        if (allRecords.isEmpty()) {
            System.out.println("[no entries]");
            return;
        }
        for (int i = 0; i < allRecords.size(); i++) {
            System.out.println((i + 1) + ". " + allRecords.get(i).getFullName());
        }
        System.out.print("Select a record: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index >= 0 && index < phoneBook.getSize()) {
            Record record = phoneBook.get(index);
            System.out.println(record);
            label:
            while (true) {
                System.out.println("[record] Enter action (edit, delete, menu): ");
                String action = scanner.nextLine().trim();
                switch (action) {
                    case "edit":
                        editRecord(record);
                        break label;
                    case "delete":
                        phoneBook.remove(index);
                        System.out.println("Record deleted!");
                        break label;
                    case "menu":
                        break label;
                    default:
                        System.out.println("Unknown action.\n");
                        break;
                }
            }
        } else {
            System.out.println("Invalid record number.");
        }
    }

    private static void searchContacts() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        List<Record> results = phoneBook.search(query);

        System.out.println("Found " + results.size() + " results:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getFullName());
        }

        System.out.println();

        while (true) {
            System.out.println("[search] Enter action ([number], back, again): ");
            String action = scanner.nextLine().trim();

            if (action.equals("back")) {
                break;
            } else if (action.equals("again")) {
                searchContacts();
                break;
            } else {
                try {
                    int index = Integer.parseInt(action) - 1;
                    if (index >= 0 && index < results.size()) {
                        System.out.println(results.get(index));
                        System.out.println();

                        label:
                        while (true) {
                            System.out.println("[record] Enter action (edit, delete, menu): ");
                            String recordAction = scanner.nextLine().trim();

                            switch (recordAction) {
                                case "edit":
                                    editRecord(results.get(index));
                                    break label;
                                case "delete":
                                    phoneBook.remove(phoneBook.getAllRecords().indexOf(results.get(index)));
                                    break label;
                                case "menu":
                                    return;
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Not a number, loop again for input
                }
            }
        }
    }

    private static void editRecord(Record record) {
        while (true) {
            System.out.println("Select a field (" + String.join(", ", record.getFieldNames()) + "): ");
            String field = scanner.nextLine().trim();

            if (record.getFieldNames().contains(field)) {
                System.out.print("Enter " + field + ": ");
                String value = scanner.nextLine();
                record.setFieldValue(field, value);
                record.updateLastEditTime();

                System.out.println("Saved");
                System.out.println(record);

                if (filename != null) {
                    phoneBook.saveToFile(filename);
                }
                break;
            } else {
                System.out.println("Invalid field!");
            }
        }
    }

}


class PhoneBook implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<Record> records;

    public PhoneBook() {
        this.records = new ArrayList<>();
    }

    public List<Record> search(String query) {
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return records.stream()
                .filter(record -> pattern.matcher(record.toString()).find())
                .collect(Collectors.toList());
    }

    public void add(Record record) {
        this.records.add(record);
    }

    public void remove(int index) {
        if (index >= 0 && index < records.size()) {
            records.remove(index);
        }
    }

    public Record get(int index) {
        return records.get(index);
    }

    public int getSize() {
        return records.size();
    }

    public List<Record> getAllRecords() {
        return new ArrayList<>(records);
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public static PhoneBook loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (PhoneBook) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading from file: " + e.getMessage());
            return new PhoneBook();
        }
    }
}

abstract class Record {
    private String phoneNumber;
    private final LocalDateTime timeCreated;
    private LocalDateTime lastEditTime;

    public Record(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.timeCreated = LocalDateTime.now();
        this.lastEditTime = LocalDateTime.now();
    }

    public String getPhoneNumber() {
        return phoneNumber.isEmpty() ? "[no number]" : phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateLastEditTime();
    }

    protected void updateLastEditTime() {
        this.lastEditTime = LocalDateTime.now();
    }

    public String getTimeCreated() {
        return timeCreated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public String getLastEditTime() {
        return lastEditTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public static boolean isInvalidPhoneNumber(String number) {
        String regex = "\\+?(\\([0-9a-zA-Z]+\\)|[0-9a-zA-Z]+([ -][(][0-9a-zA-Z]{2,}[)])?)([ -][0-9a-zA-Z]{2,})*";
        return !number.matches(regex) || number.substring(1).contains("+");
    }

    public abstract List<String> getFieldNames();

    public abstract String getFieldValue(String field);

    public abstract void setFieldValue(String field, String value);

    public abstract String getFullName();

    @Override
    public abstract String toString();
}

class Person extends Record {
    private String name;
    private String surname;
    private String birthDate = "";
    private String gender = "";

    public Person(String name, String surname, String phoneNumber) {
        super(phoneNumber);
        this.name = name;
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public List<String> getFieldNames() {
        return Arrays.asList("name", "surname", "birthDate", "gender", "phoneNumber");
    }

    @Override
    public String getFieldValue(String field) {
        return switch (field) {
            case "name" -> name;
            case "surname" -> surname;
            case "birthDate" -> birthDate;
            case "gender" -> gender;
            case "phoneNumber" -> getPhoneNumber();
            default -> null;
        };
    }

    @Override
    public void setFieldValue(String field, String value) {
        switch (field) {
            case "name" -> this.name = value;
            case "surname" -> this.surname = value;
            case "birthDate" -> this.birthDate = value;
            case "gender" -> this.gender = value;
            case "phoneNumber" -> setPhoneNumber(value);
        }
    }


    @Override
    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Surname: " + surname + "\n" +
                "Birth date: " + (birthDate.isEmpty() ? "[no data]" : birthDate) + "\n" +
                "Gender: " + (gender.isEmpty() ? "[no data]" : gender) + "\n" +
                "Number: " + getPhoneNumber() + "\n" +
                "Time created: " + getTimeCreated() + "\n" +
                "Time last edit: " + getLastEditTime();
    }
}

class Organization extends Record {
    private String orgName;
    private String address;

    public Organization(String orgName, String address, String phoneNumber) {
        super(phoneNumber);
        this.orgName = orgName;
        this.address = address;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public List<String> getFieldNames() {
        return Arrays.asList("orgName", "address", "phoneNumber");
    }

    @Override
    public String getFieldValue(String field) {
        return switch (field) {
            case "orgName" -> orgName;
            case "address" -> address;
            case "phoneNumber" -> getPhoneNumber();
            default -> null;
        };
    }

    @Override
    public void setFieldValue(String field, String value) {
        switch (field) {
            case "orgName" -> this.orgName = value;
            case "address" -> this.address = value;
            case "phoneNumber" -> setPhoneNumber(value);
        }
    }


    @Override
    public String getFullName() {
        return orgName;
    }

    @Override
    public String toString() {
        return "Organization name: " + orgName + "\n" +
                "Address: " + address + "\n" +
                "Number: " + getPhoneNumber() + "\n" +
                "Time created: " + getTimeCreated() + "\n" +
                "Time last edit: " + getLastEditTime();
    }
}
