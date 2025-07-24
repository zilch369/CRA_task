package mission2;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Assemble {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private static final int CarType_Q      = 0;
    private static int runtest;

    private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    private static final int GM = 1, TOYOTA = 2, WIA = 3;
    private static final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
    private static final int BOSCH_S = 1, MOBIS = 2;

    private static int[] carInfo = new int[5];
    static final int goBackStep = 0;

    private static String[] carTypes = {"", "Sedan", "SUV", "Truck"};
    static final List<PartMenu> partMenus = List.of(
            new PartMenu("engine", new String[]{"", "GM", "TOYOTA", "WIA"}),
            new PartMenu("brake", new String[]{"", "MANDO", "CONTINENTAL", "BOSCH"}),
            new PartMenu("steering", new String[]{"", "BOSCH", "MOBIS"})
    );

    public static void setRun_Test(int run_Test) {
        runtest = run_Test;
    }

    public static Assemble getInstance() {
        return new Assemble();
    }

    public static void main(String[] args) {
        Assemble.getInstance().run();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        int step = CarType_Q;
        runtest = partMenus.size() + 1;
        while (true) {
            System.out.print(CLEAR_SCREEN);
            System.out.flush();
            String buf = selectMenu(step, sc);
            if (buf == null) break;
            int answer = isValidMenu(step, buf);
            if (answer == -1) continue;
            if (answer == 0) { step = setStep(step); continue; }
            step = selectItem(step, answer);
        }
        sc.close();
    }

    public static String selectMenu(int step, Scanner sc) {
        showMenu(step);
        String buf = sc.nextLine().trim();

        if (buf.equalsIgnoreCase("exit")) {
            System.out.println("bye");
            return null;
        }
        return buf;
    }
    public static int isValidMenu(int step, String buf) {
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
    public static void showMenu(int step) {
        if (step == CarType_Q) {
            showCarTypeMenu();
        } else if (step >= 1 && step <= partMenus.size()) {
            int partIndex = step - 1;
            partMenus.get(partIndex).show();
        } else if (step == runtest) {
            showRunTestMenu();
        }
        System.out.print("INPUT > ");
    }
    public static int setStep(int step) {
        if (step == runtest) {
            step = CarType_Q;
        } else if (step > CarType_Q) {
            step--;
        }
        return step;
    }
    public static int selectItem(int step, int answer) {

        if (step == runtest) {
            if (answer == 1) {
                runProducedCar();
                delay(2000);
            } else if (answer == 2) {
                System.out.println("Test...");
                delay(1500);
                testProducedCar();
                delay(2000);
            }

        } else if (step == CarType_Q) {
            selectCarType(answer);
            delay(800);
            step++;
        } else {
            selectPart(step,answer);
            delay(800);
            step++;
        }
        return step;
    }

    public static void showCarTypeMenu() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        System.out.println("  ____/_|_____________|____");
        System.out.println(" |                      O  |");
        System.out.println(" '-(@)----------------(@)--'");
        System.out.println("===============================");
        System.out.println("what car type?");
        for (int i = 1; i < carTypes.length; i++) {
            System.out.printf("%d. %s\n", i, carTypes[i]);
        }
        System.out.println("===============================");
    }
    public static void showRunTestMenu() {
        System.out.println("good car is produced.");
        System.out.println("what next job?");
        System.out.println("0. go home");
        System.out.println("1. RUN");
        System.out.println("2. Test");
        System.out.println("===============================");
    }

    public static boolean isValidRange(int step, int ans) {
        if (step == CarType_Q) {
            if (ans < 1 || ans > 3) {
                System.out.println("ERROR :: car type has range 1 ~ 3");
                return false;
            }
        }
        if (step >= 1 && step <= partMenus.size()) {
            if (ans < 0 || ans > partMenus.get(step - 1).options.length-1) {
                System.out.printf("ERROR :: %s range 1 ~ %d\n", partMenus.get(step - 1).name, partMenus.get(step - 1).options.length -1);
                return false;
            }
        }
        if (step == runtest) {
            if (ans < 0 || ans > 2) {
                System.out.println("ERROR :: Run or Test choice");
                return false;
            }
        }
        return true;
    }

    public static void selectCarType(int a) {
        carInfo[CarType_Q] = a;
        String name = (a >= 1 && a <= carTypes.length - 1) ? carTypes[a] : "no type";
        System.out.printf("cartype %s choice.\n", name);
    }
    public static void selectPart(int partIndex, int a) {
        carInfo[partIndex] = a;
        String optionName = (a >= 1 && a <= partMenus.get(partIndex-1).options.length - 1) ? partMenus.get(partIndex-1).options[a] : "break part";
        System.out.printf("%s %s choice.\n", optionName, partMenus.get(partIndex-1).name);
    }

    public static boolean isValidRunCheck() {
        if (isValidSedan()) return false;
        if (isValidSuv())       return false;
        if (isValidTruckEngine())          return false;
        if (isValidTruckBrake())  return false;
        if (isValidBosch()) return false;
        return true;
    }

    public static boolean isValidBosch() {
        return carInfo[2] == BOSCH_B && carInfo[3] != BOSCH_S;
    }
    public static boolean isValidTruckBrake() {
        return carInfo[CarType_Q] == TRUCK && carInfo[2] == MANDO;
    }
    public static boolean isValidTruckEngine() {
        return carInfo[CarType_Q] == TRUCK && carInfo[1] == WIA;
    }
    public static boolean isValidSuv() {
        return carInfo[CarType_Q] == SUV && carInfo[1] == TOYOTA;
    }
    public static boolean isValidSedan() {
        return carInfo[CarType_Q] == SEDAN && carInfo[2] == CONTINENTAL;
    }

    public static void runProducedCar() {
        if (!isValidRunCheck()) {
            System.out.println("car is not work");
            return;
        }
        if (carInfo[1] == 4) {
            System.out.println("engine is not work.");
            System.out.println("car is not work.");
            return;
        }

        System.out.printf("Car Type : %s\n", carTypes[carInfo[CarType_Q]]);
        for (int i = 0; i < partMenus.size(); i++) {
            System.out.printf("%s   : %s\n", partMenus.get(i).name, partMenus.get(i).options[carInfo[i + 1]]);
        }
//        System.out.printf("%s   : %s\n", partMenus.get(0).name, partMenus.get(0).options[carInfo[Engine_Q]]);
//        System.out.printf("Brake    : %s\n", brakeFactory[carInfo[BrakeSystem_Q]]);
//        System.out.printf("Steering : %s\n", steeringFactory[carInfo[SteeringSystem_Q]]);
        System.out.println("car is work.");
    }

    public static void testProducedCar() {
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
    public static void fail(String msg) {
        System.out.println("car assemble test : FAIL");
        System.out.println(msg);
    }
    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}

class PartMenu {
    final String name;
    final String[] options;

    public PartMenu(String name, String[] options) {
        this.name = name;
        this.options = options;
    }

    public void show() {
        System.out.printf("what %s?\n", name);
        System.out.println("0. go back");
        for (int i = 1; i < options.length; i++) {
            System.out.printf("%d. %s\n", i, options[i]);
        }
        System.out.println("===============================");
    }
}