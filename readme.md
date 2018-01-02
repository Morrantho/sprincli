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
so if something doesn't work, make sure it's installed.

* When referring to <code>root</code> directory, you must be able to see your project's <code>pom.xml</code>, otherwise you're in the wrong folder.
You need to be here at all times to run sprincli commands.

### Known Issues:

* Let me know.

### Unix / Linux / Mac Install:

* Add an alias anywhere in your .bashrc. To get there, type <code>cd</code>
anywhere in a terminal and press enter. Its hidden, Run: <code>ls -a</code> to see it. If its not there, create it with <code>touch .bashrc</code>.
Open it with a text editor and add an alias, replacing the
example directory with the location of where you placed sprincli
on your pc. It must end with <code>Sprincli.jar</code> inside the <code>build</code> folder.

Example: <code>alias sprincli="java -jar /home/yourProfile/Documents/sprincli/build/Sprincli.jar"</code>

* Reload your .bashrc with: <code>source .bashrc</code>

### Windows Install:

#### Command Prompt:

* Make a shortcut to cmd.exe on your desktop.
Right-click it, goto Properties > Shortcut.

* Add this onto <code>Target:</code> <code>/K C:\Users\yourName\Documents\doskey.cmd</code>

* Create doskey.cmd in that spot above.

* Add this to doskey.cmd: <code>DOSKEY sprincli=java -jar C:\Users\yourName\Documents\code\Java\sprincli\build\Sprincli.jar $1 $2
</code> replacing the directory with your own. Save and close everything.

* Reopen cmd prompt from your desktop shortcut. Run: <code>sprincli</code>
to verify it works. You MUST use the shortcut you created as it points to
the alias we created.

#### Git Bash:

Same as UNIX instructions.

* Open a git bash terminal.
* <code>cd</code> and press enter.
* <code>touch .bashrc</code> if you don't have one already.
* Proceed with UNIX instructions.

### Using Sprincli:

* Run: <code>sprincli</code> in your terminal for a list of commands.

<code>sprincli new yourProject</code> will create a new spring boot application in your current directory. This must be done before you can use any other commands. After running new, <code>cd</code> into the project. You are now in your project's <code>root</code> directory.

* Run: <code>sprincli run</code> in your <code>root</code> directory to run your server.

### Deployment Steps:

* Enter <code>root</code> directory and run: <code>sprincli deploy</code>

* Enter <code>target</code> folder and run: <code>java -jar yourProject.war</code>

* Once you've verified it runs successfully, FTP the .war file
or push to git and clone the repo on your server.