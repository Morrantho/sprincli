import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.net.URL;
import java.net.MalformedURLException;

public class Util{
	static FileReader fr;
	static BufferedReader br;
	static FileWriter fw;
	static BufferedWriter bw;
	static String line = "";
	static String text = "";
	static String OS = System.getProperty("os.name").toLowerCase();
	
	public static void copy(File source,File dest) throws IOException{
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;

		System.out.println("Creating file: "+dest);

		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}

	public static String toWindows(String str){
		String s = str.replace("/","\\");
		return s;
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static void openFile(String dir){

	}

	public static String readFile(String dir){
		line = "";
		text = "";
		try{		
			fr = new FileReader(dir);
			br = new BufferedReader(fr);
			while((line = br.readLine()) != null)
				text += line+"\n";
		}catch(IOException e){
			System.out.println("Failed to read file: "+dir);
		}

		return text;
	}

	public static void closeFile(){

	}
	public static void writeToFile(String dir,String newText){
		try{
			fw = new FileWriter(dir);
			bw = new BufferedWriter(fw);
			bw.write(newText);
			bw.close();
		}catch(IOException e){
			System.out.println("Failed to read file: "+dir);
		}
	}

	public static URL getLocation(final Class<?> c) {
	    if (c == null) return null;

	    try {
	        final URL codeSourceLocation =
	            c.getProtectionDomain().getCodeSource().getLocation();
	        if (codeSourceLocation != null) return codeSourceLocation;
	    }
	    catch (final SecurityException e){}
	    catch (final NullPointerException e){}

	    final URL classResource = c.getResource(c.getSimpleName() + ".class");
	    if (classResource == null) return null; // cannot find class resource

	    final String url = classResource.toString();
	    String suffix = c.getCanonicalName().replace('.', '/') + ".class";

	    // if(Util.isWindows()) suffix = Util.toWindows(suffix);
	    if (!url.endsWith(suffix)) return null; // weird URL

	    // strip the class's path from the URL string
	    final String base = url.substring(0, url.length() - suffix.length());

	    String path = base;

	    // remove the "jar:" prefix and "!/" suffix, if present
	    if (path.startsWith("jar:")) path = path.substring(4, path.length() - 2);

	    try {
	        return new URL(path);
	    }
	    catch (final MalformedURLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}