public class TileScoreManager {
    public int getTilePoints(String c) {
        c = c.replaceAll(" ", "");
        if (c.length() == 1) {
            switch (c) {
                case "a":
                case "e":
                case "i":
                case "o":
                case "u":
                case "n":
                case "r":
                case "t":
                case "l":
                case "s":
                    return 1;
                case "d":
                case "g":
                    return 2;
                case "b":
                case "c":
                case "m":
                case "p":
                    return 3;
                case "f":
                case "h":
                case "v":
                case "w":
                case "y":
                    return 4;
                case "k":
                    return 5;
                case "j":
                case "x":
                    return 8;
                case "z":
                    return 10;
                default:
                    return 0;
            }
        }
        return 0;
    }
}
