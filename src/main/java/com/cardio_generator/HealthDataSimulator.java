package com.cardio_generator;

import com.cardio_generator.generators.*;
import com.cardio_generator.outputs.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class HealthDataSimulator {

    private static HealthDataSimulator instance; // Singleton

    private static int patientCount = 50;
    private static ScheduledExecutorService scheduler;
    private static OutputStrategy outputStrategy = new ConsoleOutputStrategy(); // default
    private static final Random random = new Random();

    // Private constructor
    private HealthDataSimulator() {}

    // Singleton access
    public static synchronized HealthDataSimulator getInstance() {
        if (instance == null) {
            instance = new HealthDataSimulator();
        }
        return instance;
    }

    public void run(String[] args) throws IOException {
        parseArguments(args);
        scheduler = Executors.newScheduledThreadPool(patientCount * 4);

        List<Integer> patientIds = initializePatientIds(patientCount);
        Collections.shuffle(patientIds);
        scheduleTasksForPatients(patientIds);
    }

    private static void parseArguments(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                case "--patient-count":
                    if (i + 1 < args.length) {
                        try {
                            patientCount = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid patient count. Using default.");
                        }
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        String outputArg = args[++i];
                        if (outputArg.equals("console")) {
                            outputStrategy = new ConsoleOutputStrategy();
                        } else if (outputArg.startsWith("file:")) {
                            String dir = outputArg.substring(5);
                            Path path = Paths.get(dir);
                            if (!Files.exists(path)) Files.createDirectories(path);
                            outputStrategy = new FileOutputStrategy(dir);
                        } else if (outputArg.startsWith("websocket:")) {
                            int port = Integer.parseInt(outputArg.substring(10));
                            outputStrategy = new WebSocketOutputStrategy(port);
                        } else if (outputArg.startsWith("tcp:")) {
                            int port = Integer.parseInt(outputArg.substring(4));
                            outputStrategy = new TcpOutputStrategy(port);
                        }
                    }
                    break;
                default:
                    System.err.println("Unknown option " + args[i]);
                    printHelp();
                    System.exit(1);
            }
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("  -h                         Show help");
        System.out.println("  --patient-count <count>   Number of patients (default: 50)");
        System.out.println("  --output <type>           Output type: console, file:<dir>, websocket:<port>, tcp:<port>");
    }

    private static List<Integer> initializePatientIds(int count) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) ids.add(i);
        return ids;
    }

    private static void scheduleTasksForPatients(List<Integer> patientIds) {
        ECGDataGenerator ecg = new ECGDataGenerator(patientCount);
        BloodSaturationDataGenerator sat = new BloodSaturationDataGenerator(patientCount);
        BloodPressureDataGenerator bp = new BloodPressureDataGenerator(patientCount);
        BloodLevelsDataGenerator levels = new BloodLevelsDataGenerator(patientCount);
        AlertGenerator alert = new AlertGenerator(patientCount);

        for (int id : patientIds) {
            scheduleTask(() -> ecg.generate(id, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> sat.generate(id, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bp.generate(id, outputStrategy), 1, TimeUnit.MINUTES);
            scheduleTask(() -> levels.generate(id, outputStrategy), 2, TimeUnit.MINUTES);
            scheduleTask(() -> alert.generate(id, outputStrategy), 20, TimeUnit.SECONDS);
        }
    }

    private static void scheduleTask(Runnable task, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(task, random.nextInt(5), period, unit);
    }

    public static void main(String[] args) throws IOException {
        HealthDataSimulator.getInstance().run(args);
    }
}
