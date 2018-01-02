import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.lang.Runtime;
import java.lang.Process;

import java.util.ArrayList;
import java.lang.InterruptedException;
import java.util.Scanner;

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

		if(Util.isWindows()){
			newFile = Util.toWindows(newFile);
			classPath = Util.toWindows(classPath)+"\\";
			classPath = classPath.substring(1,classPath.length()-1);
		}

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
				app = arg;
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
					if(Util.isWindows())
						directory = Util.toWindows(directory);

					System.out.println("Creating directory: "+directory);
					File d = new File(directory);
					d.mkdirs();
				}
			
				// Pre-Locate these file destinations:
				String startup = dest+"/"+arg+"Application.java";
				String pom     = dir+"/"+arg+"/pom.xml";
				String router  = dest+"/controllers/Router.java";
				String appProp = srcDir+"/resources/application.properties";
				String msgProp = srcDir+"/resources/messages.properties";
				String view    = srcDir+"/webapp/WEB-INF/index.jsp";
				String stat    = srcDir+"/resources/static/";

				if(Util.isWindows()){
					startup = Util.toWindows(startup);
					pom = Util.toWindows(pom);
					appProp = Util.toWindows(appProp);
					msgProp = Util.toWindows(msgProp);
					view = Util.toWindows(view);
				}

				try{
					Util.copy(new File(classPath+"Startup.txt"),new File(startup));
					Util.copy(new File(classPath+"pom.txt"),new File(pom));
					Util.copy(new File(classPath+"application.properties"),new File(appProp));
					Util.copy(new File(classPath+"messages.properties"),new File(msgProp));
					Util.copy(new File(classPath+"TemplateView.txt"),new File(view));
					// Create first controller.
					parse(dest+"/controllers","Router","TemplateRouter",true,".java");
					
					Util.writeToFile(stat+"css/style.css","");
					Util.writeToFile(stat+"js/main.js","");
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
				
				String d = dir+"/"+arg+"/project.txt";
				String e = dir+"/"+arg;

				if(Util.isWindows()){
					d = Util.toWindows(d);
					e = Util.toWindows(e);
				}

				// Save project name to determine future project path
				Util.writeToFile(d,e);
			
				System.out.println("\nProject created successfully. Run: sprincli run in your root directory to launch your server.");
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
				String models   = dest+"/models";
				String services = dest+"/services";
				String repos    = dest+"/repositories";
				String cont     = dest+"/controllers";
				String views    = srcDir+"webapp/WEB-INF";
				String pom      = dir+"/pom.xml";
				String props    = dir+"/src/main/resources/application.properties";

				if(Util.isWindows()){
					models = Util.toWindows(models);
					services = Util.toWindows(services);
					repos = Util.toWindows(repos);
					cont = Util.toWindows(cont);
					views = Util.toWindows(views);
					pom = Util.toWindows(pom);
					props = Util.toWindows(props);
				}

				parse(models,"User","TemplateUser",true,".java");
				parse(services,"UserService","TemplateUserService",true,".java");
				parse(repos,"UserRepository","TemplateUserRepository",true,".java");
				parse(cont,"RouteController","TemplateRouteController",true,".java");
				parse(cont,"UserController","TemplateUserController",true,".java");

				parse(views,"dashboard","dashboard",false,".jsp");
				parse(views,"newUser","newUser",false,".jsp");

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
				String models = dest+"/models";

				if(Util.isWindows()) models = Util.toWindows(models);

				parse(models,arg,"TemplateModel",true,".java");
			}
		),

		new Command(
			"controller",
			"Usage: <controllerName> --Creates a new Controller",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a controller name."); return;}
				String arg = args[1];
				String cont = dest+"/controllers";
				if(Util.isWindows()) cont = Util.toWindows(cont);
				parse(cont,arg,"TemplateController",true,".java");
			}
		),

		new Command(
			"view",
			"Usage: <viewName> --Creates a new view / .jsp",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a view name"); return;}
				String arg = args[1];
				String views = srcDir+"/webapp/WEB-INF";
				if(Util.isWindows()) views = Util.toWindows(views);
				parse(views,arg,"TemplateView",false,".jsp");
			}
		),

		new Command(
			"service",
			"Usage: <serviceName> --Creates a new Service",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a service name"); return;}
				String arg = args[1];
				String svc = dest+"/services";
				if(Util.isWindows()) svc = Util.toWindows(svc);
				parse(svc,arg,"TemplateService",true,".java");
			}
		),

		new Command(
			"repository",
			"Usage: <repositoryName> --Creates a new Repository",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a repositoryName"); return;}
				String arg = args[1];
				String repos = dest+"/repositories";
				if(Util.isWindows()) repos = Util.toWindows(repos);
				parse(repos,arg,"TemplateRepository",true,".java");
			}
		),

		new Command(
			"validator",
			"Usage: <validatorName> --Creates a new Validator.",
			(String ...args)->{
				if(args.length < 2){System.out.println("Supply a validator name."); return;}
				String arg = args[1];
				String vals = dest+"/validators";
				if(Util.isWindows()) vals = Util.toWindows(vals);
				parse(vals,arg,"TemplateValidator",true,".java");
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
		if(Util.isWindows()){cmd = "cmd /c "+cmd;} // exec() is OS dependant.

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
		catch(IOException e1){System.out.println(e1);}
		catch(InterruptedException e2){System.out.println(e2);}		
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
				if(!command.key.equals("new") && !isProject()){
					System.out.println("project.txt not found. Navigate to your project directory or create a project with 'new' before using: "+command.key);
					return; 
				}else if(!command.key.equals("new") && isProject()){
					root   = dir;
					app    = root.substring(root.lastIndexOf("/")+1,root.length());
					srcDir = root+"/src/main/";
					pkgDir = "java/com/project/"+app;
					dest   = srcDir+pkgDir;

					if(Util.isWindows()){
						root = Util.toWindows(root);
						app = Util.toWindows(app);
						app = root.substring(root.lastIndexOf("\\")+1,root.length());
						srcDir = Util.toWindows(srcDir);
						pkgDir = "java\\com\\project\\"+app;
						pkgDir = Util.toWindows(pkgDir);
						dest = Util.toWindows(dest);
						dest = srcDir+pkgDir;
					}
				}
				command.event.call(args);
				return;
			}
		}
		help();
	}
}
