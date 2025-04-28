import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Result {
    private static final String BLANK_SPACE = " ";

    /*
     * Complete the 'matrixRotation' function below.
     *
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY matrix
     *  2. INTEGER r
     */
    public static void matrixRotation(List<List<Integer>> matrix, int r) {
        int m = matrix.size(); // number of rows (height)
        int n = matrix.get(0).size(); // number of columns (width)
        int layers = Math.min(m, n) / 2; // We can only have as many layers as half the smallest side

        for (int layer = 0; layer < layers; layer++) {
            List<int[]> coords = collectCoordinates(m, n, layer);
            List<Integer> layerElements = extractLayerElements(matrix, coords);
            List<Integer> rotatedLayerElements = rotate(layerElements, r);
            placeRotatedElementsBack(matrix, coords, rotatedLayerElements);
        }

        System.out.println("------------------------------------------------");
        System.out.printf("The result for %sx%s matrix, rotation factor %s, is: %n", m, n, r);
        System.out.println("------------------------------------------------");

        printMatrix(matrix);
    }

    /**
     *
     * @param m - number of rows (height) of a 2D_INTEGER_ARRAY
     * @param n - number of columns (width) of a 2D_INTEGER_ARRAY
     * @param layer of a 2D_INTEGER_ARRAY matrix
     * @return collected coordinates of 2D_INTEGER_ARRAY in an array
     */
    private static List<int[]> collectCoordinates(int m, int n, int layer) {
        List<int[]> coords = new ArrayList<>();

        // top
        for (int j = layer; j < n - layer; j++) {
            coords.add(new int[]{layer, j});
        }

        // right
        for (int i = layer + 1; i < m - layer - 1; i++) {
            coords.add(new int[]{i, n - layer - 1});
        }

        // bottom
        for (int j = n - layer - 1; j >= layer; j--) {
            coords.add(new int[]{m - layer - 1, j});
        }

        // left
        for (int i = m - layer - 2; i > layer; i--) {
            coords.add(new int[]{i, layer});
        }

        return coords;
    }

    private static List<Integer> extractLayerElements(List<List<Integer>> matrix,
                                                      List<int[]> coords) {
        return coords.stream()
                .map(coord -> matrix.get(coord[0]).get(coord[1]))
                .toList();

    }

    private static List<Integer> rotate(List<Integer> layerElements, int r) {
        int len = layerElements.size();
        int shift = r % len;

        // returning rotated layer elements
        return IntStream.range(0, len)
                .mapToObj(i -> layerElements.get((i + shift) % len))
                .toList();
    }

    private static void placeRotatedElementsBack(List<List<Integer>> matrix,
                                                 List<int[]> coords,
                                                 List<Integer> rotatedLayerElements) {
        for (int i = 0; i < coords.size(); i++) {
            int[] coord = coords.get(i);
            matrix.get(coord[0])
                    .set(coord[1], rotatedLayerElements.get(i));
        }
    }

    private static void printMatrix(List<List<Integer>> matrix) {
        for (List<Integer> row : matrix) {
            for (Integer val : row) {
                System.out.print(val + BLANK_SPACE);
            }
            System.out.println();
        }
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        System.out.println("Please enter your input matrix:");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int m = Integer.parseInt(firstMultipleInput[0]); // Rows

        int n = Integer.parseInt(firstMultipleInput[1]); // Columns

        int r = Integer.parseInt(firstMultipleInput[2]); // Rotations

        List<List<Integer>> matrix = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                matrix.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Result.matrixRotation(matrix, r);

        bufferedReader.close();
    }
}
