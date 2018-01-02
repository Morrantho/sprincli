# Sprincli

### Features
* Generate Project
* Generate Models
* Generate Views
* Generate Controllers
* Generate Validators
* Generate Repositories
* Generate Services
* Generate Login + Registration
* Project Deployment
* Run server
* Maven Support

## Notes: 

* Apache Maven is required to use some of Sprincli's commands,
so if something doesn't work, that could be why.

* When referring to "root" directory, you must be able to see your project's "pom.xml", otherwise you're in the wrong folder.

### One time setup:
* Add an alias anywhere in your .bashrc. Its in your
home directory, but hidden. Run: ls -a to see it.
Open it with a text editor and add an alias, replacing the
example directory with the location of where you placed sprincli
on your pc. It must end with "Sprincli.jar" inside the "build" folder.

Example: alias sprincli="java -jar /home/yourProfile/Documents/sprincli/build/Sprincli.jar"

* Reload your .bashrc with: source .bashrc

### Using Sprincli:
* Run: "sprincli" in your terminal for a list of commands.

sprincli new <projectName> will create a new spring boot application in your current directory. This must be done before you can use any other commands. After running new, cd into the project. You are now in your project's "root" directory.

* Run <code>mvn spring-boot:run</code> in your root directory to run your server.

### Deployment Steps:
* Enter root directory and run: sprincli deploy
* Enter target folder and run: java -jar yourProject.war
* Once you've verified it runs successfully, FTP the .war file
or push to git and clone the repo on your server.