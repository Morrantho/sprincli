# Sprincli

### Features

* Generate Project
* Generate Packages
* Generate Models
* Generate Views
* Generate Controllers
* Generate Validators
* Generate WebConfig
* Generate Views
* Generate Repositories
* Generate Services

### Great, how do I use it?

On Unix systems, add a new variable to your etc/bash.bashrc

Ex: sprincli="java -jar /home/yourProfile/Documents/sprincli/build/Sprincli.jar"

Now you can access it directly in terminal with: $sprincli <commandhere>

### Now what?

Run $sprincli in your terminal for a list of commands.

$sprincli new <projectName> will create a new spring boot application. This must be done before you can use any other commands. After running new, cd into the project to perform more commands. If you cant see the pom.xml, you're in the wrong folder.

Use <code>mvn spring-boot:run</code> at the root of your project to launch your spring server.