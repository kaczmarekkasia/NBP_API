package nbp.zad.URL;

public class WrongDateException extends Exception {
    public WrongDateException() {
        super("Data końcowa jest wcześniejsza niż data początkowa...popraw to :)");
    }
}
