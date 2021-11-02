import java.util.Scanner;
import static java.lang.Math.abs;
import static java.lang.Math.max;


public class Main {
    // -1 is empty space
    // 0 is a car
    // 1 is a truck
    // 2 is a building
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        /// size of the map
        String[] mapBoundaries = scanner.nextLine().split(" ");
        int width = Integer.parseInt(mapBoundaries[0]);
        int height = Integer.parseInt(mapBoundaries[1]);

        int[][] map = new int[height][width];
        for (int i = 0; i < height; i ++)
            for (int j = 0; j < width; j ++)
                map[i][j] = -1;

        /// Reading buildings:
        String[] buildings = scanner.nextLine().split(" ");
        int[] buildingCoordinates = new int[buildings.length];
        for(int k = 0; k < buildings.length; k ++)
            buildingCoordinates[k] = Integer.parseInt(buildings[k]) - 1;

        /// Placing all the buildings on the map:
        for (int k = 0; k < buildingCoordinates.length; k += 4) {
            final int x1 = buildingCoordinates[k];
            final int y1 = buildingCoordinates[k + 1];
            final int x2 = buildingCoordinates[k + 2];
            final int y2 = buildingCoordinates[k + 3];

            for (int i = y1; i <= y2; i ++)
                for (int j = x1; j <= x2; j ++)
                    map[i][j] = 2;
        }

        /// Input formal: type of vehicle, location (x, y) and fuel
        String[] vehiclesRaw = scanner.nextLine().split(" ");
        int[] vehicles = new int[vehiclesRaw.length];
        for (int k = 0; k < vehiclesRaw.length; k ++)
            vehicles[k] = Integer.parseInt(vehiclesRaw[k]);

        /// Placing vehicles on the map
        for (int k = 0; k < vehicles.length; k += 4)
            map[vehicles[k + 2] -= 1][vehicles[k + 1] -= 1] = vehicles[k];

        /// Movement of the vehicle
        String[] movementsRaw = scanner.nextLine().split(" ");
        int [] movements = new int[movementsRaw.length];
        for (int k = 0; k < movementsRaw.length; k ++)
            movements[k] = Integer.parseInt(movementsRaw[k]) - 1;

        for (int moveIndex = 0; moveIndex < movements.length; moveIndex += 4) {
            final int x1 = movements[moveIndex];
            final int y1 = movements[moveIndex + 1];
            final int x2 = movements[moveIndex + 2];
            final int y2 = movements[moveIndex + 3];

            /// Starting cell should be inside the map:
            if (y1 < 0 || x1 < 0 || y1 >= height || x1 >= width) {
                System.out.print("error");
                return;
            }

            /// No vehicle in the first cell:
            if (map[y1][x1] != 0 && map[y1][x1] != 1) {
                System.out.print("error");
                return;
            }

            /// Vehicle can move only to the adjacent cell:
            if (getDistance(x1, y1, x2, y2) != 1) {
                System.out.print("error");
                return;
            }

            for (int vehicleIndex = 0; vehicleIndex < vehicles.length; vehicleIndex += 4) {
                final int vehicleType = vehicles[vehicleIndex];
                final int vehicleX = vehicles[vehicleIndex + 1];
                final int vehicleY = vehicles[vehicleIndex + 2];
                final int vehicleFuel = vehicles[vehicleIndex + 3];

                if (vehicleX != x1 || vehicleY != y1)
                    continue;

                if (y2 < 0 || x2 < 0 || y2 >= height || x2 >= width) {
                    System.out.print("out of bound");
                    return;
                }

                /// Checking if out of fuel
                if (vehicleFuel < (vehicleType + 1)) {
                    System.out.print("out of fuel");
                    return;
                }

                /// Target cell is occupied with building
                if (map[y2][x2] == 2) {
                    System.out.print("road accident");
                    return;
                }

                map[y1][x1] = -1;
                /// Adjacent cells to the target should not be occupied with vehicles
                for (int i = y2 - 1; i <= y2 + 1; i ++)
                    for (int j = x2 - 1; j <= x2 + 1; j ++) {
                        if (i < 0 || i >= height || j < 0 || j >= width || (i == y2 && j == x2))
                            continue;

                        if (map[i][j] == 0 || map[i][j] == 1) {
                            System.out.print("vehicles are too close to each other");
                            return;
                        }
                    }

                /// Passed all check, so we need to update vehicle information
                vehicles[vehicleIndex + 1] = x2;
                vehicles[vehicleIndex + 2] = y2;
                vehicles[vehicleIndex + 3] = vehicleFuel - (vehicleType + 1);
                map[y2][x2] = vehicleType;
                break;
            }
        }

        int fuel = -1;

        for (int i = 0; i < vehicles.length; i += 4)
            fuel = max(fuel, vehicles[i + 3]);

        for (int i = 0; i < vehicles.length; i += 4)
            if (vehicles[i + 3] == fuel) {
                System.out.print(vehicles[i] + " " + (vehicles[i + 1] + 1) + " " + (vehicles[i + 2] + 1));
                break;
            }
    }

    static int getDistance (final int x1, final int y1, final int x2, final int y2) {
        return max(abs(x1 - x2), abs(y1 - y2));
    }
}
