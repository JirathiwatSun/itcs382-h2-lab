package parcelpoint.cli;

import parcelpoint.domain.Locker;
import parcelpoint.domain.Parcel;
import parcelpoint.domain.ParcelSize;
import parcelpoint.service.ParcelService;

import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

public class ParcelCommandLine {
    private final ParcelService service;

    public ParcelCommandLine(ParcelService service) {
        this.service = service;
    }

    public void run(Scanner in) {
        System.out.println("ParcelPoint commands:");
        System.out.println("  store <code> <studentId> <SMALL|MEDIUM|LARGE>");
        System.out.println("  pickup <code> <days>");
        System.out.println("  find <code>");
        System.out.println("  staff");
        System.out.println("  quit");
        System.out.print("> ");
        while (in.hasNextLine()) {
            String[] words = in.nextLine().trim().split("\\s+");
            if (words[0].equals("quit")) {
                return;
            }
            handle(words);
            System.out.print("> ");
        }
    }

    private void handle(String[] words) {
        switch (words[0]) {
            case "store" -> store(words);
            case "pickup" -> pickUp(words);
            case "find" -> find(words);
            case "staff" -> listAll();
            default -> System.out.println("Unknown command: " + words[0]);
        }
    }

    private void store(String[] words) {
        if (words.length != 4) {
            System.out.println("Usage: store <code> <studentId> <SMALL|MEDIUM|LARGE>");
            return;
        }
        ParcelSize size;
        try {
            size = ParcelSize.valueOf(words[3].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            System.out.println("Size must be SMALL, MEDIUM or LARGE");
            return;
        }
        Optional<Locker> locker = service.store(new Parcel(words[1], words[2], size));
        if (locker.isPresent()) {
            System.out.println("Parcel " + words[1] + " is in locker " + locker.get().getNumber());
        } else {
            System.out.println("No free locker for a " + size + " parcel");
        }
    }

    private void pickUp(String[] words) {
        if (words.length != 3) {
            System.out.println("Usage: pickup <code> <days>");
            return;
        }
        int days;
        try {
            days = Integer.parseInt(words[2]);
        } catch (NumberFormatException e) {
            System.out.println("Days must be a whole number, such as 2");
            return;
        }
        Optional<Double> fee = service.pickUp(words[1], days);
        if (fee.isPresent()) {
            System.out.println("Parcel " + words[1] + " picked up. Fee: " + fee.get() + " baht");
        } else {
            System.out.println("No waiting parcel with code " + words[1]);
        }
    }

    private void find(String[] words) {
        if (words.length != 2) {
            System.out.println("Usage: find <code>");
            return;
        }
        Optional<Parcel> parcel = service.find(words[1]);
        System.out.println(parcel.map(ParcelCommandLine::describe)
                .orElse("No parcel with code " + words[1]));
    }

    private void listAll() {
        for (Parcel parcel : service.allParcels()) {
            System.out.println(describe(parcel));
        }
        System.out.println("Free lockers: " + service.countFreeLockers());
    }

    private static String describe(Parcel parcel) {
        String where = parcel.getLockerNumber() == null ? "" : ", locker " + parcel.getLockerNumber();
        return parcel.getCode() + ", " + parcel.getStudentId() + ", " + parcel.getSize() + ", "
                + parcel.getStatus() + where;
    }
}
