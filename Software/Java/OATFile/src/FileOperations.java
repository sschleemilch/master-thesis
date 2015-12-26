import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileOperations {
	public static byte[] readFileToBytes(String pathToFile){
		
		byte [] result = null;
		Path path = Paths.get(pathToFile);
		try {
			System.out.println("Reading file: " + pathToFile);
			result = Files.readAllBytes(path);
		}catch (IOException e){
			System.err.println("Could not load ELF-File. Exiting");
			System.err.println(e.getMessage());
			System.exit(0);
		}
		return result;
	}
	
	public static void writeBytesToFile(String pathToFile, byte[] bytes){
		Path path = Paths.get(pathToFile);
		try{
			Files.deleteIfExists(path);
			System.out.println("Writing to file: " + pathToFile);
			Files.write(path, bytes, StandardOpenOption.CREATE_NEW);
		}catch (IOException e){
			System.err.println("Cannot write byte-Array to file. Exiting");
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}
}
