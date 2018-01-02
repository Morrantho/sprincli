import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.Runtime;
import java.lang.Process;

import java.util.ArrayList;
import java.lang.InterruptedException;

public class Sprincli{
	static String app    = "";
	static String dir    = "";
	static String text   = ""; //file text
	static String root   = "";
	static String srcDir = "";
	static String pkgDir = "";
	static String dest   = "";
	static String classPath = getPath(); // get execution directory	
	
	private static void parse(String dir,String arg,String copy,boolean shouldRepl,String ext){
		String newFile = dir+"/"+arg+ext;

		try{
			Util.copy(new File(classPath+copy+".txt"),new File(newFile));

			if(shouldRepl){
				readAndReplace(newFile,
					new String[]{"..",copy},
					new String[]{"."+app+".",arg}
				);
			}

			System.out.println("Created file: "+newFile);			
		}catch(IOException e){
			System.out.println("Failed to copy file: "+copy+".txt");
		}
	}

	static Command[] commands = {
		new Command(
			"new",
			"Usage: <projectName> --Creates a new Spring Boot Project",
			(String... args)->{
				if(isProject()){
					System.out.println("You cannot nest spring boot projects.");
					return;
				}
				if(args.length < 2){
					System.out.println("Supply a project name.");
					return;
				}
				String arg = args[1];
				System.out.println("Initializing Project...");
				root   = dir+"/"+arg;
				srcDir = root+"/src/main/";
				pkgDir = "java/com/project/"+arg;
				dest   = srcDir+pkgDir;

				String[] directories = {
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
				String view = srcDir+"/webapp/WEB-INF/index.jsp";

				try{
					Util.copy(new File(classPath+"Startup.txt"),new File(startup));
					Util.copy(new File(classPath+"pom.txt"),new File(pom));
					Util.copy(new File(classPath+"application.properties"),new File(appProp));
					Util.copy(new File(classPath+"messages.properties"),new File(msgProp));
					Util.copy(new File(classPath+"TemplateView.txt"),new File(view));
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
				// Main Class for deployment:
				readAndReplace(pom,new String[]{
					"<mainClass>com.project."
				},new String[]{
					"<mainClass>com.project."+arg+"."+arg+"Application"
				});
				
				// Save project name to determine future project path
				Util.writeToFile(dir+"/"+arg+"/project.txt",dir+"/"+arg);
			
				System.out.println("Project created successfully. Run: sprincli run in your root directory to launch your server.");
			}
		),

		new Command(
			"run",
			"Usage: <> --Runs your server.",
			(String ...args)->{
				execute("mvn spring-boot:run");
			}
		),

		new Command(
			"login",
			"Usage: <> --Creates a basic login and registration with BCrypt.",
			(String ...args)->{
				parse(dest+"/models","User","TemplateUser",true,".java");
				parse(dest+"/services","UserService","TemplateUserService",true,".java");
				parse(dest+"/repositories","UserRepository","TemplateUserRepository",true,".java");
				parse(dest+"/controllers","RouteController","TemplateRouteController",true,".java");
				parse(dest+"/controllers","UserController","TemplateUserController",true,".java");

				parse(srcDir+"webapp/WEB-INF","dashboard","dashboard",false,".jsp");
				parse(srcDir+"webapp/WEB-INF","newUser","newUser",false,".jsp");

				String pom = dir+"/pom.xml";
				String props = dir+"/src/main/resources/application.properties";
				
				// Uncomment JPA + MySQL Dependencies
				readAndReplace(pom,new String[]{"<!--","-->"},new String[]{"",""});

				// Uncomment MySQL configuration
				readAndReplace(props,new String[]{"#"},new String[]{""});
				
				System.out.println("\n!!!CHANGE YOUR DATABASE IN application.properties IF YOU HAVENT ALREADY!!!");
			}
		),

		new Command(
			"model",
			"Usage: <modelName> --Creates a new Model",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a model name."); return;}
				String arg = args[1];
				parse(dest+"/models",arg,"TemplateModel",true,".java");
			}
		),

		new Command(
			"controller",
			"Usage: <controllerName> --Creates a new Controller",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a controller name."); return;}
				String arg = args[1];
				parse(dest+"/controllers",arg,"TemplateController",true,".java");
			}
		),

		new Command(
			"view",
			"Usage: <viewName> --Creates a new view / .jsp",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a view name"); return;}
				String arg = args[1];
				parse(srcDir+"/webapp/WEB-INF",arg,"TemplateView",false,".jsp");
			}
		),

		new Command(
			"service",
			"Usage: <serviceName> --Creates a new Service",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a service name"); return;}
				String arg = args[1];
				parse(dest+"/services",arg,"TemplateService",true,".java");
			}
		),

		new Command(
			"repository",
			"Usage: <repositoryName> --Creates a new Repository",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a repositoryName"); return;}
				String arg = args[1];
				parse(dest+"/repositories",arg,"TemplateRepository",true,".java");
			}
		),

		new Command(
			"validator",
			"Usage: <validatorName> --Creates a new Validator.",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a validator name."); return;}
				String arg = args[1];
				parse(dest+"/validators",arg,"TemplateValidator",true,".java");
			}
		),

		new Command(
			"deploy",
			"Usage: <> --Compiles a .war file for deployment.",
			(String ...args)->{
				execute("mvn clean package");
			}
		),
	};

	static void execute(String cmd){
		try{				
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = br.readLine();
			while(line != null){
				System.out.println(line);
				line = br.readLine();
			}

			int exitVal = p.waitFor();
			System.out.println("Error: "+exitVal);
		}
		catch(IOException e1){}
		catch(InterruptedException e2){}		
	}

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
				// if(args.length<2){
				// 	System.out.println(command.usage);
				// 	return;
				// }	

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
				command.event.call(args);
				return;
			}
		}
		help();
	}
}
