import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static void logTest(String msg){
        try(FileWriter writer = new FileWriter("log.txt", true))
        {
            writer.append(msg);
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
