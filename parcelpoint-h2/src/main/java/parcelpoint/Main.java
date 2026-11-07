package parcelpoint;

import parcelpoint.cli.ParcelCommandLine;
import parcelpoint.service.ParcelService;
import parcelpoint.storage.InMemoryParcelRepository;
import parcelpoint.storage.ParcelRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParcelRepository repository = new InMemoryParcelRepository();
        ParcelService service = new ParcelService(repository);
        new ParcelCommandLine(service).run(new Scanner(System.in));
    }
}
