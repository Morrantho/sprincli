import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import java.lang.Runtime;
import java.lang.Process;

//TODO:
// Generate Views

public class Sprincli{
	static String app    = "";
	static String dir    = "";
	static String text   = ""; //file text
	static String root   = "";
	static String srcDir = "";
	static String pkgDir = "";
	static String dest   = "";
	static String classPath = getPath(); // get execution directory

	static Command[] commands = {
		new Command(
			"new",
			"Usage: <projectName> --Creates a new Spring Boot Project",
			(String arg)->{
				System.out.println("Initializing Project...");
				root   = dir+"/"+arg;
				srcDir = root+"/src/main/";
				pkgDir = "java/com/project/"+arg;
				dest   = srcDir+pkgDir;

				String[] directories = {
					dest+"/config",
					dest+"/models",
					dest+"/controllers",
					dest+"/repositories",
					dest+"/services",
					dest+"/validators",

					srcDir+"resources/static/css",
					srcDir+"resources/static/js",
					
					srcDir+"webapp/WEB-INF"
				};

				for(String directory:directories){
					System.out.println("Creating directory: "+directory);
					File d = new File(directory);
					d.mkdirs();
				}
			
				// Pre-Locate these file destinations:
				String startup = dest+"/"+arg+"Application.java";
				String pom     = dir+"/"+arg+"/pom.xml";
				String appProp = srcDir+"/resources/application.properties";
				String msgProp = srcDir+"/resources/messages.properties";

				try{
					Util.copy(new File(classPath+"Startup.txt"),new File(startup));
					Util.copy(new File(classPath+"pom.txt"),new File(pom));
					Util.copy(new File(classPath+"application.properties"),new File(appProp));
					Util.copy(new File(classPath+"messages.properties"),new File(msgProp));					
				}catch(IOException e){
					System.out.println("Failed to read file: ");
				}
				// main.java:
				readAndReplace(startup,new String[]{
					"com.project.","class Application",".run("
				},new String[]{
					"com.project."+arg,"class "+arg+"Application",".run("+arg
				});
				// pom.xml:
				readAndReplace(pom,new String[]{
					"<groupId>com.example","<artifactId>myproject"
				},new String[]{
					"<groupId>com.project."+arg,"<artifactId>"+arg
				});
				// Save project name to determine future project path
				Util.writeToFile(dir+"/"+arg+"/project.txt",dir+"/"+arg);
			}
		),

		new Command(
			"model",
			"Usage: <modelName> --Creates a new Model",
			(String arg)->{
				String model = dest+"/models/"+arg+".java";

				try{
					Util.copy(new File(classPath+"TemplateModel.txt"),new File(model));

					readAndReplace(model,
						new String[]{"..","TemplateModel"},
						new String[]{"."+app+".",arg}
					);

					System.out.println("Created Model: "+arg);
				}catch(IOException e){
					System.out.println("Model creation failed: "+arg);
				}
			}
		),

		new Command(
			"controller",
			"Usage: <controllerName> --Creates a new Controller",
			(String arg)->{
				String controller = dest+"/controllers/"+arg+".java";

				try{
					Util.copy(new File(classPath+"TemplateController.txt"),new File(controller));

					readAndReplace(controller,
						new String[]{"..","TemplateController"},
						new String[]{"."+app+".",arg}
					);

					System.out.println("Created controller: "+arg);
				}catch(IOException e){
					System.out.println("Controller creation failed: "+arg);
				}
			}
		),

		new Command(
			"view",
			"Usage: <viewName> --Creates a new view / .jsp",
			(String arg)->{
				String view = srcDir+"/webapp/WEB-INF/"+arg+".jsp";

				try{
					Util.copy(new File(classPath+"TemplateView.txt"),new File(view));
					System.out.println("Created view: "+arg);
				}catch(IOException e){
					System.out.println("View creation failed: "+arg);
				}
			}
		),

		new Command(
			"service",
			"Usage: <serviceName> --Creates a new Service",
			(String arg)->{
				String service = dest+"/services/"+arg+".java";

				try{
					Util.copy(new File(classPath+"TemplateService.txt"),new File(service));

					readAndReplace(service,
						new String[]{"..","TemplateService"},
						new String[]{"."+app+".",arg}
					);

					System.out.println("Created Service: "+arg);
				}catch(IOException e){
					System.out.println("Service creation failed: "+arg);
				}
			}
		),

		new Command(
			"repository",
			"Usage: <viewName> --Creates a new Repository",
			(String arg)->{
				String repository = dest+"/repositories/"+arg+".java";

				try{
					Util.copy(new File(classPath+"TemplateRepository.txt"),new File(repository));

					readAndReplace(repository,
						new String[]{"..","TemplateRepository"},
						new String[]{"."+app+".",arg}
					);

					System.out.println("Created Repository: "+arg);
				}catch(IOException e){
					System.out.println("Repository creation failed: "+arg);
				}
			}
		),

		new Command(
			"config",
			"Usage: <configName> --Creates a new WebSecurityConfig. Argument is ignored, but required",
			(String arg)->{
				String config = dest+"/config/WebSecurityConfig.java";

				try{
					Util.copy(new File(classPath+"TemplateConfig.txt"),new File(config));
					readAndReplace(config,new String[]{".."},new String[]{"."+app+"."});
					System.out.println("Created config: "+arg);
				}catch(IOException e){
					System.out.println("Config creation failed: "+arg);
				}
			}
		),

		new Command(
			"validator",
			"Usage: <validatorName> --Creates a new Validator.",
			(String arg)->{
				String validator = dest+"/validators/"+arg+".java";

				try{
					Util.copy(new File(classPath+"TemplateValidator.txt"),new File(validator));

					readAndReplace(validator,
						new String[]{"..","TemplateValidator"},
						new String[]{"."+app+".",arg}
					);

					System.out.println("Created validator: "+arg);
				}catch(IOException e){
					System.out.println("validator creation failed: "+arg);
				}
			}
		),
		
		//Doesnt work yet.
		new Command(
			"run",
			"Usage: <arg> --Run a Spring Boot App. Argument required, but ignored.",
			(String arg)->{
				try{
					Runtime runtime = Runtime.getRuntime();
					Process p = runtime.exec("mvn spring-boot:run");
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		),
	};

	static void help(){
		System.out.println("Commands:");
		for(Command command:commands)
			System.out.println(command.key+" - "+command.usage);
	}

	// Look for previously generated project.txt
	public static boolean isProject(){
		File folder = new File(dir);
		for(File file:folder.listFiles()){
			if(file.getName().equals("project.txt"))
				return true;
		}
		return false;
	}

	public static String getPath(){
		String s = Util.getLocation(Sprincli.class).toString();
		s = s.substring(5,s.length());    // remove file:
		s = s.replace("Sprincli.jar",""); // prevent classpath issues when searching for files to copy from.
		return s;
	}

	public static void readAndReplace(String fil,String[] replaces,String[] replaceWith){
		if(replaces.length > replaceWith.length || replaceWith.length > replaces.length){return;}
		text = Util.readFile(fil);

		for(int i=0;i<replaces.length;i++)
			text = text.replace(replaces[i],replaceWith[i]);
			
			Util.writeToFile(fil,text);			
		}	
		
	public static void main(String[] args){
		if(args.length<1){help(); return;}
		dir = System.getProperty("user.dir");

		for(Command command:commands){
			if(command.key.equals(args[0])){
				if(args.length<2){
					System.out.println(command.usage);
					return;
				}	

				if(!command.key.equals("new") && !isProject()){
					System.out.println("project.txt not found. Navigate to your project directory or create a project with 'new' before using: "+command.key);
					return; 
				}else if(!command.key.equals("new") && isProject()){
					root   = dir;
					app    = root.substring(root.lastIndexOf("/")+1,root.length());
					srcDir = root+"/src/main/";
					pkgDir = "java/com/project/"+app;
					dest   = srcDir+pkgDir;
				}
				command.event.call(args[1]);
				return;
			}
		}
		help();
	}
}
