package mission1;

import java.util.Scanner;

public class Assemble {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private static final int CarType_Q      = 0;
    private static final int Engine_Q       = 1;
    private static final int BrakeSystem_Q  = 2;
    private static final int SteeringSystem_Q = 3;
    private static final int Run_Test       = 4;

    private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    private static final int GM = 1, TOYOTA = 2, WIA = 3;
    private static final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
    private static final int BOSCH_S = 1, MOBIS = 2;

    private static int[] carInfo = new int[5];

    private static final int goBackStep = 0;
    private static String[] carTypes = {"", "Sedan", "SUV", "Truck"};
    private static String[] engineFactory = {"", "GM", "TOYOTA", "WIA"};
    private static String[] brakeFactory = {"", "MANDO", "CONTINENTAL", "BOSCH"};
    private static String[] steeringFactory = {"", "BOSCH", "MOBIS"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int step = CarType_Q;

        while (true) {
            System.out.print(CLEAR_SCREEN);
            System.out.flush();

            String buf = selectMenu(step, sc);
            if (buf == null) break;

            int answer = isValidMenu(step, buf);
            if (answer == -1) {
                continue; // Invalid input, re-prompt
            }
            if (answer == goBackStep) {
                step = setStep(step);
                continue;
            }

            step = selectItem(step, answer);
        }

        sc.close();
    }

    private static String selectMenu(int step, Scanner sc) {
        showMenu(step);
        String buf = sc.nextLine().trim();

        if (buf.equalsIgnoreCase("exit")) {
            System.out.println("bye");
            return null;
        }
        return buf;
    }
    private static int isValidMenu(int step, String buf) {
        int answer = -1;
        try {
            answer = Integer.parseInt(buf);
        } catch (NumberFormatException e) {
            System.out.println("ERROR :: only number input");
            delay(800);
            return answer;
        }
        if (!isValidRange(step, answer)) {
            delay(800);
            return -1;
        }
        return answer;
    }
    private static void showMenu(int step) {
        switch (step) {
            case CarType_Q:
                showCarTypeMenu(); break;
            case Engine_Q:
                showEngineMenu(); break;
            case BrakeSystem_Q:
                showBrakeMenu(); break;
            case SteeringSystem_Q:
                showSteeringMenu(); break;
            case Run_Test:
                showRunTestMenu(); break;
        }
        System.out.print("INPUT > ");
    }
    private static int setStep(int step) {
        if (step == Run_Test) {
            step = CarType_Q;
        } else if (step > CarType_Q) {
            step--;
        }
        return step;
    }
    private static int selectItem(int step, int answer) {
        switch (step) {
            case CarType_Q:
                selectCarType(answer);
                delay(800);
                step = Engine_Q;
                break;
            case Engine_Q:
                selectEngine(answer);
                delay(800);
                step = BrakeSystem_Q;
                break;
            case BrakeSystem_Q:
                selectBrakeSystem(answer);
                delay(800);
                step = SteeringSystem_Q;
                break;
            case SteeringSystem_Q:
                selectSteeringSystem(answer);
                delay(800);
                step = Run_Test;
                break;
            case Run_Test:
                if (answer == 1) {
                    runProducedCar();
                    delay(2000);
                } else if (answer == 2) {
                    System.out.println("Test...");
                    delay(1500);
                    testProducedCar();
                    delay(2000);
                }
                break;
        }
        return step;
    }

