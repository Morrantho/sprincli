javac Sprincli.java
jar cvfe Sprincli.jar Sprincli *.class
chmod +x Sprincli.jar

cd .. && mkdir build
cd src
cp Sprincli.jar ../build &&
cp application.properties ../build &&
cp messages.properties ../build &&
cp pom.txt ../build &&
cp Startup.txt ../build &&
cp TemplateConfig.txt ../build &&
cp TemplateController.txt ../build &&
cp TemplateModel.txt ../build &&
cp TemplateRepository.txt ../build &&
cp TemplateService.txt ../build &&
cp TemplateValidator.txt ../build &&
cp TemplateView.txt ../build
rm Sprincli.jar