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

On windows, add a new system variable, including Sprincli.jar

### Now what?

Run $sprincli in your terminal for a list of commands.

$sprincli new <projectName> will create a new spring boot application. This must be done before you can use any other commands. Then cd into the project and perform more commands.

The run command doesnt work yet. Use <code>mvn spring-boot:run</code> at the root of your project for the time being. You'll need maven of course.