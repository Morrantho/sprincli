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
* Windows + Git Bash support

## Notes: 

* Apache Maven is required to use some of Sprincli's commands,
so if something doesn't work, that could be why.

* When referring to "root" directory, you must be able to see your project's "pom.xml", otherwise you're in the wrong folder.

### Known Issues:

* Windows users will have to use Git Bash for Sprincli to work.

* <code>sprincli run</code> and <code>sprincli deploy</code>
don't work for Windows users yet.

### One time setup:

* Add an alias anywhere in your .bashrc. Its in your
home directory, but hidden. Run: ls -a to see it.
Open it with a text editor and add an alias, replacing the
example directory with the location of where you placed sprincli
on your pc. It must end with "Sprincli.jar" inside the "build" folder.

Example: <code>alias sprincli="java -jar /home/yourProfile/Documents/sprincli/build/Sprincli.jar"</code>

* Reload your .bashrc with: <code>source .bashrc</code>

### Using Sprincli:

* Run: <code>sprincli</code> in your terminal for a list of commands.

<code>sprincli new yourProject</code> will create a new spring boot application in your current directory. This must be done before you can use any other commands. After running new, <code>cd</code> into the project. You are now in your project's <code>root</code> directory.

* Run: <code>sprincli run</code> in your <code>root</code> directory to run your server.

### Deployment Steps:

* Enter <code>root</code> directory and run: <code>sprincli deploy</code>

* Enter <code>target</code> folder and run: <code>java -jar yourProject.war</code>

* Once you've verified it runs successfully, FTP the .war file
or push to git and clone the repo on your server.