    private static void showCarTypeMenu() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        System.out.println("  ____/_|_____________|____");
        System.out.println(" |                      O  |");
        System.out.println(" '-(@)----------------(@)--'");
        System.out.println("===============================");
        System.out.println("what car type?");
        System.out.println("1. Sedan");
        System.out.println("2. SUV");
        System.out.println("3. Truck");
        System.out.println("===============================");
    }
    private static void showEngineMenu() {
        System.out.println("what engine?");
        System.out.println("0. go back");
        System.out.println("1. GM");
        System.out.println("2. TOYOTA");
        System.out.println("3. WIA");
        System.out.println("4. brake engine");
        System.out.println("===============================");
    }
    private static void showBrakeMenu() {
        System.out.println("what brake?");
        System.out.println("0. go back");
        System.out.println("1. MANDO");
        System.out.println("2. CONTINENTAL");
        System.out.println("3. BOSCH");
        System.out.println("===============================");
    }
    private static void showSteeringMenu() {
        System.out.println("what steering?");
        System.out.println("0. go back");
        System.out.println("1. BOSCH");
        System.out.println("2. MOBIS");
        System.out.println("===============================");
    }
    private static void showRunTestMenu() {
        System.out.println("good car is produced.");
        System.out.println("what next job?");
        System.out.println("0. go home");
        System.out.println("1. RUN");
        System.out.println("2. Test");
        System.out.println("===============================");
    }

    private static boolean isValidRange(int step, int ans) {
        switch (step) {
            case CarType_Q:
                if (ans < 1 || ans > 3) {
                    System.out.println("ERROR :: car type has range 1 ~ 3");
                    return false;
                }
                break;
            case Engine_Q:
                if (ans < 0 || ans > 4) {
                    System.out.println("ERROR :: engine range 1 ~ 4");
                    return false;
                }
                break;
            case BrakeSystem_Q:
                if (ans < 0 || ans > 3) {
                    System.out.println("ERROR :: brake range 1 ~ 3 ");
                    return false;
                }
                break;
            case SteeringSystem_Q:
                if (ans < 0 || ans > 2) {
                    System.out.println("ERROR :: steering range 1 ~ 2");
                    return false;
                }
                break;
            case Run_Test:
                if (ans < 0 || ans > 2) {
                    System.out.println("ERROR :: Run or Test choice");
                    return false;
                }
                break;
        }
        return true;
    }

    private static void selectCarType(int a) {
        carInfo[CarType_Q] = a;
        String name = (a >= 1 && a <= carTypes.length - 1) ? carTypes[a] : "no type";
        System.out.printf("cartype %s choice.\n", name);
    }
    private static void selectEngine(int a) {
        carInfo[Engine_Q] = a;
        String name = (a >= 1 && a <= engineFactory.length - 1) ? engineFactory[a] : "brake engine";
        System.out.printf("%s engine choice.\n", name);
    }
    private static void selectBrakeSystem(int a) {
        carInfo[BrakeSystem_Q] = a;
        String name = (a >= 1 && a <= brakeFactory.length - 1) ? brakeFactory[a] : "no factory";
        System.out.printf("%s brake choice.\n", name);
    }
    private static void selectSteeringSystem(int a) {
        carInfo[SteeringSystem_Q] = a;
        String name =(a >= 1 && a <= steeringFactory.length - 1) ? steeringFactory[a] : "no factory";
        System.out.printf("%s steering choice.\n", name);
    }
    private static boolean isValidRunCheck() {
        if (isValidSedan()) return false;
        if (isValidSuv())       return false;
        if (isValidTruckEngine())          return false;
        if (isValidTruckBrake())  return false;
        if (isValidBosch()) return false;
        return true;
    }

    private static boolean isValidBosch() {
        return carInfo[BrakeSystem_Q] == BOSCH_B && carInfo[SteeringSystem_Q] != BOSCH_S;
    }
    private static boolean isValidTruckBrake() {
        return carInfo[CarType_Q] == TRUCK && carInfo[BrakeSystem_Q] == MANDO;
    }
    private static boolean isValidTruckEngine() {
        return carInfo[CarType_Q] == TRUCK && carInfo[Engine_Q] == WIA;
    }
    private static boolean isValidSuv() {
        return carInfo[CarType_Q] == SUV && carInfo[Engine_Q] == TOYOTA;
    }
    private static boolean isValidSedan() {
        return carInfo[CarType_Q] == SEDAN && carInfo[BrakeSystem_Q] == CONTINENTAL;
    }

    private static void runProducedCar() {
        if (!isValidRunCheck()) {
            System.out.println("car is not work");
            return;
        }
        if (carInfo[Engine_Q] == 4) {
            System.out.println("engine is not work.");
            System.out.println("car is not work.");
            return;
        }

        System.out.printf("Car Type : %s\n", carTypes[carInfo[CarType_Q]]);
        System.out.printf("Engine   : %s\n", engineFactory[carInfo[Engine_Q]]);
        System.out.printf("Brake    : %s\n", brakeFactory[carInfo[BrakeSystem_Q]]);
        System.out.printf("Steering : %s\n", steeringFactory[carInfo[SteeringSystem_Q]]);
        System.out.println("car is work.");
    }

    private static void testProducedCar() {
        if (isValidSedan()) {
            fail("Sedan - Continental brake not avaliable");
        } else if (isValidSuv()) {
            fail("SUV - TOYOTA engine not avaliable");
        } else if (isValidTruckEngine()) {
            fail("Truck - WIA engine not avaliable");
        } else if (isValidTruckBrake()) {
            fail("Truck - Mando brake not avaliable");
        } else if (isValidBosch()) {
            fail("Bosch brake - Bosch steering only use");
        } else {
            System.out.println("car assemble test : PASS");
        }
    }
    private static void fail(String msg) {
        System.out.println("car assemble test : FAIL");
        System.out.println(msg);
    }
    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}