package mission2;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import java.io.*;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AssembleTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    Assemble assemble;

    @BeforeEach
    void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        assemble = mock(Assemble.class);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testMain_normalFlow() {
        String simulatedInput = "1\n1\n1\n1\n1\nexit\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Assemble.main(new String[]{});

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = outContent.toString();
        assertTrue(output.contains("car is work"));
    }

    @Test
    void testSelectCarType() {
        assemble.selectCarType(1);
        assertTrue(outContent.toString().contains("Sedan"));
        outContent.reset();
        assemble.selectCarType(99);
        assertTrue(outContent.toString().contains("no type"));
    }

    @Test
    void testSelectEngine() {
        assemble.selectPart(1, 2);
        assertTrue(outContent.toString().contains("TOYOTA"));
        outContent.reset();
        assemble.selectPart(1, 99);
        assertTrue(outContent.toString().contains("break part"));
    }

    @Test
    void testSelectBrakeSystem() {
//        assemble.selectBrakeSystem(3);
        assemble.selectPart(2, 3);
        assertTrue(outContent.toString().contains("BOSCH"));
        outContent.reset();
//        assemble.selectBrakeSystem(99);
        assemble.selectPart(2, 99);
        assertTrue(outContent.toString().contains("break part"));
    }

    @Test
    void testSelectSteeringSystem() {
//        assemble.selectSteeringSystem(2);
        assemble.selectPart(3, 2);
        assertTrue(outContent.toString().contains("MOBIS"));
        outContent.reset();
//        assemble.selectSteeringSystem(99);
        assemble.selectPart(3, 99);
        assertTrue(outContent.toString().contains("break part"));
    }

    @Test
    void testIsValidRange() {
        assemble.setRun_Test(4);
        assertFalse(assemble.isValidRange(0, 0));
        assertFalse(assemble.isValidRange(1, 5));
        assertFalse(assemble.isValidRange(2, 4));
        assertFalse(assemble.isValidRange(3, 3));
        assertFalse(assemble.isValidRange(4, 3));
        assertTrue(assemble.isValidRange(0, 1));
    }

    @Test
    void testSetStep() {
        assertEquals(0, assemble.setStep(4));
        assertEquals(2, assemble.setStep(3));
        assertEquals(0, assemble.setStep(0));
    }

    @Test
    void testGoBackStep() {
        int step = 2; // 예: 엔진 선택 단계
        int answer = 0; // goBackStep
        if (answer == Assemble.goBackStep) {
            step = Assemble.setStep(step);
        }
        assertEquals(1, step); // 이전 단계로 이동했는지 확인
    }

    @Test
    void testIsValidMenu() {
        assertEquals(-1, assemble.isValidMenu(0, "abc"));
        assertEquals(-1, assemble.isValidMenu(0, "5"));
        assertEquals(1, assemble.isValidMenu(0, "1"));
    }

    @Test
    void testSelectItem() {
        int next = assemble.selectItem(0, 1);
        assertEquals(1, next);
        next = assemble.selectItem(1, 1);
        assertEquals(2, next);
        next = assemble.selectItem(2, 1);
        assertEquals(3, next);
        next = assemble.selectItem(3, 1);
        assertEquals(4, next);
        next = assemble.selectItem(4, 1);
        assertEquals(4, next);
        next = assemble.selectItem(4, 2);
        assertEquals(4, next);
    }

    @Test
    void testShowMenus() {

        assemble.showCarTypeMenu();
        assertTrue(outContent.toString().contains("what car type?"));
        outContent.reset();
        assemble.partMenus.get(0).show();;
        assertTrue(outContent.toString().contains("what engine?"));
        outContent.reset();
        assemble.partMenus.get(1).show();
        assertTrue(outContent.toString().contains("what brake?"));
        outContent.reset();
        assemble.partMenus.get(2).show();
        assertTrue(outContent.toString().contains("what steering?"));
        outContent.reset();
        assemble.showRunTestMenu();
        assertTrue(outContent.toString().contains("good car is produced."));
    }

    @Test
    void testRunProducedCar_invalid() throws Exception {
        Field carInfo = Assemble.class.getDeclaredField("carInfo");
        carInfo.setAccessible(true);
        int[] arr = (int[]) carInfo.get(null);
        arr[0] = 1; // SEDAN
        arr[2] = 2; // CONTINENTAL (invalid for Sedan)
        assemble.runProducedCar();
        assertTrue(outContent.toString().contains("car is not work"));
    }

    @Test
    void testRunProducedCar_engineNotWork() throws Exception {
        Field carInfo = Assemble.class.getDeclaredField("carInfo");
        carInfo.setAccessible(true);
        int[] arr = (int[]) carInfo.get(null);
        arr[0] = 1; // SEDAN
        arr[1] = 4; // engine not work
        arr[2] = 1;
        arr[3] = 1;
        assemble.runProducedCar();
        assertTrue(outContent.toString().contains("engine is not work"));
    }

    @Test
    void testRunProducedCar_valid() throws Exception {
        Field carInfo = Assemble.class.getDeclaredField("carInfo");
        carInfo.setAccessible(true);
        int[] arr = (int[]) carInfo.get(null);
        arr[0] = 1; // SEDAN
        arr[1] = 1; // GM
        arr[2] = 1; // MANDO
        arr[3] = 1; // BOSCH
        assemble.runProducedCar();
        assertTrue(outContent.toString().contains("car is work"));
    }

    @Test
    void testTestProducedCar_allFailCases() throws Exception {
        Field carInfo = Assemble.class.getDeclaredField("carInfo");
        carInfo.setAccessible(true);
        int[] arr = (int[]) carInfo.get(null);

        arr[0] = 1; arr[2] = 2;
        assemble.testProducedCar();
        assertTrue(outContent.toString().contains("Sedan - Continental brake"));

        outContent.reset();
        arr[0] = 2; arr[1] = 2;
        assemble.testProducedCar();
        assertTrue(outContent.toString().contains("SUV - TOYOTA engine"));

        outContent.reset();
        arr[0] = 3; arr[1] = 3;
        assemble.testProducedCar();
        assertTrue(outContent.toString().contains("Truck - WIA engine"));

        outContent.reset();
        arr[0] = 3; arr[1] = 2; arr[2] = 1;
        assemble.testProducedCar();
        assertTrue(outContent.toString().contains("Mando"));

        outContent.reset();
        arr[2] = 3; arr[3] = 2;
        assemble.testProducedCar();
        assertTrue(outContent.toString().contains("Bosch brake - Bosch steering only use"));
    }

    @Test
    void testTestProducedCar_pass() throws Exception {
        Field carInfo = Assemble.class.getDeclaredField("carInfo");
        carInfo.setAccessible(true);
        int[] arr = (int[]) carInfo.get(null);
        arr[0] = 1; arr[1] = 1; arr[2] = 1; arr[3] = 1;
        assemble.testProducedCar();
        assertTrue(outContent.toString().contains("car assemble test : PASS"));
    }

    @Test
    void testFailMethod() {
        assemble.fail("fail message");
        assertTrue(outContent.toString().contains("car assemble test : FAIL"));
        assertTrue(outContent.toString().contains("fail message"));
    }

    @Test
    void testDelay() {
        long start = System.currentTimeMillis();
        assemble.delay(100);
        long end = System.currentTimeMillis();
        assertTrue(end - start >= 100);
    }
}